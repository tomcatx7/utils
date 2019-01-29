package rpc;

import commonInterface.CaculateService;
import commonInterface.CaculateServiceImp;
import commonInterface.HelloService;
import commonInterface.HelloServiceImp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


public class SimpleRPC {
    /**
     *
     * @param service 注册的服务实例
     * @param port 服务暴露的端口
     * @throws IOException
     */
    public static void export(final Object service, int port) throws IOException {
        if (port < 0 || port > 65535) throw new IllegalArgumentException();
        if (service == null) throw new NullPointerException();
        final ServerSocket serverSocket = new ServerSocket(port);
        System.out.println(port+":"+"服务已注册。。。");

        for (; ; ) {
            final Socket socket = serverSocket.accept();
            System.out.println("获得连接:"+socket.getRemoteSocketAddress());
            new Thread(new Runnable() {
                public void run() {
                    ObjectInputStream input = null;
                    ObjectOutputStream out = null;
                    try {
                        input = new ObjectInputStream(socket.getInputStream());
                        out = new ObjectOutputStream(socket.getOutputStream());
                        try {
                            String methodName = input.readUTF();
                            System.out.println("服务端调用方法"+methodName);
                            Class<?>[] paramsType = (Class<?>[]) input.readObject();
                            System.out.println("服务端接收方法参数类型"+ Arrays.toString(paramsType));
                            Object[] argums = (Object[]) input.readObject();
                            System.out.println("服务端接收方法参数值"+ Arrays.toString(argums));
                            //通过反射，调用本地服务方法（需要三个参数：方法名，方法参数类型，参数值）
                            Method method = (Method) service.getClass().getMethod(methodName, paramsType);
                            Object result = method.invoke(service, argums);
                            System.out.println("服务端执行方法，获得返回值"+result);
                            out.writeObject(result);

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            out.writeChars(e.toString());
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            out.writeChars(e.toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            out.writeChars(e.toString());
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            out.writeChars(e.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (input != null) input.close();
                            if (out != null) out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     *
     * @param interfaceClazz 代理对象实现的接口
     * @param host 远程端主机地址
     * @param port RPC服务暴露的端口
     * @param <T> 返回的代理对象
     * @return
     */
    public static <T> T refer(Class<T> interfaceClazz, final String host, final int port){
        if (interfaceClazz == null)throw new NullPointerException();
        if (!interfaceClazz.isInterface())throw new IllegalArgumentException();
        if (port < 0 || port > 65535) throw new IllegalArgumentException();

        T proxy = (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class[]{interfaceClazz}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();

                Socket socket = new Socket(host,port);
                Object result = null;
                ObjectInputStream input = null;
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeUTF(methodName);
                    System.out.println("代理对象调用方法"+methodName);
                    out.writeObject(parameterTypes);
                    System.out.println("代理对象传入方法参数类型"+parameterTypes);
                    out.writeObject(args);
                    System.out.println("代理对象传入方法参数值"+args);

                    input = new ObjectInputStream(socket.getInputStream());
                    result = input.readObject();
                    System.out.println("获得返回值"+result);
                    if (result instanceof Throwable){
                        throw (Throwable)result;
                    }
                    return result;
                }finally {
                    if (input !=null)input.close();
                    if (out !=null)out.close();
                    socket.close();
                }
            }
        });
        return proxy;

    }


    public static void main(String[] args) throws IOException {
        HelloService helloService = new HelloServiceImp();
        //注册服务，将服务端口暴露出来
        SimpleRPC.export(helloService,9999);
    }
}
