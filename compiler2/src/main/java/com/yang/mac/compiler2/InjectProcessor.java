package com.yang.mac.compiler2;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by bo on 2018/10/22.
 */
@SupportedAnnotationTypes({"com.yang.mac.annotations.InjectInt", "com.yang.mac.annotations.InjectString"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class InjectProcessor extends AbstractProcessor {
    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    private HashMap<String, GenCreateJavaFile> mGenCreateJavaFiles = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (TypeElement te : set) {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(te)) {
                addElementToGencreateJavaFile(element);
            }
        }
        createJavaFile();
        return true;
    }

    private void createJavaFile() {
        for (String className : mGenCreateJavaFiles.keySet()) {
            GenCreateJavaFile file = mGenCreateJavaFiles.get(className);
            MethodSpec.Builder builder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(CONTEXT, "context");
            for (Element element : file.elements) {
//                if (element.asType().toString().equals("int")) {
//                    builder.addStatement("(($N)context).$N = context.getResources().getInteger(R.integer.$N)", file.className.split("_")[0],
//                            element.getSimpleName(), element.getSimpleName());
//                } else if (element.asType().toString().equals("java.lang.String")) {
//                    builder.addStatement("(($N)context).$N = context.getResources().getString(R.String.$N)", file.className.split("_")[0],
//                            element.getSimpleName(), element.getSimpleName());
//                }
                if (element.asType().toString().equals("int")) {
                    builder.addStatement("(($N)context).$N = context.getResources().getInteger(R.integer.$N)",
                            file.className.split("_")[0], element.getSimpleName(), element.getSimpleName());
                } else if (element.asType().toString().equals("java.lang.String")) {
                    builder.addStatement("(($N)context).$N = context.getResources().getString(R.string.$N)",
                            file.className.split("_")[0], element.getSimpleName(), element.getSimpleName());
                }
            }

            TypeSpec typeSpec = TypeSpec.classBuilder(file.className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(builder.build())
                    .build();
            try {
                JavaFile javaFile = JavaFile.builder(file.packageName, typeSpec).build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addElementToGencreateJavaFile(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String[] split = typeElement.getQualifiedName().toString().split("\\.");
        String className = split[split.length - 1];
        GenCreateJavaFile genCreateJavaFile = mGenCreateJavaFiles.get(className);
        if (genCreateJavaFile == null) {
            GenCreateJavaFile file = new GenCreateJavaFile();
            file.packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
            file.className = className + "_Inject";
            file.elements = new ArrayList<>();
            file.elements.add(element);
            mGenCreateJavaFiles.put(className, file);
        } else {
            genCreateJavaFile.elements.add(element);
        }

    }

    private static class GenCreateJavaFile {
        String packageName;
        String className;
        List<Element> elements;
    }
}
