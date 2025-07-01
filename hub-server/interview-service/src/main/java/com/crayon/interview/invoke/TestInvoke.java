package com.crayon.interview.invoke;

/**
 * @author Mengdl
 * @date 2025/06/30
 */
public class TestInvoke {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        JdkHandler jdkHandler = new JdkHandler(userService);
        UserService proxy = (UserService) jdkHandler.getProxy();
        proxy.add();

        System.out.println("---------------------------------------");
        CglibProxy cglibProxy = new CglibProxy();
        UserService cglibProxyProxy = (UserServiceImpl) cglibProxy.getProxy(UserServiceImpl.class);
        cglibProxyProxy.add();
    }

}
