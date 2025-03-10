package com.demo.multitenants;

import com.demo.multitenants.multitanants.MultitenantDataSource;
import com.demo.multitenants.multitanants.TenantContext;
import com.demo.multitenants.publicDbConnection.DbProperty;
import com.demo.multitenants.publicDbConnection.DbPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan({"com.clapcle.communication", "com.clapcle.core","com.demo.multitenants"})
@EnableJpaRepositories({"com.demo.multitenants.publicDbConnection","com.clapcle.communication.repository"})
//@EntityScan({"com.clapcle.communication.entity"})
@EntityScan({"com.demo.multitenants.publicDbConnection", "com.clapcle.communication.entity"})

public class MultitenantsApplication {

    @Autowired
    private DbPropertyRepository dbPropertyRepository;

    @Autowired
    private Environment environment;



    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(MultitenantsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadTenants() {
        Object datasourceMultitenant = dataSource();
        if (datasourceMultitenant instanceof MultitenantDataSource multitenantDataSource) {
            System.out.println("MultitenantDataSource");
            TenantContext.setCurrentTenant("public");
            List<DbProperty> byDbNDbPropertyName = dbPropertyRepository.findAll();
            Map<Object, Object> dataSources = new HashMap<>(multitenantDataSource.getResolvedDataSources());
            byDbNDbPropertyName.forEach(db -> {
                DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.driverClassName(db.getDriverClassName());
                dataSourceBuilder.username(db.getUsername());
                dataSourceBuilder.password(db.getPassword());
                dataSourceBuilder.url(db.getUrl());
                dataSources.put(db.getDbName(), dataSourceBuilder.build());
            });
            multitenantDataSource.setTargetDataSources(dataSources);
            multitenantDataSource.setDefaultTargetDataSource(multitenantDataSource.getResolvedDataSources().get("public"));
            multitenantDataSource.afterPropertiesSet();
        } else {
            System.err.println("Invalid datasource bean type: not supported");
        }

    }

    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        String tenantId = "public";
        dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
        dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
        resolvedDataSources.put(tenantId, dataSourceBuilder.build());

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get("public"));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

}
