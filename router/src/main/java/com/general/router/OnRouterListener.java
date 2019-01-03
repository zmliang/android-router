package com.general.router;

/**
 * Author: zml
 * Date  : 2019/1/3 - 13:30
 **/
public interface OnRouterListener {

    void onRouterError(RouterStuff routerStuff,Throwable throwable);

    void onRouterGoAfter(RouterStuff routerStuff);

    boolean onRouterGoAround(RouterStuff routerStuff);

    void onRouterGoBefore(RouterStuff routerStuff);

    void onRouterTargetNotFound(RouterStuff routerStuff);

}
