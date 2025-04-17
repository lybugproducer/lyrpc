package com.lyrpc.demo.api;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.demo.entity.User;

/**
 * 用户 RPC 接口
 *
 * @author lybugproducer
 * @since 2025/1/26 10:59
 */
public interface UserLyrpc extends Lyrpc {
    /**
     * 获取用户信息 rpc 接口 provider 和 consumer 都需要依赖此接口
     * @param id 用户 id
     * @return User 用户对象
     */
    User getUserById(int id);
}
