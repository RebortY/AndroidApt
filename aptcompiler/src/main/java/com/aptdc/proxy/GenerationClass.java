package com.aptdc.proxy;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import javax.lang.model.element.Modifier;

/*************************
 * 文件信息
 * 模块名：com.example.proxy
 * 文件名：GenerationClass
 * 创建者：yangll
 * 创建日期：16/9/18
 * 功能描述：
 * *************************
 */

public class GenerationClass {


    private static final ClassName INJECTOR = ClassName.get("com.aptdc", "AbstractLogInjector");
    private static final ClassName LOGSENDER = ClassName.get("com.aptdc","DCSender");


    private String packageName;
    private String targetClassName;
    private ClassName tClassName;
    private String proxyClassName;
    public static final  String SUFFIX ="$$YLL";

    public GenerationClass(String packageName, String targetClassName) {
        this.packageName = packageName;
        this.targetClassName = targetClassName;
        this.proxyClassName = targetClassName+SUFFIX;
        tClassName = ClassName.get(packageName,targetClassName);
    }



    public MethodSpec.Builder createMethod(AnLogClass logClass){
        return MethodSpec.methodBuilder(logClass.getMethodElement().getSimpleName().toString())
            .addStatement("new $T().sendLog($S,$S)",LOGSENDER,logClass.getAction(),logClass.getTagName());
    }

   public TypeSpec.Builder createClass(){

        TypeSpec.Builder logClass  = TypeSpec.classBuilder(proxyClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ParameterizedTypeName.get(INJECTOR,TypeVariableName.get("T")))
            .addTypeVariable(TypeVariableName.get("T",tClassName));

        return logClass;
    }




}
