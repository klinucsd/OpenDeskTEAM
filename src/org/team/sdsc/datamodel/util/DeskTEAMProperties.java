package org.team.sdsc.datamodel.util;

import java.util.*;
import java.io.*;

public class DeskTEAMProperties {

    private static Properties props = new Properties();

    static {
        load();
    }

    public static void load() {
        try {
	    InputStream is = DataUploadException.class.getResourceAsStream("/deskTEAM.properties");
            props.load(is);
        } catch (Exception ex){}
    }


    public static String getProperty(String key) {

        return props.getProperty(key);
    }


}
