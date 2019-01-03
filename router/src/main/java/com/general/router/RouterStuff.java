package com.general.router;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.general.router.exception.RouterIllegalException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: zml
 * Date  : 2019/1/3 - 13:30
 **/
public class RouterStuff {
    private WeakReference<Context> contextRef;
    private Intent intent;
    private String uri;

    private RouterErrorCallBack error;
    private RouterTargetNotFoundCallBack targetNotFound;
    private RouterGoBeforeCallBack goBefore;
    private RouterGoAroundCallBack goAround;
    private RouterGoAfterCallBack goAfter;

    private List<Class<? extends RouterStrategy>> supportStrategies;

    public RouterStuff intent(Intent intent) {
        this.intent = intent;
        return this;
    }

    @Nullable
    public Context context() {
        return null == contextRef ? null : contextRef.get();
    }

    public void setContext(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public RouterStuff uri(String uriStr) {
        this.uri = uriStr;
        return this;
    }

    public Intent intent() {
        return intent;
    }

    public String uriAsString() {
        return uri;
    }

    public RouterErrorCallBack error() {
        return error;
    }

    public RouterStuff error(RouterErrorCallBack errorListener) {
        this.error = errorListener;
        return this;
    }

    public RouterTargetNotFoundCallBack targetNotFound() {
        return targetNotFound;
    }

    public RouterStuff targetNotFound(RouterTargetNotFoundCallBack targetNotFoundListener) {
        this.targetNotFound = targetNotFoundListener;
        return this;
    }

    public RouterGoBeforeCallBack goBefore() {
        return goBefore;
    }

    public RouterStuff goBefore(RouterGoBeforeCallBack goBeforeListener) {
        this.goBefore = goBeforeListener;
        return this;
    }

    public RouterGoAroundCallBack goAround() {
        return goAround;
    }

    public RouterStuff goAround(RouterGoAroundCallBack goAroundListener) {
        this.goAround = goAroundListener;
        return this;
    }

    public RouterGoAfterCallBack goAfter() {
        return goAfter;
    }

    public RouterStuff goAfter(RouterGoAfterCallBack goAfterListener) {
        this.goAfter = goAfterListener;
        return this;
    }

    @SafeVarargs
    public final RouterStuff strategies(Class<? extends RouterStrategy>... strategies) {
        if (null == supportStrategies) {
            supportStrategies = new ArrayList<>();
        }
        supportStrategies.addAll(Arrays.asList(strategies));
        return this;
    }

    @Nullable
    public List<Class<? extends RouterStrategy>> strategies() {
        return supportStrategies;
    }

    public boolean go() {
        if (null == contextRef) {
            throw new RouterIllegalException("Context can not be null!");
        }
        if (null == uri) {
            throw new RouterIllegalException("Uri can not be null!");
        }
        return Router.to(this);
    }

    @Override
    public String toString() {
        return "RouterStuff{" +
                "contextRef=" + contextRef +
                ", intent=" + intent +
                ", uri='" + uri + '\'' +
                ", error=" + error +
                ", targetNotFound=" + targetNotFound +
                ", goBefore=" + goBefore +
                ", goAround=" + goAround +
                ", goAfter=" + goAfter +
                '}';
    }

}
