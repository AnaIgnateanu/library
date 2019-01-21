package com.task.library.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


@Configuration
public class DatabaseRouter {

    @Autowired
    @PostConstruct
    void init() {
        populate(primaryDataSource());
        populate(secondaryDataSource());
    }

    public void populate(DataSource dataSource) {
        Resource initSchema = new ClassPathResource("schema-mysql.sql");
        Resource initData = new ClassPathResource("data-mysql.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }

    @Bean("primary")
    @ConfigurationProperties(prefix="spring.primary.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("secondary")
    @ConfigurationProperties(prefix="spring.second.datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @SuppressWarnings("serial")
	@Bean("router")
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> targetDatasources = new HashMap<Object, Object>(){{
            put(Database.SECONDARY, secondaryDataSource());
            put(Database.PRIMARY, primaryDataSource());
        }};
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(secondaryDataSource());
        routingDataSource.setTargetDataSources(targetDatasources);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

}
