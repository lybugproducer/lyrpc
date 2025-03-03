package com.lyrpc.demo.provider.impl;

import com.lyrpc.demo.entity.User;
import com.lyrpc.demo.api.UserLyrpc;

/**
 * 用户 RPC 接口 服务提供方实现类
 *
 * @author lybugproducer
 * @since 2025/1/26 11:05
 */
public class UserLyrpcImpl implements UserLyrpc {

    private static final String PREFIX = "user";

    /**
     * 获取用户信息 rpc 接口实现
     * @param id 用户 id
     * @return {@link User} 用户信息
     */
    @Override
    public User getUserById(int id) {
        return new User(id, PREFIX + id);
    }
}
