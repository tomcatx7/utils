package commonInterface;

public class HelloServiceImp implements HelloService {
    public String sayHello(String arg) {
        return "hello"+arg;
    }
}
