package io.github.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.BeanUtils;

/**
 * @author Wilson
 * @date 2019/7/30
 **/
@Data
public class DataSourceProperties {
    private DriverType driverClass;
    private String url;
    private String username;
    private String password;

    public DataSourceConfig toDataSourceConfig() {
        return new DataSourceConfig(url, driverClass.getDriverClass().getCanonicalName(), username, password);
    }
}
