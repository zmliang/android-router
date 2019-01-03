package com.general.router;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Author: zml
 * Date  : 2019/1/3 - 15:47
 **/
public abstract class RouterAbstractStrategy implements RouterStrategy {

    @Override
    public void onRouterMappings(RouterMapping[] rapidRouterMappings) {
        // ignore
    }

    @Nullable
    @Override
    public final RouterTarget findRouterTarget(@NonNull Uri uri) {
        RouterTarget routerTarget = findRouterTargetInternal(uri);
        if (null != routerTarget) {
            routerTarget.setRouterStrategy(this);
        }
        return routerTarget;
    }

    @Nullable
    protected abstract RouterTarget findRouterTargetInternal(@NonNull Uri uri);
}
