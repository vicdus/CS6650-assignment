package utilities;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DBConnectionPoolWrapper {
    private static BasicDataSource ds;

    static {
        Map<String, String> credentials = OperationWrapper.readConfig("./resource/credentials.yml");
        Map<String, String> config = OperationWrapper.readConfig("./resource/db_config.yml");

        String url = "jdbc:postgresql://"
                + config.get("db_endpoint") + ":"
                + config.get("db_port") + "/"
                + config.get("db_name");
        String usr = credentials.get("db_usr");
        String pwd = credentials.get("db_pwd");
        Integer dbConnPoolSize = Integer.parseInt(config.get("db_connection_pool_size"));
        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(usr);
        ds.setPassword(pwd);
        ds.setMaxActive(dbConnPoolSize);
        ds.setInitialSize(10);
        ds.setTestOnBorrow(false);
        ds.setDefaultAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
