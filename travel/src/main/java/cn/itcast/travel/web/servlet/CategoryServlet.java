package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    // 创建Service对象
    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询所有旅游分类信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 调用Service查询所有旅游分类信息
        List<Category> list = service.findAll();

        // 2. 将list集合转换（序列化）为json字符串，并返回给客户端
        writeValue(response, list);
    }


}
