package com.general.router;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Author: zml
 * Date  : 2019/1/3 - 15:25
 **/
public interface RouterConfiguration {

    /**
     * 配置路由策略
     * @return
     */
    @NonNull
    RouterStrategy[] configRouterStrategies();


    /**
     * 配置路由映射
     * @return
     */
    @Nullable
    RouterMapping[] configRouterMappings();
}
