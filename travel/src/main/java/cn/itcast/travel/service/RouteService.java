package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * 旅游线路
 */
public interface RouteService {

    /**
     * 根据类别分页查询旅游线路
     * @param cid
     * @param currentPage
     * @param rows
     * @param rname
     * @return
     */
    PageBean<Route> pageQuery(int cid, int currentPage, int rows, String rname);

    /**
     * 根据旅游线路id查询旅游线路的详细信息
     * @param rid
     * @return
     */
    Route findRoute(String rid);
}
