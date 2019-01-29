package rpc;

import commonInterface.HelloService;

public class RpcClient {
    public static void main(String[] args) {
        //引用服务
        HelloService helloService = SimpleRPC.refer(HelloService.class, "127.0.0.1", 9999);
        System.out.println(helloService.sayHello("zxj"));

    }
}
