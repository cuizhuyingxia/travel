package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routrImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 根据类别分页查询旅游线路
     * @param cid
     * @param currentPage
     * @param rows
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int rows, String rname) {
        // 1. 创建PageBean对象
        PageBean<Route> routePageBean = new PageBean<>();

        // 2. 调用Dao查询总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        routePageBean.setTotalCount(totalCount);

        // 3. 计算总页码数
        int totalPage = totalCount % rows == 0 ? totalCount / rows : (totalCount / rows) + 1;
        routePageBean.setTotalPage(totalPage);

        // 4. 处理当前页
        /*
        因为用户可能会在第一页上面点上一页，那么当前页就会小于1，这不是我们想要的结果，所以我们要判断如果当前页小于1，则将当前页置为1
         */
        if (currentPage < 1) {
            currentPage = 1;
        }
        /*
        因为用户可能会在最后一页上面点下一页，那么当前页就会大于总页数，这不是我们想要的，所以我们要判断如果当前页大于总页数，则将总页数的值赋给当前页
         */
        /*
        不过还有一种情况，如果总页数为0，就是说没有查询到数据，当用户在最后一页上面点下一页时，当前页会大于总页数，
        但是我们不能直接向上面一样将总页数的值赋给当前页，因为现在总页数为0，如果把0赋给当前页，那么在数据库中查询时就会报错。
        因为在查询前会计算起始记录的索引：(当前页-1) * rows     而(0-1) * rows的结果为负数，数据库中的索引是没有负数的，所以就会报错
        所以我们应该先判断，总页数是否为0，如果不为0，则再继续下面的判断；
        如果总页数为0，说明就没有查询到数据，那么就不做任何处理，即使用户一直点下一页也没关系，因为本来就查不到数据，即使将当前页点到10000，也还是没有数据，页面上只会显示空白
         */
        if (totalPage != 0)
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        // 5. 设置当前页和rows属性
        routePageBean.setCurrentPage(currentPage);
        routePageBean.setRows(rows);

        // 6. 计算数每页数据的起始索引      (当前页-1) * rows
        int start = (currentPage - 1) * rows;

        // 7. 调用Dao查询每页显示的数据
        List<Route> routes = routeDao.findByPage(cid, start, rows, rname);
        routePageBean.setList(routes);

        // 8. 返回PageBean对象
        return routePageBean;
    }


    /**
     * 根据旅游线路id查询旅游线路的详细信息
     * @param rid
     * @return
     */
    @Override
    public Route findRoute(String rid) {
        // 1. 根据rid查询route对象
        Route route = routeDao.findRoute(Integer.parseInt(rid));

        // 2. 根据rid查询图片集合信息
        List<RouteImg> imgs = routrImgDao.findImgs(Integer.parseInt(rid));
        route.setRouteImgList(imgs);

        // 3. 根据sid查询商家信息
        Seller seller = sellerDao.findSeller(route.getSid());
        route.setSeller(seller);

        // 4. 查询该旅游线路的收藏次数
        int favoriteCount = favoriteDao.findFavoriteCount(Integer.parseInt(rid));
        route.setCount(favoriteCount);

        return route;
    }
}
