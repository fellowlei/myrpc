package com.mark.rpc.registry;

/**
 * Created by lulei on 2018/5/28.
 */
public interface Constant {
    int ZK_SESSION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}
