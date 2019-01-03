package com.general.router;

/**
 * Author: zml
 * Date  : 2019/1/3 - 13:35
 **/
public interface RouterErrorCallBack {
    boolean onRouterError(RouterStuff stuff,Throwable throwable);
}
