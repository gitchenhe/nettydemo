package com.chenhe.entity;

import java.lang.reflect.Method;

/**
 * @author chenhe
 * @date 2019-05-10 17:10
 * @desc
 */
public class ServiceInvokeParam {
    /**
     * 调用的类
     */
    private Class serviceBean;
    /**
     * 调用的方法入口
     */
    private Method method;
    /**
     * 方法的参数
     */
    private Object[] args;

    /**
     * 服务返回结果
     */
    private Object result;

    public Class getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Class serviceBean) {
        this.serviceBean = serviceBean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
