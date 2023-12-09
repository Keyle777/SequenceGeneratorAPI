package keyle.fun.SequenceGeneratorAPI.service.impl;

import keyle.fun.SequenceGeneratorAPI.entity.SequenceNumber;
import keyle.fun.SequenceGeneratorAPI.service.Sequence;
import keyle.fun.SequenceGeneratorAPI.utils.DatabaseManager;
import keyle.fun.SequenceGeneratorAPI.utils.SegException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.*;
/**
 * 基于Mysql数据库的实现方式
 */
@Slf4j
@Service
public class DatabaseSequenceServiceImpl implements Sequence {

    @Resource
    private DatabaseManager databaseManager;

    private static boolean tableCreated = false;

    private final Object lock = new Object();

    // 本地缓存，用于并发防重，设置过期时间为1分钟
    private final ConcurrentHashMap<String, Boolean> localCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newScheduledThreadPool(1);

    @Override
    public String nextNo() {
        try {
            String sequenceNo = generateSequenceNo();

            // 使用本地缓存实现并发防重
            if (localCache.putIfAbsent(sequenceNo, Boolean.TRUE) == null) {
                // 缓存不存在，说明序列号是第一次生成
                try {
                    synchronized (lock) {
                        if (!tableCreated) {
                            databaseManager.createTable();
                            tableCreated = true;
                        }

                        SequenceNumber existingSequence = databaseManager.findBySequenceNo(sequenceNo);

                        if (existingSequence == null) {
                            SequenceNumber sequenceNumber = new SequenceNumber();
                            sequenceNumber.setSequenceNo(sequenceNo);
                            databaseManager.insert(sequenceNumber);
                            return sequenceNo;
                        } else {
                            throw new SegException("生成序列号失败，重复生成");
                        }
                    }
                } finally {
                    // 生成完成后，从本地缓存中移除
                    localCache.remove(sequenceNo);
                }
            } else {
                throw new SegException("生成序列号失败，重复生成");
            }
        } catch (Exception e) {
            log.error("生成序列号失败", e);
            throw new SegException("生成序列号失败，异常信息:" + e);
        }
    }

    @PostConstruct
    private void initializeCleanupTask() {
        // 定期清理过期缓存项，这里设置为每分钟执行一次
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredCache, 0, 1, TimeUnit.MINUTES);
    }

    private void cleanupExpiredCache() {
        long currentTimestamp = System.currentTimeMillis();
        localCache.entrySet().removeIf(entry -> currentTimestamp - entry.getKey().hashCode() > TimeUnit.MINUTES.toMillis(1));
    }

    private String generateSequenceNo() {
        // 使用 hashCode 来作为缓存键，便于后续过期清理
        return "SN" + UUID.randomUUID();
    }
}
