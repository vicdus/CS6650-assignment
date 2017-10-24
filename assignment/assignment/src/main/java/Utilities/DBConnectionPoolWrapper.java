package Utilities;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DBConnectionPoolWrapper {
    private static BasicDataSource ds;

    static {
        Map<String, String> credentials = OperationWrapper.readConfig("./resource/credentials.yml");
        Map<String, String> config = OperationWrapper.readConfig("./resource/db_config.yml");

        String URL = "jdbc:postgresql://"
                + config.get("db_endpoint") + ":"
                + config.get("db_port") + "/"
                + config.get("db_name");
        String USR = credentials.get("db_usr");
        String PWD = credentials.get("db_pwd");
        Integer DB_CONNECTION_POOL_SIZE = Integer.parseInt(config.get("db_connection_pool_size"));
        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(URL);
        ds.setUsername(USR);
        ds.setPassword(PWD);
        ds.setMaxActive(DB_CONNECTION_POOL_SIZE);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
