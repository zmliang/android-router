package com.general.router;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Author: zml
 * Date  : 2019/1/3 - 15:49
 **/
public class AppRouterStrategySimple extends RouterAbstractStrategy {

    private HashMap<String,RouterTarget> mapping;

    @Override
    public void onRouterMappings(RouterMapping[] rapidRouterMappings) {
        HashMap<String, RouterTarget> result = new HashMap<>();
        for (RouterMapping mapping : rapidRouterMappings) {
            mapping.calcSimpleRouterMapper(result);
        }
        mapping = result;
    }

    @Nullable
    @Override
    protected RouterTarget findRouterTargetInternal(@NonNull Uri uri) {
        if (null == mapping) {
            return null;
        }
        return mapping.get(uri.getScheme() + "://" + uri.getHost());
    }

    @Override
    public String parseParamFromUri(@NonNull Uri uri, @NonNull String paramKey) {
        return uri.getQueryParameter(paramKey);
    }
}
