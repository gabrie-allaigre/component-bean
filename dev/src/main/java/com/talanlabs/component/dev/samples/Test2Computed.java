package com.talanlabs.component.dev.samples;

public class Test2Computed {

    String name;

    public String getName(ITest2 test2) {
        return name;
    }

    public void setName(ITest2 test2, String name) {
        this.name = name;
    }

    public void rien(ITest2 test2) {
        System.out.println("rien " + name);
    }
}
