package framework.utils;

import java.io.*;
import java.util.Properties;

public class FileUtils {

    private FileUtils(){}

    public static Properties getPropertiesFromFile(String filePath) throws IOException{
        InputStream stream = readFileAsStream(filePath);
        Properties prop = new Properties();

        if(stream == null) {
            throw new FileNotFoundException(filePath);
        } else {
            prop.load(stream);
        }
        return prop;
    }

    public static InputStream readResourceAsStream(String filePath) {
        return FileUtils.class.getClassLoader().getResourceAsStream(filePath);
    }

    public static InputStream readFileAsStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(getAbsoluteFilePath(filePath));
    }

    public static String getAbsoluteFilePath(String filePath) {
        return new File(filePath).getAbsolutePath();
    }

    public static void clearDirectory(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    FileUtils.clearDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
    }
}
