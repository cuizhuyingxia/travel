package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据旅游分类id查询总记录数
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        int totalCount = 0;

        try {
            // 1. 定义sql模板
            String sql = "select count(*) from tab_route where 1 = 1";

            // 2. 拼接sql
            StringBuilder stringBuilder = new StringBuilder(sql);
            // 创建一个集合，用于保存参数的值
            List params = new ArrayList();
            // 判断接收的参数是否有值
            if (cid != 0) {
                stringBuilder.append( " and cid = ? " );
                params.add(cid);
            }
            if (rname != null && rname.length() > 0) {
                stringBuilder.append( " and rname like ? " );
                params.add("%"+rname+"%");
            }
            // 转换为字符串
            sql = stringBuilder.toString();

            // 3. 查询
            totalCount = template.queryForObject(sql, Integer.class, params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return totalCount;
    }


    /**
     * 根据旅游分类id查询当前页的数据集合
     * @param cid
     * @param start
     * @param rows
     * @param rname
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int rows, String rname) {
        List<Route> list = null;

        try {
            // 1. 定义sql模板
            String sql = "select * from tab_route where 1 = 1";

            // 2. 拼接sql
            StringBuilder stringBuilder = new StringBuilder(sql);
            // 创建一个集合，用于保存参数的值
            List params = new ArrayList();
            // 判断接收的参数是否有值
            if (cid != 0) {
                stringBuilder.append( " and cid = ? " );
                params.add(cid);
            }

            if (rname != null && rname.length() > 0) {
                stringBuilder.append( " and rname like ? " );
                params.add("%"+rname+"%");
            }

            // 拼接分页条件
            stringBuilder.append(" limit ? , ? ");
            // 转换为字符串
            sql = stringBuilder.toString();
            // 添加分页条件的参数
            params.add(start);
            params.add(rows);

            // 3. 查询
            list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根据旅游线路id查询旅游线路的详细信息
     * @param rid
     * @return
     */
    @Override
    public Route findRoute(int rid) {
        Route route = null;
        try {
            // 1. 定义sql
            String sql = "select * from tab_route where rid = ?";
            // 2. 查询
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return route;
    }
}
