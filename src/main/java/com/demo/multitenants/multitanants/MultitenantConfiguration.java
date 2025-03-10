package com.demo.multitenants.multitanants;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultitenantConfiguration {

    @Value("${defaultTenant}")
    private String defaultTenant;

    @Bean(name = "datasourceMultitenant")
    @Lazy
    @ConfigurationProperties(prefix = "tenants")
    public DataSource dataSource() {
        File[] files = Paths.get("allTenants").toFile().listFiles();
        System.out.println(files);
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        for (File propertyFile : files) {
            Properties tenantProperties = new Properties();
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            try {
                tenantProperties.load(new FileInputStream(propertyFile));
                String tenantId = tenantProperties.getProperty("name");
                dataSourceBuilder.driverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
                dataSourceBuilder.username(tenantProperties.getProperty("datasource.username"));
                dataSourceBuilder.password(tenantProperties.getProperty("datasource.password"));
                dataSourceBuilder.url(tenantProperties.getProperty("datasource.url"));
                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
            } catch (IOException exp) {
                throw new RuntimeException("Problem in tenant datasource:" + exp);
            }
        }
        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get("public"));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }


   /* @Bean
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        List<DbProperty> byDbNDbPropertyame = dbPropertyRepository.findAll();

        for (DbProperty tenant : byDbNDbPropertyame) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                    .driverClassName(tenant.getDriverClassName())
                    .url(tenant.getUrl())
                    .username(tenant.getUsername())
                    .password(tenant.getPassword());
            resolvedDataSources.put(tenant.getDbName(), dataSourceBuilder.build());
        }

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();

        return dataSource;
    }*/

 /*   @Bean
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/postgres")  // Replace with actual DB details
                .username("master_user")
                .password("password")
                .driverClassName("org.postgresql.Driver")
                .build();
    }*/

}
