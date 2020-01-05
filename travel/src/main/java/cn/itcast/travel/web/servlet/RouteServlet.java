package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 旅游线路分页展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取参数
        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        String rowsStr = request.getParameter("rows");
        // 获取rname线路名称
        String rname = request.getParameter("rname");
        // 解决中文乱码问题，tomcat8自动帮我们解决了中文乱码问题，而当前我们使用的是tomcat7，所以需要自己解决中文乱码问题
        /*
        首先我们要知道，浏览器使用的字符集，和本地操作系统有关，如果是windows系统，那么浏览器使用的字符集为GBK
        tomcat使用的字符集为iso-8859-1，而request流对象是由tomcat创建的，所以request流对象的字符集也是iso-8859-1
        所以，当我们使用request流对象的iso-8859-1字符集去获取浏览器的GBK字符集编码的数据时，就会出现中文乱码问题
        解决办法就是，将获取到的数据再转换为utf-8格式的数据
         */
        // 首先使用getBytes方法获取rname的iso-8859-1格式的字节数组，然后再转换为utf-8格式的
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        /*
        因为使用getParameter.js获取?后面的参数的值时，如果参数不存在，那么获取到的值为null，
        而我们在传递传递参数时：javascript:load('+cid+', 1, \''+rname+'\'); 相当于是javascript:load('+cid+', 1, 'null');
        '+rname+'是获取rname的值，然后还要再加上两个\''+rname+'\'，因为rname为字符串类型
        所以需要判断下，如果rname的值为"null"，则将rename的值置为""
         */
        if ("null".equals(rname)) {
            rname = "";
        }

        // 2. 处理参数
        int cid = 0;            // 旅游分类id
        boolean cidStr_Valid = cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr);
        // 如果cidStr是有效的
        if (cidStr_Valid) {
            cid = Integer.parseInt(cidStr);
        }
        /*if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }*/

        int currentPage = 0;    // 当前页码，如果为空，则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int rows = 0;           // 每页显示的条目数，如果为空，则默认显示5条记录
        if (rowsStr != null && rowsStr.length() > 0) {
            rows = Integer.parseInt(rowsStr);
        } else {
            rows = 5;
        }

        // 3. 调用service查询数据
        PageBean<Route> routePageBean = routeService.pageQuery(cid, currentPage, rows, rname);

        // 4. 将查询到的PageBean对象序列化为json字符串，返回给客户端
        writeValue(response, routePageBean);
    }

    /**
     * 根据旅游线路id查询旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接收旅游线路id
        String rid = request.getParameter("rid");

        // 2. 调用Service查询Route对象
        Route route = routeService.findRoute(rid);

        // 3. 将Route序列化为json，然后返回给客户端
        writeValue(response, route);

    }


    /**
     * 查询用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取线路id
        String rid = request.getParameter("rid");

        // 2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            // 用户尚未登录
            return ;                // 直接返回，不再进行下面的操作
        } else {
            // 用户已登录
            // 获取用户id
            uid = user.getUid();
        }

        // 3. 调用FavoriteService查询是否收藏该线路
        boolean flag = favoriteService.isFavorite(uid, rid);

        // 4. 将收藏结果返回给客户端
        writeValue(response, flag);
    }


    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取线路id
        String rid = request.getParameter("rid");

        // 2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            // 用户尚未登录
            return ;                // 直接返回，不再进行下面的操作
        } else {
            // 用户已登录
            // 获取用户id
            uid = user.getUid();
        }

        // 3. 调用Service添加收藏
        favoriteService.addFavorite(uid, rid);
    }

}

