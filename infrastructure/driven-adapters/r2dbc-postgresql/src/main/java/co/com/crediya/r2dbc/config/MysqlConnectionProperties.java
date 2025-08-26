package co.com.crediya.r2dbc.config;
//import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "adapters.r2dbc")
public record MysqlConnectionProperties(
        String url,
        String username,
        String password
) {}
