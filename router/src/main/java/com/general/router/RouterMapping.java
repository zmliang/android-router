package com.general.router;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Author: zml
 * Date  : 2019/1/3 - 14:08
 **/
public abstract class RouterMapping {
    protected HashMap<String,RouterTarget> getEnsureMap(HashMap<String,HashMap<String,RouterTarget>> routerMapper,String key){
        HashMap<String,RouterTarget> map = routerMapper.get(key);
        if (map == null){
            map = new HashMap<>();
            routerMapper.put(key,map);
        }
        return map;
    }

    public abstract HashMap<String, RouterTarget> calcSimpleRouterMapper(@NonNull HashMap<String, RouterTarget> routerMapper);

    public abstract HashMap<String, RouterTarget> calcRegRouterMapper(@NonNull HashMap<String, RouterTarget> routerMapper);

}
