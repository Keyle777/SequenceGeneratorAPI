package keyle.fun.SequenceGeneratorAPI.service.impl;

import keyle.fun.SequenceGeneratorAPI.service.Sequence;
import keyle.fun.SequenceGeneratorAPI.utils.SegException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基于雪花算法的实现方式
 */
@Slf4j
@Service
public class SnowflakeSequenceServiceImpl implements Sequence {

    @Value("${snowflake.workerId:1}")
    private long workerId;

    @Value("${snowflake.datacenterId:1}")
    private long datacenterId;

    private long sequence = 0L;

    private final long twepoch = 1288834974657L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);
    private long lastTimestamp = -1L;

    @Override
    public synchronized String nextNo() {
        try {
            long timestamp = timeGen();

            // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new SegException("时钟回退，无法生成序列号");
            }

            // 如果在相同毫秒内，递增序列号
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    // 序列号溢出，等待下一毫秒
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                // 如果是新的毫秒，重置序列号
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            // 构建ID
            long id = ((timestamp - twepoch) << timestampLeftShift) |
                    (datacenterId << datacenterIdShift) |
                    (workerId << workerIdShift) |
                    sequence;

            // 格式化成字符串
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return dateFormat.format(new Date(id));
        } catch (Exception e) {
            log.error("生成序列号失败", e);
            throw new SegException("生成序列号失败，异常信息:" + e);
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
