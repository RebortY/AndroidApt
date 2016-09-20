package com.aptdc.proxy;

import javax.lang.model.element.Element;

/*************************
 * 文件信息
 * 模块名：com.aptlog.proxy
 * 文件名：AnLogClass
 * 创建者：yangll
 * 创建日期：16/9/19
 * 功能描述：
 * *************************
 */

public class AnLogClass {


    private Element methodElement;
    private String action;
    private String tagName;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Element getMethodElement() {
        return methodElement;
    }

    public void setMethodElement(Element methodElement) {
        this.methodElement = methodElement;
    }

    public String getMethodName(){
        if(methodElement == null)
        {
            return "";
        }
        return methodElement.getSimpleName().toString();
    }
}
