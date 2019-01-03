package com.general.router;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Author: zml
 * Date  : 2019/1/3 - 14:06
 **/
public interface RouterStrategy {
    void onRouterMappings(RouterMapping[] routerMappings);

    @Nullable
    RouterTarget findRouterTarget(@NonNull Uri uri);

    String parseParamFromUri(@NonNull Uri uri,@NonNull String paramKey);
}
