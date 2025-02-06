package com.taihuafufc.lybugproducer;

/**
 * TODO rpc 接口定义
 *
 * @author lybugproducer
 * @since 2025/1/26 10:59
 */
public interface UserLyrpc {
    /**
     * 获取用户信息 rpc 接口 provider 和 consumer 都需要依赖此接口
     * @param id 用户 id
     * @return String 用户对象
     */
    String getUserById(int id);
}
