package com.testninja.selenium.utils;

import com.beust.jcommander.internal.Nullable;
import org.bson.internal.Base64;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtils {

    private FileUtils() {
    }

    public static Properties getPropertiesFromFile(String filePath) throws IOException {
        InputStream stream = readFileAsStream(filePath);
        Properties prop = new Properties();

        if (stream == null) {
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

    public static String getRelativeFilePath(String basePath, String filePath) {
        Path pathAbsolute = Paths.get(filePath);
        Path pathBase = Paths.get(basePath);
        Path pathRelative = pathBase.relativize(pathAbsolute);
        return pathRelative.toString();
    }

    public static String convertImageToBase64(String filePath) throws IOException {
        File imageFile = new File(filePath);
        return Base64.encode(org.apache.commons.io.FileUtils.readFileToByteArray(imageFile));
    }

    public static void createDirectoryIfNotExists(String directoryPath, @Nullable Boolean emptyDirectoryIfExists) {
        File dirPath = new File(directoryPath);
        if (dirPath.exists()) {
            if (emptyDirectoryIfExists) {
                clearDirectory(dirPath);
            }
        } else {
            dirPath.mkdirs();
        }
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

    public static String getAbsoluteFolderPath(String folderName) {
        return System.getProperty("user.dir") + File.separator + folderName;
    }

    private static String getFilePathFormRsource(String basePath) {
        return new File(FileUtils.class.getClassLoader().getResource(basePath).getFile()).getPath();
    }
}
