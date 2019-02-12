package de.hhu.abschlussprojektverleihplattform.database;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:h2:tcp://" + host + ":" + port + "/perfectlybalanced");
        dataSourceBuilder.username(user);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
}