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

import com.baomidou.mybatisplus.generator.AutoGenerator;
import io.github.generator.TemplatePath;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

/**
 * 模板路径配置项
 *
 * @author tzg hubin
 * @since 2017-06-17
 */
@Data
@Accessors(chain = true)
@ToString(exclude = "autoGenerator")
public class TemplateConfig {

    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;
    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;
    private String constant = ConstVal.TEMPLATE_ENTITY_CONSTANT;
    private String service = ConstVal.TEMPLATE_SERVICE;
    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;
    private String mapper = ConstVal.TEMPLATE_MAPPER;
    private String xml = ConstVal.TEMPLATE_XML;
    private String controller = ConstVal.TEMPLATE_CONTROLLER;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }


    private AutoGenerator autoGenerator;

    public AutoGenerator backGenerator() {
        return autoGenerator;
    }

    /**
     * 不生成Controller
     *
     * @return TemplateConfig
     */
    public TemplateConfig excludeController() {
        return setController(null);
    }

    /**
     * 不生成Controller
     *
     * @return TemplateConfig
     */
    public TemplateConfig useEnumConstant() {
        return setConstant(ConstVal.TEMPLATE_ENTITY_CONSTANT_ENUM);
    }

    public TemplateConfig computePath(String path, BiFunction<TemplateConfig, String, TemplateConfig> setFunc) {
        return StringUtils.isNotEmpty(path) ? setFunc.apply(this, StringUtils.substringBefore(path, ".ftl")) : this;
    }

    public TemplateConfig parseTemplatePath(TemplatePath templatePath) {
        if (templatePath == null) {
            return this;
        }
        computePath(templatePath.getEntity(), TemplateConfig::setEntity);
        computePath(templatePath.getConstant(), TemplateConfig::setConstant);
        computePath(templatePath.getDao(), TemplateConfig::setMapper);
        computePath(templatePath.getXml(), TemplateConfig::setXml);
        computePath(templatePath.getService(), TemplateConfig::setService);
        computePath(templatePath.getServiceImpl(), TemplateConfig::setServiceImpl);
        computePath(templatePath.getController(), TemplateConfig::setController);
        excludeIf(templatePath.getExcludeEntity(), TemplateConfig::setEntity, null);
        excludeIf(templatePath.getExcludeConstant(), TemplateConfig::setConstant, null);
        excludeIf(templatePath.getExcludeDao(), TemplateConfig::setMapper, null);
        excludeIf(templatePath.getExcludeXml(), TemplateConfig::setXml, null);
        excludeIf(templatePath.getExcludeService(), TemplateConfig::setService, null);
        excludeIf(templatePath.getExcludeServiceImpl(), TemplateConfig::setServiceImpl, null);
        excludeIf(templatePath.getExcludeController(), TemplateConfig::setController, null);
        return this;
    }

    private <T> TemplateConfig excludeIf(boolean isExclude, BiFunction<TemplateConfig, T, TemplateConfig> setFunc, T t) {
        return isExclude ? setFunc.apply(this, t) : this;
    }

}
