package io.github.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Driver;

/**
 * @author Wilson
 * @date 2019/7/31
 **/
@Getter
@AllArgsConstructor
public enum DriverType {
    /**
     * mysql
     */
    MYSQL(com.mysql.jdbc.Driver.class),
    /**
     * oracle
     */
    ORACLE(OracleDriver.class),
    /**
     * PostgreSQL
     */
    POSTGRE_SQL(org.postgresql.Driver.class);
    private Class<? extends Driver> driverClass;
}
