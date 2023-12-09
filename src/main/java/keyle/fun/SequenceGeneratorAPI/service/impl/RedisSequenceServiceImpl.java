package keyle.fun.SequenceGeneratorAPI.service.impl;

import keyle.fun.SequenceGeneratorAPI.service.Sequence;
import keyle.fun.SequenceGeneratorAPI.utils.RedisCache;
import keyle.fun.SequenceGeneratorAPI.utils.SegException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的实现方式
 */
@Slf4j
@Service
public class RedisSequenceServiceImpl implements Sequence {

    @Resource
    private RedisCache redisCache;

    private static final String SEQUENCE_KEY_PREFIX = "sequence:";

    @Override
    public String nextNo() {
        try {
            String sequenceNo = generateSequenceNo();
            String key = SEQUENCE_KEY_PREFIX + sequenceNo;

            // 利用Redis的原子性操作来实现并发防重
            boolean success = redisCache.setCacheObjectIfAbsent(key, "");
            if (success) {
                redisCache.expire(key, 1, TimeUnit.MINUTES); // 设置过期时间
                return sequenceNo;
            } else {
                throw new SegException("生成序列号失败，重复生成");
            }
        } catch (Exception e) {
            log.error("生成序列号失败", e);
            throw new SegException("生成序列号失败，异常信息:" + e);
        }
    }

    private String generateSequenceNo() {
        // 实现生成序列号的逻辑
        return "SN" + UUID.randomUUID();
    }
}
