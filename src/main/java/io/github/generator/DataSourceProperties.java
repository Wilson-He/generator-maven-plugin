package io.github.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.Data;

import java.util.List;
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
    private boolean bitToInteger = true;
    /**
     * 注释样式,如: key:value:comment  key:value-comment
     */
    private String commentPattern;
    /**
     * 不生成指定字段常量类
     */
    private List<String> excludeConstantFields;

    DataSourceConfig toDataSourceConfig() {
        if (driverType == null) {
            driverType = DriverType.MYSQL;
        }
        Objects.requireNonNull(url, "url不能为空");
        Objects.requireNonNull(username, "username不能为空");
        Objects.requireNonNull(password, "password不能为空");
        if (!url.contains("?")) {
            url = url + "?useSSL=true";
        } else {
            if (!url.contains("useSSL")) {
                url += "&useSSL=true";
            }
        }
        ConstantCommentConfig.initInstance(commentPattern, excludeConstantFields);
        return new DataSourceConfig(url, driverType.getDriverClass().getCanonicalName(), username, password)
                .setBitToInteger(bitToInteger);
    }
}
