package com.demo.multitenants.multitanants;

import com.demo.multitenants.publicDbConnection.DbPropertyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Order(1)
class TenantFilter implements Filter {


    private final DbPropertyRepository dbPropertyRepository;

    public TenantFilter(DbPropertyRepository dbPropertyRepository) {
        this.dbPropertyRepository = dbPropertyRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String tenantName = req.getHeader("X-TenantID");
        TenantContext.setCurrentTenant(tenantName);
        System.out.println("Filter --" + Thread.currentThread().getName());

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.setCurrentTenant("");
        }
    }
}
