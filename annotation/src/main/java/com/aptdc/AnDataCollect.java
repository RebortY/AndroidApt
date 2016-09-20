package com.aptdc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*************************
 * 文件信息
 * 模块名：com.example
 * 文件名：AnDataCollect
 * 创建者：yangll
 * 创建日期：16/9/17
 * 功能描述：
 * *************************
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AnDataCollect {
    String tagname() default "tag";
    String action() ;
}
