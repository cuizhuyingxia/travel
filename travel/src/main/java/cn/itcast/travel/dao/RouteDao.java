package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 旅游线路
 */
public interface RouteDao {

    /**
     * 根据旅游分类id查询总记录数
     * @param cid
     * @param rname
     * @return
     */
    int findTotalCount(int cid, String rname);

    /**
     * 根据旅游分类id查询当前页的数据集合
     * @param cid
     * @param start
     * @param rows
     * @param rname
     * @return
     */
    List<Route> findByPage(int cid, int start, int rows, String rname);

    /**
     * 根据旅游线路id查询旅游线路的详细信息
     * @param rid
     * @return
     */
    Route findRoute(int rid);
}
