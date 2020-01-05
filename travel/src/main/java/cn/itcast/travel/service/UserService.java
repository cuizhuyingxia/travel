package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

/**
 * 用户
 */
public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 激活用户
     * @param code
     * @return
     */
    boolean active(String code);

    /**
     * 登录
     * @return
     * @param user
     */
    User login(User user);
}
