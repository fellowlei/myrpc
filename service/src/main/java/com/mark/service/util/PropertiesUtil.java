package com.mark.service.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static Properties properties = new Properties();


    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public static String getPropertyValue(String key, String defValue) {
        return properties.getProperty(key, defValue);
    }

    public static String getEnvPropertyValue(String key, String defValue) {
        String value = System.getenv(key);
        return value != null?value:properties.getProperty(key, defValue);
    }

    public static int getPropertyIntValue(String key) {
        return Integer.parseInt(properties.getProperty(key).trim());
    }


    static {
        InputStream inputStream = null;

        try {
            inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("properties/business.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("load properties/business.properties failed!" + e.getMessage());
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
