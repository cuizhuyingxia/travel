package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        完成方法分发
         */

        // 1. 获取请求路径
        String uri = req.getRequestURI();
        //System.out.println("请求url：" +uri);          // /travel/user/login

        // 2. 获取请求路径中的方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //System.out.println(methodName);             // login


        try {
            // 3. 获取方法对象
            // 因为用户在请求一个servlet时，这个servlet首先会调用service方法，所以在service方法中this对象就是调用service方法的servlet对象
            //System.out.println(this);               // cn.itcast.travel.web.servlet.UserServlet@4c201315
            Method methodObj = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            // 4. 调用方法
            methodObj.invoke(this, req, resp);
            // 暴力反射
            /*// 忽略掉方法修饰符，获取所有类型的方法，包括受保护的和私有的
            Method methodObj = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            // 使用暴力反射，调用方法，可以调用所有类型的方法，包括受保护的和私有的
            methodObj.setAccessible(true);
            methodObj.invoke(this, req, resp);*/
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // 封装方法：将对象序列化为json字符串
    public String writeValueAsString(Object obj) {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    // 封装方法：将对象序列化为json字符串，并返回给客户端
    public void writeValue(HttpServletResponse response, Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        try {
            objectMapper.writeValue(response.getOutputStream(), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
