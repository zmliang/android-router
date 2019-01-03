package com.general.code.router;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.general.api.RConfig;
import com.general.router.AppRouterStrategyRegular;
import com.general.router.AppRouterStrategySimple;
import com.general.router.RouterConfiguration;
import com.general.router.RouterMapping;
import com.general.router.RouterStrategy;


/**
 * Author: zml
 * Date  : 2019/1/3 - 15:44
 **/
@RConfig(mappingName = "AppRouterMapping")
public class AppRouterConfiguration implements RouterConfiguration {

    @NonNull
    @Override
    public RouterStrategy[] configRouterStrategies() {
        return new RouterStrategy[]{
                new AppRouterStrategySimple(),
                new AppRouterStrategyRegular()
        };
    }

    @Nullable
    @Override
    public RouterMapping[] configRouterMappings() {
        return new RouterMapping[]{
                new AppRouterMapping()
        };
    }
}
