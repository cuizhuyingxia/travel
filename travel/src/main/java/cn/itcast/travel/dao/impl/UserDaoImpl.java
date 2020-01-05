package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    // 创建JdbcTemplate对象
    // 需要传入数据库连接池对象作为参数，然后JdbcTemplate会自动去获取数据库连接对象，也会自动帮我们释放资源
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_user where username = ?";
            // 2. 查询
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }

        return user;
    }

    /**
     * 保存用户信息
     * @param user
     */
    @Override
    public void save(User user) {
        try {
            // 1. 定义sql
            String sql = "insert into tab_user(username, password, name, birthday, sex, telephone, email, status, code) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // 2. 添加
            template.update(sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getBirthday(),
                    user.getSex(),
                    user.getTelephone(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getCode()
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据激活码查询用户信息
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_user where code = ?";
            // 2. 查询
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 修改用户的激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        // 1. 定义sql
        String sql = "update tab_user set status = 'Y' where uid = ?";
        // 2. 修改
        template.update(sql, user.getUid());
    }

    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_user where username = ? and password = ?";
            // 2. 查询
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }


}
