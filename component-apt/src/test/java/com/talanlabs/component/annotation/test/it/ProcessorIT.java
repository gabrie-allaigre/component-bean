package com.talanlabs.component.annotation.test.it;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.talanlabs.aptdebug.APTCompiler;
import com.talanlabs.aptdebug.JavaFileObjects;
import com.talanlabs.component.annotation.processor.ComponentBeanProcessor;
import com.talanlabs.component.annotation.test.data.IAddress;
import com.talanlabs.component.annotation.test.data.IUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.List;

public class ProcessorIT {

    @Test
    public void test1() {
        APTCompiler.Result result = APTCompiler.newCompiler().processedWith(new ComponentBeanProcessor(false)).build(JavaFileObjects.forClass(IUser.class), JavaFileObjects.forClass(IAddress.class));

        List<JavaFileObject> javaFileObjects = result.getGeneratedSources();

        Assertions.assertThat(javaFileObjects).hasSize(12);

        javaFileObjects.forEach(javaFileObject -> {
            String path = JavaFileObjects.getPathName(javaFileObject);

            try {
                String source = JavaFileObjects.toString(javaFileObject);
                String res = Resources.toString(Resources.getResource(path), Charsets.UTF_8);

                Assertions.assertThat(source).isEqualToIgnoringWhitespace(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
