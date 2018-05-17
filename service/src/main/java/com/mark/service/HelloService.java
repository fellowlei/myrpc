package com.mark.service;

public class HelloService {
    private DemoService demoService;
    public String hello(String name){
        return demoService.hello(name);
    }

    public DemoService getDemoService() {
        return demoService;
    }

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

    public static void main(String[] args) {
        Integer num = 100;
        System.out.println(Integer.toBinaryString(num));
        System.out.println(Integer.toBinaryString(num >> 2));
        System.out.println(num >> 2);
        System.out.println(Integer.toBinaryString(num << 2));
        System.out.println(num << 2);
    }
}
