package com.aptdc;

import java.util.LinkedHashMap;
import java.util.Map;

public class AptDC {

    private static final Map<Class<?>, AbstractLogInjector<Object>> INJECTORS = new LinkedHashMap<Class<?>, AbstractLogInjector<Object>>();

    /**
     * 外部调用api
     * @param object 需要注册的类
     * @param methodName 注册的方法
     */
    public static void log(Object object,String methodName){
         AbstractLogInjector<Object> logInInjector = findInjector(object);
         logInInjector.inject(object,methodName);
    }

    private static AbstractLogInjector<Object> findInjector(Object activity) {
        Class<?> clazz = activity.getClass();
        AbstractLogInjector<Object> injector = INJECTORS.get(clazz);
        if (injector == null) {
            try {
                Class injectorClazz = Class.forName(clazz.getName() + "$$YLL");
                injector = (AbstractLogInjector<Object>) injectorClazz.newInstance();
                INJECTORS.put(clazz, injector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return injector;
    }

}
