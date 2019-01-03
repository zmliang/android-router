package com.general.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.general.router.exception.RouterIllegalException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: zml
 * Date  : 2019/1/3 - 13:29
 **/
public class Router {
    private static OnRouterListener onRouterListener;

    public static void setOnRapidRouterListener(OnRouterListener onRapidRouterListener) {
        Router.onRouterListener = onRapidRouterListener;
    }

    private static final String TAG = Router.class.getSimpleName();

    /**
     * Tree<{RapidRouterStrategy class name}, {RapidRouterStrategy}>
     */
    private static LinkedHashMap<String, RouterStrategy> routerStrategyTreeMap;

    public static void init(@NonNull RouterConfiguration rapidRouterConfiguration) {
        config(rapidRouterConfiguration);
    }

    private static void config(RouterConfiguration rapidRouterConfiguration) {
        // Router Strategy configuration
        routerStrategyTreeMap = new LinkedHashMap<>();

        RouterMapping[] rapidRouterMappings = rapidRouterConfiguration.configRouterMappings();
        for (RouterStrategy routerStrategy : rapidRouterConfiguration.configRouterStrategies()) {
            // Router Mapping configuration
            routerStrategy.onRouterMappings(rapidRouterMappings);
            routerStrategyTreeMap.put(routerStrategy.getClass().getCanonicalName(), routerStrategy);
        }

    }


    public static RouterStuff with(@NonNull Context context) {
        RouterStuff routerStuff = new RouterStuff();
        routerStuff.setContext(context);
        return routerStuff;
    }

    protected static boolean to(RouterStuff routerStuff) {
        if (null == routerStrategyTreeMap || routerStrategyTreeMap.isEmpty()) {
            throw new RouterIllegalException("Router is not initialized! Please call Router::init() first.");
        }

        String uriStr = routerStuff.uriAsString();
        try {
            Uri uri = Uri.parse(uriStr);

            RouterTarget routerTarget = findRouterStrategy(routerStuff, uri);
            RouterStrategy routerStrategy;
            if (null == routerTarget || null == (routerStrategy = routerTarget.getRouterStrategy())) {
                RouterTargetNotFoundCallBack targetNotFoundCallback = routerStuff.targetNotFound();
                if (null == targetNotFoundCallback || !targetNotFoundCallback.onRouterTargetNotFound(routerStuff)) {
                    if (null != onRouterListener) {
                        onRouterListener.onRouterTargetNotFound(routerStuff);
                    }
                }
                return false;
            }

            Intent intent = routerStuff.intent();

            if (null == intent) {
                intent = new Intent();
                routerStuff.intent(intent);
            }

            Context context = routerStuff.context();
            if (null == context) {
                return false;
            }
            intent.setComponent(new ComponentName(context, routerTarget.getTargetClass()));
            intent.setData(uri);
            HashMap<String, Class> params = routerTarget.getParams();

            for (String paramName : uri.getQueryParameterNames()) {
                Class tempClass;
                Class paramClass = null == params || null == (tempClass = params.get(paramName)) ? String.class : tempClass;
                putExtraToIntent(intent, paramClass, paramName, routerStrategy.parseParamFromUri(uri, paramName));
            }

            RouterGoBeforeCallBack goBeforeCallback = routerStuff.goBefore();
            if (null == goBeforeCallback || !goBeforeCallback.onRouterGoBefore(routerStuff)) {
                if (null != onRouterListener) {
                    onRouterListener.onRouterGoBefore(routerStuff);
                }
            }

            RouterGoAroundCallBack goAroundCallback = routerStuff.goAround();
            if (null != goAroundCallback) {
                goAroundCallback.onRouterGoAround(routerStuff);
                if (null != onRouterListener) {
                    onRouterListener.onRouterGoAfter(routerStuff);
                }
            } else {
                boolean proceed = true;
                if (null != onRouterListener) {
                    proceed = !onRouterListener.onRouterGoAround(routerStuff);
                }
                if (proceed) {
                    context.startActivity(intent);
                    if (null != onRouterListener) {
                        onRouterListener.onRouterGoAfter(routerStuff);
                    }
                }
            }

            return true;
        } catch (Throwable throwable) {
            Log.e(TAG, "", throwable);
            if (null != onRouterListener) {
                onRouterListener.onRouterError(routerStuff, throwable);
            }
            return false;
        }
    }

    /**
     * 根据策略 查询RouterTarget
     */
    @Nullable
    private static RouterTarget findRouterStrategy(RouterStuff routerStuff, Uri uri) {
        RouterTarget routerTarget = null;
        List<Class<? extends RouterStrategy>> supportStrategies = routerStuff.strategies();
        if (null == supportStrategies || supportStrategies.isEmpty()) {
            for (Map.Entry<String, RouterStrategy> entry : routerStrategyTreeMap.entrySet()) {
                if (null != (routerTarget = entry.getValue().findRouterTarget(uri))) {
                    break;
                }
            }
        } else {
            for (Class<? extends RouterStrategy> routerStrategyClass : supportStrategies) {
                RouterStrategy routerStrategy = routerStrategyTreeMap.get(routerStrategyClass.getCanonicalName());
                if (null != routerStrategy) {
                    if (null != (routerTarget = routerStrategy.findRouterTarget(uri))) {
                        break;
                    }
                }
            }
        }

        return routerTarget;
    }

    private static void putExtraToIntent(Intent intent, Class clazz, String paramName, String value) {
        if (null != value) {
            if (String.class == clazz) {
                intent.putExtra(paramName, value);
            } else if (int.class == clazz || Integer.class == clazz) {
                try {
                    intent.putExtra(paramName, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (long.class == clazz || Long.class == clazz) {
                try {
                    intent.putExtra(paramName, Long.parseLong(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (boolean.class == clazz || Boolean.class == clazz) {
                try {
                    intent.putExtra(paramName, Boolean.parseBoolean(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (float.class == clazz || Float.class == clazz) {
                try {
                    intent.putExtra(paramName, Float.parseFloat(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (double.class == clazz || Double.class == clazz) {
                try {
                    intent.putExtra(paramName, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (short.class == clazz || Short.class == clazz) {
                try {
                    intent.putExtra(paramName, Short.parseShort(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if ((char.class == clazz || Character.class == clazz) && value.length() > 0) {
                try {
                    intent.putExtra(paramName, value.charAt(0));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else if (byte.class == clazz || Byte.class == clazz) {
                try {
                    intent.putExtra(paramName, Byte.parseByte(value));
                } catch (NumberFormatException e) {
                    throw new RouterIllegalException("Expect type of " + paramName + ": " + clazz + ", actual value: " + value);
                }
            } else {
                intent.putExtra(paramName, value);
            }
        }
    }

}
