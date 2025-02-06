package com.taihuafufc.lybugproducer.impl;

import com.taihuafufc.lybugproducer.User;
import com.taihuafufc.lybugproducer.UserLyrpc;

/**
 * TODO rpc 接口实现类
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
    public String getUserById(int id) {
        return PREFIX + id;
    }
}
