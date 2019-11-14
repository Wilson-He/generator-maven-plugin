/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import io.github.generator.TemplateConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.BiFunction;

/**
 * 全局配置
 *
 * @author hubin
 * @since 2016-12-02
 */
@Data
@Accessors(chain = true)
public class GlobalConfig {

    /**
     * 生成文件的输出目录【默认 当前项目的output目录下】
     */
    private String outputDir = System.getProperty("user.dir") + "/output/src/main/java";

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;

    /**
     * 是否打开输出目录
     */
    private boolean open = true;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;

    /**
     * 开发人员
     */
    private String author = "";

    /**
     * 开启 Kotlin 模式
     */
    private boolean kotlin = false;

    /**
     * 开启 swagger2 模式
     */
    private boolean swagger2 = false;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord = false;

    /**
     * 开启 BaseResultMap
     */
    private boolean baseResultMap = true;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 开启 baseColumnList
     */
    private boolean baseColumnList = false;
    /**
     * 各层文件名称方式，例如： %sAction 生成 UserAction
     * %s 为占位符
     */
    private String entityName;
    private String constantName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    /**
     * 指定生成的主键的ID类型
     */
    private IdType idType;
    private String crudIdType;

    private AutoGenerator autoGenerator;

    public AutoGenerator backGenerator() {
        return autoGenerator;
    }

    public GlobalConfig initName(TemplateConfig templates) {
        if (templates == null) {
            return this;
        }
        return compute(templates.getEntityPattern() != null, GlobalConfig::setEntityName, templates.getEntityPattern())
                .compute(templates.getConstantPattern() != null, GlobalConfig::setConstantName, templates.getConstantPattern())
                .compute(templates.getXmlPattern() != null, GlobalConfig::setXmlName, templates.getXmlPattern())
                .compute(templates.getDaoPattern() != null, GlobalConfig::setMapperName, templates.getDaoPattern())
                .compute(templates.getServicePattern() != null, GlobalConfig::setServiceName, templates.getServicePattern())
                .compute(templates.getServiceImplPattern() != null, GlobalConfig::setServiceImplName, templates.getServiceImplPattern())
                .compute(templates.getControllerPattern() != null, GlobalConfig::setControllerName, templates.getControllerPattern());
    }

    private GlobalConfig compute(boolean isExecute, BiFunction<GlobalConfig, String, GlobalConfig> setFunc, String val) {
        return isExecute ? setFunc.apply(this, val) : this;
    }

}
