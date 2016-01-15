package com.talanlabs.component.dev;

import com.talanlabs.component.dev.samples.IAddress;
import com.talanlabs.component.mapper.ComponentMapper;

import java.util.HashMap;
import java.util.Map;

public class MainGeneric {

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("city", "gaby");
        System.out.println(ComponentMapper.getInstance().toComponent(map, IAddress.class));
    }
}
