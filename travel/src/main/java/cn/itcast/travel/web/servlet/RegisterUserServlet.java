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

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), resultInfo);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
