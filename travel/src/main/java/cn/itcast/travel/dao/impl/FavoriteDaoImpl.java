package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据uid和rid查询收藏信息
     * @param uid
     * @param rid
     * @return
     */
    @Override
    public Favorite findByRidAndUid(int uid, int rid) {
        Favorite favorite = null;

        try {
            // 1. 定义sql
            String sql = "select * from tab_favorite where uid = ? and rid = ?";
            // 2. 查询
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return favorite;
    }

    /**
     * 查询该旅游线路的收藏次数
     * @param rid
     * @return
     */
    @Override
    public int findFavoriteCount(int rid) {
        int count = 0;

        try {
            // 1. 定义Sql
            String sql = "select count(*) from tab_favorite where rid = ?";
            // 2. 查询
            count = template.queryForObject(sql, Integer.class, rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * 添加收藏
     * @param uid
     * @param rid
     */
    @Override
    public void addFavorite(int uid, int rid) {
        // 1. 定义sql
        String sql = "insert into tab_favorite values(?, ?, ?)";
        // 2. 添加
        template.update(sql, rid, new Date(), uid);
    }
}
