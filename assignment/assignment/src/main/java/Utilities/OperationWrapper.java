package Utilities;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

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
}
