package framework.core;

import com.beust.jcommander.JCommander;
import framework.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class CliParser {

    public static void parseArgs(Object obj, String[] cliArguments, String[] defaultArguments) {
        getCliParser(obj).parse(defaultArguments);
        getCliParser(obj).parse(cliArguments);
    }

    public static void parseArgs(Object obj, String[] cliArguments, String defaultArgumentsPropertyFile) throws IOException {
        Properties props = FileUtils.getPropertiesFromFile(defaultArgumentsPropertyFile);
        parseArgs(obj, cliArguments, convertPropsToCommandLineArgs(props));
    }

    private static JCommander getCliParser(Object obj) {
        return new JCommander.Builder().acceptUnknownOptions(true).addObject(obj).build();
    }

    private static String[] convertPropsToCommandLineArgs(Properties prop) {
        List<String> args = new ArrayList<>();
        Enumeration keys = prop.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            args.add("--" + key);
            args.add((String) prop.get(key));
        }
        return args.stream().toArray(String[] ::new);
    }

}
