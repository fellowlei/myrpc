package com.mark.rpc.client;

import com.mark.rpc.client.proxy.ObjectProxy;
import com.mark.service.domain.User;
import com.mark.service.UserService;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lulei on 2018/5/28.
 */
public class RpcClient {
    private String serverAddress;
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    public RpcClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        );
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public void stop() {
        threadPoolExecutor.shutdown();
        ConnectManage.getInstance().stop();
    }

    public static void main(String[] args) {
        ConnectManage.getInstance().connectServerNode(new InetSocketAddress("localhost",9999));
        UserService userService = RpcClient.create(UserService.class);
        User mark = userService.query("mark");
        System.out.println(mark);
        mark = userService.query("mark");
        System.out.println(mark);
        mark = userService.query("mark");
        System.out.println(mark);
    }
}
