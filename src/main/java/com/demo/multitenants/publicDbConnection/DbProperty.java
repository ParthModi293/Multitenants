/*
package com.demo.multitenants.publicDbConnection;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "db_properties")
@Data
public class DbProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('db_properties_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "db_name", nullable = false)
    private String dbName;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "driver_class_name", nullable = false, length = 1000)
    private String driverClassName;

    @Column(name = "password", nullable = false)
    private String password;




}*/
