package mx.ipn.escom.ia.cerradura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/practica2?useSSL=false&serverTimezone=UTC")
                .username("admin")
                .password("admin")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}