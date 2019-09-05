package io.github.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.Data;

import java.util.Objects;

/**
 * @author Wilson
 * @date 2019/7/30
 **/
@Data
public class DataSourceProperties {
    private DriverType driverType;
    private String url;
    private String username;
    private String password;

    DataSourceConfig toDataSourceConfig() {
        if(driverType == null){
            driverType = DriverType.MYSQL;
        }
        Objects.requireNonNull(url, "url不能为空");
        Objects.requireNonNull(username, "username不能为空");
        Objects.requireNonNull(password, "password不能为空");
        return new DataSourceConfig(url, driverType.getDriverClass().getCanonicalName(), username, password);
    }
}
