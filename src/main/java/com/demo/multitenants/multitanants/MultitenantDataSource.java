package com.demo.multitenants.multitanants;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected String determineCurrentLookupKey() {
        String currentTenant = TenantContext.getCurrentTenant();
        System.out.println("datasource--"+ Thread.currentThread().getName());
        System.out.println("tenant not found : " + currentTenant);
        return currentTenant;
    }

   /* public void resolveDataSources(String tenant) {
        resolveSpecifiedDataSource(tenant);
    }*/
}