package co.com.crediya.r2dbc.config;


import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
/*
@Configuration
public class MysqlR2dbcConfig {

    private static final int INITIAL_SIZE = 5;
    private static final int MAX_SIZE = 20;
    private static final int MAX_IDLE_TIME = 30; // minutos

    @Bean
    public ConnectionPool mysqlConnectionPool(MysqlConnectionProperties properties) {

        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(properties.url())
                .mutate()
                .option(ConnectionFactoryOptions.USER, properties.username())
                .option(ConnectionFactoryOptions.PASSWORD, properties.password())
                .build();

        ConnectionFactory connectionFactory = ConnectionFactories.get(options);

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}*/