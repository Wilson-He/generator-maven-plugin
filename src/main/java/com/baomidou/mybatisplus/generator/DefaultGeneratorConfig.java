package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * 获取生成器各默认配置实例工厂类
 */
public class DefaultGeneratorConfig {

    /**
     * 获取默认生成器
     *
     * @param dbConfigFileName Spring数据库配置文件名,如application.yml、application.properties
     * @param packageClass     生成文件到packageClass所在的包
     * @return AutoGenerator
     * @throws IOException 文件读取错误
     */
    public static AutoGenerator defaultAutoGenerator(String dbConfigFileName, Class<?> packageClass) throws IOException {
        return defaultAutoGenerator(dbConfigFileName, packageClass.getPackage().getName());
    }

    /**
     * 获取默认生成器
     *
     * @param dbConfigFileName Spring数据库配置文件名,如application.yml、application.properties
     * @param basePackage      java文件生成基包
     * @return AutoGenerator
     * @throws IOException 文件读取错误
     */
    public static AutoGenerator defaultAutoGenerator(String dbConfigFileName, String basePackage) throws IOException {
        return new AutoGenerator()
                .setDataSource(dataSourceConfig(dbConfigFileName))
                // 设置全局配置
                .setGlobalConfig(globalConfig())
                // 设置各层文件的生成目录
                .setPackageInfo(packageConfig(basePackage))
                // 策略配置项
                .setStrategy(strategyConfig())
                // 模板生成设置
                .setTemplate(new TemplateConfig().excludeController())
                // 选择模板引擎
                .setTemplateEngine(new FreemarkerTemplateEngine());
    }

    /**
     * 获取默认生成器
     *
     * @param basePackage      java文件生成基包
     * @param dataSourceConfig
     * @return AutoGenerator
     */
    public static AutoGenerator defaultAutoGenerator(String basePackage, DataSourceConfig dataSourceConfig) {
        return new AutoGenerator()
                .setDataSource(dataSourceConfig)
                // 设置全局配置
                .setGlobalConfig(globalConfig())
                // 设置各层文件的生成目录
                .setPackageInfo(packageConfig(basePackage))
                // 策略配置项
                .setStrategy(strategyConfig())
                // 模板生成设置
                .setTemplate(new TemplateConfig().excludeController())
                // 选择模板引擎
                .setTemplateEngine(new FreemarkerTemplateEngine());
    }

    /**
     * 获取默认生成器
     *
     * @param url         数据库url
     * @param username    数据库username
     * @param password    数据库password
     * @param driverClazz 数据库驱动类
     * @param basePackage java文件生成基包
     * @return AutoGenerator
     */
    public static AutoGenerator defaultAutoGenerator(String url, String username, String password, Class<? extends java.sql.Driver> driverClazz, String basePackage) {
        return new AutoGenerator()
                .setDataSource(dataSourceConfig(url, username, password, driverClazz))
                // 设置全局配置
                .setGlobalConfig(globalConfig())
                // 设置各层文件的生成目录
                .setPackageInfo(packageConfig(basePackage))
                // 策略配置项
                .setStrategy(strategyConfig())
                // 模板生成设置
                .setTemplate(new TemplateConfig())
                // 选择模板引擎
                .setTemplateEngine(new FreemarkerTemplateEngine());
    }

    public static PackageConfig packageConfig(String basePackage) {
        return new PackageConfig()
                .setXml("mappers")
                // 设置所有生成所在的根包
                .setParent(basePackage);
    }

    /**
     * 获取生成器数据库配置类
     *
     * @param configFileName 配置文件名
     * @return DataSourceConfig
     * @throws IOException 文件读取错误
     */
    public static DataSourceConfig dataSourceConfig(String configFileName) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:" + configFileName);
        Properties properties;
        if (configFileName.endsWith("properties")) {
            properties = PropertiesLoaderUtils.loadProperties(resources[0]);
        } else if (configFileName.endsWith("yml")) {
            YamlPropertiesFactoryBean yamlBean = new YamlPropertiesFactoryBean();
            yamlBean.setResources(resources);
            properties = yamlBean.getObject();
        } else {
            throw new InvalidParameterException("只支持yml与properties配置文件");
        }
        return new DataSourceConfig()
                .setUrl(properties.getProperty("spring.datasource.url"))
                .setUsername(properties.getProperty("spring.datasource.username"))
                .setPassword(properties.getProperty("spring.datasource.password"))
                .setDriverName(properties.getProperty("spring.datasource.driver-class-name"));
    }

    public static DataSourceConfig dataSourceConfig(String url, String username, String password, Class driverClass) {
        return new DataSourceConfig()
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(driverClass.getCanonicalName());
    }

    public static StrategyConfig strategyConfig() {
        return new StrategyConfig()
                // 命名下划线转驼峰式
                .setNaming(NamingStrategy.underline_to_camel)
                // 设置只生成指定表的entity、mapper... exclude则设置不生成的表
                // 生成的entity是否使用lombok注解
                .setEntityLombokModel(true)
                // 使用@RestController而非@Controller
                .setRestControllerStyle(true);
    }

    public static GlobalConfig globalConfig() {
        return new GlobalConfig()
                // 设置生成文件的命名格式
                .setServiceName("%sService")
                .setXmlName("%sMapper")
                // 设置生成的日期类型,默认TIME_PACK-jdk1.8的日期类型, ONLY_DATE-java.util.Date
                .setDateType(DateType.TIME_PACK)
                // 设置@TableId注解的type
                .setIdType(IdType.AUTO)
                // 设置是否生成swagger注解,默认false
                .setSwagger2(false);
    }
}
