package com.nldy.uploader;

import com.nldy.util.PropertyUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * created by shui 2017/9/28
 */
public class MyConfiguration {

    private static Map<String, String> items = new HashMap<>();

    private static String CONFIG_PROPERTIES_NAME = "config.properties";

    static {
        loadConfig();
    }

    private static void loadConfig() {

        PropertyUtil.loadProp(CONFIG_PROPERTIES_NAME);

        Set keyValue = PropertyUtil.prop.keySet();
        for (Iterator it = keyValue.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = PropertyUtil.prop.getProperty(key);
            items.put(key, value);
        }
    }

    /**
     * 获得字串配置值
     *
     * @param name
     * @return
     */
    public static String getString(String name) {
        String value = (String) items.get(name);
        return (value == null) ? "" : value;
    }
}


