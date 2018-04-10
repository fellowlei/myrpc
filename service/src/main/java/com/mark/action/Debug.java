package com.mark.action;

import com.mark.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Debug {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        HelloService helloService = (HelloService) applicationContext.getBean("helloService");
        String result = helloService.hello("mark");
        System.out.println(result);
    }
}
