package com.general.code.router;

import android.util.Log;

import com.general.router.OnRouterListener;
import com.general.router.RouterStuff;

/**
 * Author: zml
 * Date  : 2019/1/3 - 15:59
 **/
public class AppRouterGlobalListener implements OnRouterListener {
    private static final String TAG = AppRouterGlobalListener.class.getSimpleName();

    @Override
    public void onRouterError(RouterStuff routerStuff, Throwable throwable) {
        Log.i(TAG, "[onRouterError]routerStuff: " + routerStuff + ", errorMessage" + throwable.getMessage());
    }

    @Override
    public void onRouterGoAfter(RouterStuff routerStuff) {
        Log.i(TAG, "[onRouterGoAfter]routerStuff: " + routerStuff);
    }

    @Override
    public boolean onRouterGoAround(RouterStuff routerStuff) {
        Log.i(TAG, "[onRouterGoAround]routerStuff: " + routerStuff);
        return false;
    }

    @Override
    public void onRouterGoBefore(RouterStuff routerStuff) {
        Log.i(TAG, "[onRouterGoBefore]routerStuff: " + routerStuff);
    }

    @Override
    public void onRouterTargetNotFound(RouterStuff routerStuff) {
        Log.i(TAG, "[onRouterTargetNotFound]routerStuff: " + routerStuff);
    }
}
