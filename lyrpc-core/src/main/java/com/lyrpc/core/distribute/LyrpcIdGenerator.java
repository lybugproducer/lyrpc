package com.lyrpc.core.distribute;

/**
 * 分布式 ID 生成器
 *
 * @author lybugproducer
 * @since 2025/3/3 09:19
 */
public class LyrpcIdGenerator {

    // 客户端 ID 属于服务消费者的全局唯一标识
    private long clientId;

    // 下面通过 雪花算法 生成分布式 ID
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private long clockMovedCount = 0L;

    /*
     * 分布式 ID 的组成如下
     * long 类型 64 位
     * 1 位符号位 默认为 0 代表正数
     * 4 位时钟回拨次数 最大值 15 允许 15 次时钟回拨
     * 43 位时间戳 能够表示到 2109-05-15 15:35:11
     * 8 位客户端 ID 最大值 255 允许 15 个数据中心 每个数据中心允许 15 个机器
     * 8 位序列号 最大值 255 每个消费方可以抗住二十五万的 QPS 并发量
     */

    // setter for client ID
    public void setClientId(long dataCenterId, long workerId) {
        if (dataCenterId > 15 || dataCenterId < 0) {
            throw new IllegalArgumentException("Illegal Data center Id");
        }
        if (workerId > 15 || workerId < 0) {
            throw new IllegalArgumentException("Illegal Worker Id");
        }
        this.clientId = (dataCenterId << 4) | workerId;
    }

    public synchronized long nextId() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            // 发生了时钟回拨
            clockMovedCount ++;
            if (clockMovedCount > 15) {
                throw new RuntimeException("Clock moved backwards.");
            }
            lastTimestamp = timestamp;
            sequence = 0L;
        }

        if (lastTimestamp == timestamp) {
            // 同一时间戳内的序列号自增
            sequence ++;
            if (sequence > 1023) {
                // 序列号溢出 等待下一毫秒
                while (timestamp <= lastTimestamp) {
                    // 不断自旋 等待下一毫秒
                    timestamp = System.currentTimeMillis();
                }
                // 重置序列号
                sequence = 0L;
            }
        } else {
            // 不同时间戳的序列号置为 0
            sequence = 0L;
        }

        // 更新 lastTimestamp
        lastTimestamp = timestamp;

        // 组装分布式 ID
        return (clockMovedCount << 59) | (timestamp << 16) | (clientId << 10) | sequence;
    }

}
