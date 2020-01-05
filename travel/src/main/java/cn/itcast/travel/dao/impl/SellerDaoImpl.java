package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据商家id查询商家信息
     * @param sid
     * @return
     */
    @Override
    public Seller findSeller(int sid) {
        Seller seller = null;

        try {
            // 1. 定义sql
            String sql = "select * from tab_seller where sid = ?";
            // 2. 查询
            seller = template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return seller;
    }
}
