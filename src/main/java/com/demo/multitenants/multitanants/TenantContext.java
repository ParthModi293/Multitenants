package com.demo.multitenants.multitanants;


public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenant) {

        CURRENT_TENANT.set(tenant);
        System.out.println(Thread.currentThread().getName() + ": Setting current tenant to " + tenant);
    }

}