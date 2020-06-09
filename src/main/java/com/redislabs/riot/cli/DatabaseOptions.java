package com.redislabs.riot.cli;

import javax.sql.DataSource;

import lombok.Getter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@Slf4j
public class DatabaseOptions {

    @Getter
    @CommandLine.Parameters(arity = "1", description = "SQL statement", paramLabel = "SQL")
    private String sql;
    @Option(names = "--driver", description = "Fully qualified name of the JDBC driver", paramLabel = "<class>")
    private String driver;
    @Option(names = "--url", required = true, description = "JDBC URL to connect to the database", paramLabel = "<string>")
    private String url;
    @Option(names = "--username", description = "Login username of the database", paramLabel = "<string>")
    private String username;
    @Option(names = "--password", arity = "0..1", interactive = true, description = "Login password of the database", paramLabel = "<pwd>")
    private String password;

    public DataSource getDataSource() {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setUrl(url);
        properties.setDriverClassName(driver);
        properties.setUsername(username);
        properties.setPassword(password);
        log.debug("Initializing datasource: driver={} url={}", driver, url);
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

}