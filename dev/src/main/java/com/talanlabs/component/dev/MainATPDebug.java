package com.talanlabs.component.dev;

import com.google.common.base.Charsets;
import com.talanlabs.aptdebug.APTCompiler;
import com.talanlabs.aptdebug.JavaFileObjects;
import com.talanlabs.component.annotation.processor.ComponentBeanProcessor;
import com.talanlabs.component.dev.samples.IParent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainATPDebug {

    public static void main(String[] args) {
        APTCompiler.Result result = APTCompiler.newCompiler().processedWith(new ComponentBeanProcessor()).build(JavaFileObjects.forClass(IParent.class), JavaFileObjects.forClass(IParent.IFils.class));

        result.getDiagnostics().stream().forEach(System.out::println);

        result.getGeneratedSources().forEach(javaFileObject -> {
            String pathName = JavaFileObjects.getPathName(javaFileObject);
            try {
                String source = JavaFileObjects.toString(javaFileObject);

                Path path = Paths.get("dev/target/generated-sources/apt/" + pathName);
                if (path.toFile().exists()) {
                    path.toFile().delete();
                }
                path.getParent().toFile().mkdirs();
                System.out.println("Write " + path.toString());
                Files.write(path, source.getBytes(Charsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
