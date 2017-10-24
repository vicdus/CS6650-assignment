package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.ho.yaml.Yaml;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.google.common.collect.*;


public class OperationWrapper {
    public static void parseSilently(String[] args, Object bean, String errmsg) {
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(errmsg);
            System.exit(1);
        }
    }

    public static Map<String, String> readConfig(String ymlFilePath) {
        try {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            ((Map<String, Object>) Yaml.load(new File(ymlFilePath))).forEach((k, v) -> builder.put(k, v.toString()));
            return builder.build();
        } catch (FileNotFoundException e) {
            wrapException(e);
        }
        return null;
    }


    public static void wrapException(Exception e) {
        throw new RuntimeException(e);
    }

}
