package com.demo.multitenants.publicDbConnection;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DbPropertyRepository extends JpaRepository<DbProperty, Integer> {

    DbProperty findByDbName(String dbName);

}
