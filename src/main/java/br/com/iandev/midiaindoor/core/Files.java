package br.com.iandev.midiaindoor.core;

import java.io.File;

public class Files {

    public static final File DIR_APP;
    public static final File DIR_ASSETS;
    public static final File DIR_CONTENTS;
    public static final File DIR_LOGS;
    public static final File DIR_DB;

    static {
//        Logger logger = Logger.getLogger(Files.class);
//        ClassLoader classLoader = Files.class.getClassLoader();
//        URL url = classLoader.getResource("./resources/application.properties");
//        logger.debug(url.toString());
//        logger.debug(url.toExternalForm());
//        logger.debug(url.getFile());
//        File file = new File(url.getFile());

        DIR_APP = new File(".");
        DIR_ASSETS = getDirectory(DIR_APP, "assets");
        DIR_CONTENTS = getDirectory(DIR_ASSETS, "contents");
        DIR_LOGS = getDirectory(DIR_APP, "logs");
        DIR_DB = getDirectory(DIR_APP, "db");
    }

    public static File getDirectory(File parent, String child) {
        File dir = new File(parent, child);
        dir.mkdirs();
        return dir;
    }

    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File fileChild : files) {
                deleteDirectory(fileChild);
            }
        }
        directory.delete();
    }

    public static long size(File file) {
        long length = 0;
        if (file.isFile()) {
            length += file.length();
        } else {
            for (File child : file.listFiles()) {
                length += size(child);
            }
        }
        return length;
    }

    public static long size() {
        return size(DIR_APP);
    }

}
