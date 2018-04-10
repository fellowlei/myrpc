package com.mark.action;

import com.alibaba.fastjson.JSONArray;
import com.mark.service.util.KryoTool;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@WebServlet(name = "call",urlPatterns = "/call")
public class CallAction extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String pass = request.getParameter("p");
            if (null == pass || "".equals(pass.trim())) {
                return;
            }
            if (!"123456".equals(pass)) {
                return;
            }

            String content = "";
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            String bean = request.getParameter("bean");
            String methodParam = request.getParameter("method");
            String argsParam = request.getParameter("args");

            Object obj = ctx.getBean(bean);
            Method[] methods = obj.getClass().getMethods();

            for (Method method1 : methods) {
                if (method1.getName().equalsIgnoreCase(methodParam)) {
//                    Class<?> clazz = Class.forName("[Ljava.lang.Object;");
                    Object[] argObjs = (Object[]) KryoTool.decode(argsParam, Object[].class);
                    Object invoke = method1.invoke(obj, argObjs);
                    JSONArray array = new JSONArray();
                    array.add(invoke.getClass().getName());
                    array.add(KryoTool.encode(invoke));
                    content = array.toJSONString();
                    break;
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(content);
            writer.flush();
        }catch (Exception e){
            ExceptionUtils.getStackTrace(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
