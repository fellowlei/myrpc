package com.mark.action;

import com.mark.service.HelloService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "demo",urlPatterns = "/demo")
public class DemoAction extends HttpServlet{
    private HelloService helloService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        helloService = (HelloService) applicationContext.getBean("helloService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DemoAction.doGet");
        String result = helloService.hello("mark");
        System.out.println(result);
    }




}
