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

}
