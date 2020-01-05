package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao{
    // 创建JdbcTemplate对象
    // 需要传入数据库连接池对象作为参数，然后JdbcTemplate会自动去获取数据库连接对象，也会自动帮我们释放资源
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询所有旅游分类信息
     * @return
     */
    @Override
    public List<Category> findAll() {
        List<Category> list = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_category";
            // 2. 查询
            list = template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return list;
    }
}
