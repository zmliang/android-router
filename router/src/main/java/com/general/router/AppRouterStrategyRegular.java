package com.general.router;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: zml
 * Date  : 2019/1/3 - 15:52
 **/
public class AppRouterStrategyRegular extends RouterAbstractStrategy {
    /**
     * HashMap<{uri reg}, {router target}>
     */
    private HashMap<String, RouterTarget> mapping;

    @Override
    public void onRouterMappings(RouterMapping[] rapidRouterMappings) {
        HashMap<String, RouterTarget> result = new HashMap<>();
        for (RouterMapping mapping : rapidRouterMappings) {
            mapping.calcRegRouterMapper(result);
        }
        mapping = result;
    }

    @Nullable
    @Override
    protected RouterTarget findRouterTargetInternal(@NonNull Uri uri) {
        if (null == mapping) {
            return null;
        }
        for (Map.Entry<String, RouterTarget> entry : mapping.entrySet()) {
            if (uri.toString().matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String parseParamFromUri(@NonNull Uri uri, @NonNull String paramKey) {
        return uri.getQueryParameter(paramKey);
    }
}
