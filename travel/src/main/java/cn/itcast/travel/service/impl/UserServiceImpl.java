package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    // 创建UserDao对象
    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        // 1. 根据用户名查询用户信息
        User user1 = userDao.findByUsername(user.getUsername());

        // 2. 判断用户是否存在
        if (user1 != null) {
            // 用户名存在，注册失败
            return false;
        } else {
            // 设置激活码
            user.setCode(UuidUtil.getUuid());
            // 设置激活状态为false
            user.setStatus("N");
            // 设置邮件正文
            String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
            // 发送邮件
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            // 用户名不存在，注册成功，保存用户信息
            userDao.save(user);
            return true;
        }
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        // 1. 根据激活码查询用户
        User user = userDao.findByCode(code);
        if (user != null) {
            // 2. 如果用户存在，则激活用户
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 登录
     * @return
     * @param user
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
