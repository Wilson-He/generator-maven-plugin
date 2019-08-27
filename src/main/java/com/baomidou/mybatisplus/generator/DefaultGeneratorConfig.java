package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author Wilson
 * 获取生成器各默认配置实例工厂类
 */
public class DefaultGeneratorConfig {
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
                .setTemplate(new TemplatePaths().excludeController())
                // 选择模板引擎
                .setTemplateEngine(new FreemarkerTemplateEngine());
    }

    private static PackageConfig packageConfig(String basePackage) {
        return new PackageConfig()
                .setXml("mappers")
                // 设置所有生成所在的根包
                .setParent(basePackage);
    }

    private static StrategyConfig strategyConfig() {
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
