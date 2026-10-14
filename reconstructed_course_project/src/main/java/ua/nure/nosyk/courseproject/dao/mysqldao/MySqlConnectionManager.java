package ua.nure.nosyk.courseproject.dao.mysqldao;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import ua.nure.nosyk.courseproject.dao.*;

import java.sql.*;

public class MySqlConnectionManager implements ConnectionManager {
    private final MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

    public MySqlConnectionManager(DAOConfig config) {
        dataSource.setUrl(config.getUrl());
        dataSource.setUser(config.getUser());
        dataSource.setPassword(config.getPassword());
    }

    public Connection getConnection() throws SQLException {
        return getConnection(true);
    }

    public Connection getConnection(boolean autoCommit) throws SQLException {
        Connection c = dataSource.getConnection();
        c.setAutoCommit(autoCommit);
        return c;
    }

    public Connection getConnection(boolean autoCommit, int txIsolation) throws SQLException {
        Connection c = getConnection(autoCommit);
        c.setTransactionIsolation(txIsolation);
        return c;
    }
}
