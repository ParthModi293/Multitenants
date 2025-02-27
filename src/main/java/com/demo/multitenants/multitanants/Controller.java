package com.demo.multitenants.multitanants;

import com.demo.multitenants.publicDbConnection.DbProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ApplicationContext applicationContext;


    @PostMapping(path = "/employee")
    public ResponseEntity<?> createEmployee() {
        String dbName="tenant_3";
        apiCall(dbName);
        Employee newEmployee = new Employee();
        newEmployee.setName("Baeldung");
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok(newEmployee);
    }

    public void apiCall(String dbName){

        DbProperty dbProperty = new DbProperty();
        dbProperty.setDbName(dbName);
        dbProperty.setUrl("jdbc:postgresql://localhost:5432/"+dbName);
        dbProperty.setUsername("master_user");
        dbProperty.setPassword("password");

        Object datasourceMultitenant = applicationContext.getBean("datasourceMultitenant");
        if (datasourceMultitenant instanceof MultitenantDataSource multitenantDataSource) {
            System.out.println("MultitenantDataSource");
            Map<Object, Object> dataSources = new HashMap<>(multitenantDataSource.getResolvedDataSources());

                DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.driverClassName(dbProperty.getDriverClassName());
                dataSourceBuilder.username(dbProperty.getUsername());
                dataSourceBuilder.password(dbProperty.getPassword());
                dataSourceBuilder.url(dbProperty.getUrl());
                dataSources.put(dbProperty.getDbName(), dataSourceBuilder.build());

            multitenantDataSource.setTargetDataSources(dataSources);
            multitenantDataSource.setDefaultTargetDataSource(multitenantDataSource.getResolvedDataSources().get("public"));
            multitenantDataSource.afterPropertiesSet();
        } else {
            System.err.println("Invalid datasource bean type: not supported");
        }
    }

    @PostMapping(path = "/employeeTest")
    public ResponseEntity<?> createEmployeeTest() {

        Employee newEmployee = new Employee();
        newEmployee.setName("Baeldung");
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok(newEmployee);
    }




}

