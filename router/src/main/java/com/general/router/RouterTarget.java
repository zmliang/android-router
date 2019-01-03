package com.general.router;

import java.util.HashMap;

/**
 * Author: zml
 * Date  : 2019/1/3 - 14:08
 **/
public class RouterTarget {
    private RouterStrategy routerStrategy;
    private Class targetClass;
    private HashMap<String,Class> params;

    public RouterTarget(Class targetClass,HashMap<String,Class> params){
        this.targetClass = targetClass;
        this.params = params;
    }

    public RouterStrategy getRouterStrategy() {
        return routerStrategy;
    }

    public void setRouterStrategy(RouterStrategy routerStrategy) {
        this.routerStrategy = routerStrategy;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public HashMap<String, Class> getParams() {
        return params;
    }
}
