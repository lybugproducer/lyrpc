package com.lyrpc.core.configreader;

/**
 * 解析配置文件的接口
 *
 * @author lybugproducer
 * @since 2025/3/4 10:30
 */
public interface LyrpcConfigReader {

    /**
     * 解析配置文件
     *
     * @param configPath 配置文件路径
     */
    void readConfig(String configPath) throws Exception;
}
