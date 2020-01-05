package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据旅游线路id查询图片信息
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> findImgs(int rid) {
        List<RouteImg> imgs = null;

        try {
            // 1. 定义sql
            String sql = "select * from tab_route_img where rid = ?";
            // 2. 查询
            imgs = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return imgs;
    }
}
