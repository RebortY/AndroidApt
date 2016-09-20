package com.aptdc;

/*************************
 * 文件信息
 * 模块名：com.example
 * 文件名：AbstractLogInjector
 * 创建者：yangll
 * 创建日期：16/9/18
 * 功能描述：
 * *************************
 */

public interface AbstractLogInjector<T> {
    /**
     * 绑定注解方式
     * @param target 目标类
     * @param method 目标方法
     */
    void inject(T target, String method);

}
