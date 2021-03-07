package com.leaves.navigationcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.leaves.navigationannotation.ActivityDestination;
import com.leaves.navigationannotation.DestinationType;
import com.leaves.navigationannotation.FragmentDestination;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * APP页面导航信息收集注解处理器
 * <p>
 * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
 * <p>
 * SupportedSourceVersion注解:声明我们所支持的jdk版本
 * <p>
 * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.leaves.navigationannotation.FragmentDestination", "com.leaves.navigationannotation.ActivityDestination"})
public class NavProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private String clzType;
    private static String OUT_FILE_PATH = "destination.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        //日志打印,在java环境下不能使用android.util.log.e()
        messager = processingEnv.getMessager();
        //文件处理工具
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //通过处理器环境上下文roundEnv分别获取 项目中标记的FragmentDestination.class 和ActivityDestination.class注解。
        //此目的就是为了收集项目中哪些类 被注解标记了
        Set<? extends Element> fragmentElements = roundEnv.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnv.getElementsAnnotatedWith(ActivityDestination.class);

        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            Map<String, JSONObject> destMap = new HashMap<>();
            //分别 处理FragmentDestination  和 ActivityDestination 注解类型
            //并收集到destMap 这个map中。以此就能记录下所有的页面信息了
            if (!fragmentElements.isEmpty()){
                handleDestination(fragmentElements, FragmentDestination.class, destMap);
            }
            if (!activityElements.isEmpty()){
                handleDestination(activityElements, ActivityDestination.class, destMap);
            }

            //app/src/main/assets
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            try {
                //filer.createResource()意思是创建源文件
                //我们可以指定为class文件输出的地方，
                //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
                //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
                //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUT_FILE_PATH);
                String resourcePath = resource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath);

                //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
                //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets/";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                //此处就是稳健的写入了
                File outPutFile = new File(file, OUT_FILE_PATH);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();

                //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
                String content = JSON.toJSONString(destMap);
                fos = new FileOutputStream(outPutFile);
                writer = new OutputStreamWriter(fos, "UTF-8");
                writer.write(content);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        return true;
    }

    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationCls, Map<String, JSONObject> destMap) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            int id = Math.abs(typeElement.hashCode());
            String className = typeElement.getQualifiedName().toString();
            String pageUrl = null;
            boolean needLogin = false;
            boolean asStart = false;

            //得到具体的注解类型
            Annotation annotation = typeElement.getAnnotation(annotationCls);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination fragmentDestination = (FragmentDestination) annotation;
                pageUrl = fragmentDestination.pageUrl();
                needLogin = fragmentDestination.needLogin();
                asStart = fragmentDestination.asStart();
                clzType = DestinationType.TYPE_FRAGMENT;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination activityDestination = (ActivityDestination) annotation;
                pageUrl = activityDestination.pageUrl();
                needLogin = activityDestination.needLogin();
                asStart = activityDestination.asStart();
                clzType = DestinationType.TYPE_ACTIVITY;
            }

            //如果有页面使用了相同的PageUrl,则抛出异常
            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的 PageUrl: " + className);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("className", className);
                jsonObject.put("id", id);
                jsonObject.put("pageUrl", pageUrl);
                jsonObject.put("needLogin", needLogin);
                jsonObject.put("asStart", asStart);
                jsonObject.put("classType", clzType);
                destMap.put(pageUrl, jsonObject);
            }
        }
    }
}
