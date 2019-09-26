package com.peng.daliy.javabase;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements InvocationHandler {
    //需要代理的目标对象
    private Object target ;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JdkProxy invoke begin");
        Object result = method.invoke(target, args);
        System.out.println("JdkProxy invoke end");
        return result;
    }

    //定义获取代理对象方法
    public Object getJDKProxy(Object targetObject){
        //为目标对象target赋值
        this.target = targetObject;
        //JDK动态代理只能针对实现了接口的类进行代理，newProxyInstance 函数所需参数就可看出
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }
}
