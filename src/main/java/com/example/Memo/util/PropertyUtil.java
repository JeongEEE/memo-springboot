package com.example.Memo.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertyUtil {

    public static Map<String, String> properties = new HashMap<String, String>();

    static {
        InputStream input = null;
        try {
            input = com.example.Memo.util.PropertyUtil.class.getResourceAsStream("/application.properties");
            Properties p = new Properties();
            InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
            p.load(isr);
            for (Object key : p.keySet()) {
                properties.put((String) key, (String) p.get(key));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    /**
     * 返回配置文件参数
     */
    public static String get(String key) {
        return properties.get(key);
    }
}
