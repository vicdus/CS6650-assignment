package Utilities;

import org.apache.commons.dbcp.BasicDataSource;
import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DBConnectionPoolWrapper {
    private static String URL;
    private static String USR;
    private static String PWD;
    private static Connection c;
    private static BasicDataSource ds;

    static {
        try {
            Map<String, Object> credentials = (Map<String, Object>) Yaml.load(new File("./resource/credentials.yml"));
            Map<String, Object> config = (Map<String, Object>) Yaml.load(new File("./resource/db_config.yml"));
            URL = "jdbc:postgresql://"
                    + config.get("db_endpoint").toString() + ":"
                    + config.get("db_port").toString() + "/"
                    + config.get("db_name").toString();
            USR = credentials.get("db_usr").toString();
            PWD = credentials.get("db_pwd").toString();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot Find config file. See README");
            System.exit(1);
        }

        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(URL);
        ds.setUsername(USR);
        ds.setPassword(PWD);
        ds.setInitialSize(10);
        ds.setMaxActive(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
