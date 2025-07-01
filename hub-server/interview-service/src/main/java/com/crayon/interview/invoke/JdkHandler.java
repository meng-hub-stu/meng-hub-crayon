package com.crayon.interview.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Mengdl
 * @date 2025/06/30
 */
public class JdkHandler implements InvocationHandler {

    private Object target;

    public JdkHandler(Object target) {
        super();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke ---- start");
        Object result = method.invoke(target, args);
        System.out.println("invoke ---- end");
        return result;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

}
