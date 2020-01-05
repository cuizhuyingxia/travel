package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    // 创建Service对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 验证码校验
        // 获取用户输入的验证码
        String check = request.getParameter("check");
        // 从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");    // 删除session中的验证码，确保验证码只能一次性使用
        // 校验
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            // 验证码错误
            ResultInfo resultInfo = new ResultInfo();
            // 注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            // 设置响应数据的mime类型为json，字符集为utf-8
            response.setContentType("application/json;charset=utf-8");
            // 将响应数据转换为（序列化）JSON字符串，并将JSON字符串填充到response输出流中，响应给客户端
            /*ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), resultInfo);*/
            writeValue(response, resultInfo);
            return ;    // 如果校验失败，则直接返回，不再执行下面的代码
        }

        // 1. 获取数据
        Map<String, String[]> map = request.getParameterMap();

        // 2. 封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);      // map集合的键值对信息封装到user对象中
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3. 调用Service完成注册
        UserService service = new UserServiceImpl();
        boolean flag = service.register(user);

        // 4. 响应结果
        // ResultInfo对象用于封装响应的数据
        ResultInfo resultInfo = new ResultInfo();
        if (flag) {
            // 注册成功
            resultInfo.setFlag(true);
        } else {
            // 注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败");
        }

        // 设置响应数据的mime类型为json，字符集为utf-8
        response.setContentType("application/json;charset=utf-8");

        // 将响应数据转换为（序列化）JSON字符串，并将JSON字符串填充到response输出流中，响应给客户端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), resultInfo);
    }

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取激活码
        String code = request.getParameter("code");

        // 2. 调用Service完成激活
        if (code != null) {
            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            String msg = null;
            if (flag) {
                // 激活成功
                msg = "激活成功，请<a href='/travel/login.html'>登录</a>";
            } else {
                // 激活失败
                msg = "激活失败，请联系管理员";
            }

            response.getWriter().write(msg);
        }
    }


    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 验证码校验
        // 获取用户输入的验证码
        String check = request.getParameter("check");
        // 从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");    // 删除session中的验证码，确保验证码只能一次性使用
        // 校验
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            // 验证码错误
            ResultInfo resultInfo = new ResultInfo();
            // 注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            // 设置响应数据的mime类型为json，字符集为utf-8
            response.setContentType("application/json;charset=utf-8");
            // 将响应数据转换为（序列化）JSON字符串，并将JSON字符串填充到response输出流中，响应给客户端
            /*ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), resultInfo);*/
            writeValue(response, resultInfo);
            return ;    // 如果校验失败，则直接返回，不再执行下面的代码
        }

        // 1. 获取数据
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 2. 封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3. 调用Service查询
        UserService service = new UserServiceImpl();
        User user1 = service.login(user);

        ResultInfo resultInfo = new ResultInfo();

        // 4. 判断用户名或密码是否错误
        if (user1 == null) {
            // 用户名或密码错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误");
        }

        // 5. 判断用户是否激活
        if (user1 != null && !"Y".equals(user1.getStatus())) {
            // 用户尚未激活
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("您尚未激活，请激活");
        }

        // 6. 判断用户是否登录成功
        if (user1 != null && "Y".equals(user1.getStatus())) {
            // 登录成功
            resultInfo.setFlag(true);
            // 将用户信息存储到session中
            HttpSession session1 = request.getSession();
            session1.setAttribute("user", user1);
        }

        // 7. 响应数据
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), resultInfo);
    }

    /**
     * 查询用户信息功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 从session中获取用户信息
        User user = (User) request.getSession().getAttribute("user");

        // 2. 将user对象响应给head.html
        response.setContentType("application/json;charset=utf-8");
        /*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), user);*/
        writeValue(response, user);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 销毁session
        request.getSession().invalidate();

        // 2. 跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
