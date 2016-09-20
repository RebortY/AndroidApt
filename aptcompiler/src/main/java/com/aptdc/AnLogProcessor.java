package com.aptdc;

import com.aptdc.proxy.AnLogClass;
import com.aptdc.proxy.GenerationClass;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class AnLogProcessor extends AbstractProcessor{

    private static final String ANNOTATION = "@" + AnDataCollect.class.getSimpleName();
    private Messager messager;
    private Types typeUtils;
    private Filer mFiler;
    private Elements mElementsUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();
        mElementsUtils =  processingEnv.getElementUtils();
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    //值生成针对AnLog 生成的注解类
    @Override public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AnDataCollect.class.getCanonicalName());
    }

    //处理注解生成代码的地方
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //从注解中找到所有的类,以及对应的注解的方法
        LinkedHashMap<TypeElement,List<AnLogClass>> targetClassMap =  findAnnoationClass(roundEnvironment);
        //生成每一个类和方法
        for (Map.Entry<TypeElement, List<AnLogClass>> item : targetClassMap.entrySet()){

            TypeElement closeTypeElement = item.getKey();

            //获取包名
            String packageName = mElementsUtils.getPackageOf(closeTypeElement).getQualifiedName().toString();
            //获取类名称
            String className = item.getKey().getSimpleName().toString();
            //生成代理类
            GenerationClass generationClass = new GenerationClass(packageName,className);
            TypeSpec.Builder classGenBuilder =  generationClass.createClass();

            //生成抽象类的实现方法
            MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("T"),"target")
                .addParameter(String.class,"method");


            //添加处理方法
            List<AnLogClass> methods =  item.getValue();
            for (AnLogClass method: methods) {
                MethodSpec.Builder methodBuilder = generationClass.createMethod(method);
                MethodSpec methodSpec = methodBuilder.build();
                classGenBuilder.addMethod(methodSpec);

                injectMethod.addCode("if(method != null && method.length()>0)\n"
                    +"{\n"
                    +   " if(method.equals($S)){\n"
                    +   "    $N();\n"
                    +   " }\n"
                    +"}\n"
                    , method.getMethodName(),methodSpec
                );

            }
            classGenBuilder.addMethod(injectMethod.build());

            //生成类文件
            JavaFile javaFile = JavaFile.builder(packageName, classGenBuilder.build())
                     .addFileComment("Generated code from dc    . Do not modify!")
                     .build();
            try {
                 javaFile.writeTo(mFiler);
            } catch (IOException ex) {
                 error(closeTypeElement, "Unable to write binding for type %s: %s", closeTypeElement, ex.getMessage());
            }

        }

        return true;
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        processingEnv.getMessager().printMessage(kind, message, element);
    }

    private LinkedHashMap<TypeElement, List<AnLogClass>> findAnnoationClass(RoundEnvironment roundEnvironment)
    {
        LinkedHashMap<TypeElement,List<AnLogClass>> targetClassMap = new LinkedHashMap<>();
        Collection<? extends Element> anLogSet =  roundEnvironment.getElementsAnnotatedWith(AnDataCollect.class);
        for (Element e: anLogSet) {
            if (e.getKind() != ElementKind.METHOD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "only support class");
                continue;
            }
            TypeElement enclosingElement = (TypeElement) e.getEnclosingElement();

            List<AnLogClass> findLogList = targetClassMap.get(enclosingElement);
            if(findLogList == null){
                findLogList = new ArrayList<>();
                targetClassMap.put(enclosingElement,findLogList);
            }

            AnDataCollect log =  e.getAnnotation(AnDataCollect.class);
            String action  =  log.action();
            String tagName = log.tagname();
            AnLogClass anLogClass = new AnLogClass();
            anLogClass.setAction(action);
            anLogClass.setTagName(tagName);
            anLogClass.setMethodElement(e);

            findLogList.add(anLogClass);

        }
        return targetClassMap;
    }

}
