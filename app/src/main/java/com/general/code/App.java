package com.general.code;

import android.app.Application;

import com.general.code.router.AppRouterConfiguration;
import com.general.code.router.AppRouterGlobalListener;
import com.general.router.Router;

/**
 * Author: zml
 * Date  : 2019/1/3 - 16:52
 **/
public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Router.init(new AppRouterConfiguration());
        Router.setOnRapidRouterListener(new AppRouterGlobalListener());
    }
}
