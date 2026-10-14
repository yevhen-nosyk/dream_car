package ua.nure.nosyk.courseproject.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.nosyk.courseproject.dao.mysqldao.MySqlConnectionManager;

@Configuration
public class DAOFactory {
    @Bean
    public ConnectionManager connectionManager(DAOConfig config) {
        return new MySqlConnectionManager(config);
    }
}
