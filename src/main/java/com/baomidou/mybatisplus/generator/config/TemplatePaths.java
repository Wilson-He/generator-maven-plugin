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
import io.github.generator.TemplateConfig;
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
public class TemplatePaths {

    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;
    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;
    private String constant = ConstVal.TEMPLATE_ENTITY_CONSTANT;
    private String service = ConstVal.TEMPLATE_SERVICE;
    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;
    private String mapper = ConstVal.TEMPLATE_MAPPER;
    private String xml = ConstVal.TEMPLATE_XML;
    private String controller = ConstVal.TEMPLATE_CONTROLLER;
    private AutoGenerator autoGenerator;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }

    public AutoGenerator backGenerator() {
        return autoGenerator;
    }

    /**
     * 不生成Controller
     *
     * @return TemplatePaths
     */
    public TemplatePaths excludeController() {
        return setController(null);
    }

    /**
     * 不生成Controller
     *
     * @return TemplatePaths
     */
    public TemplatePaths useEnumConstant() {
        return setConstant(ConstVal.TEMPLATE_ENTITY_CONSTANT_ENUM);
    }

    public TemplatePaths computePath(String path, BiFunction<TemplatePaths, String, TemplatePaths> setFunc) {
        return StringUtils.isNotEmpty(path) ? setFunc.apply(this, StringUtils.substringBefore(path, ".ftl")) : this;
    }

    public TemplatePaths parseTemplatePath(TemplateConfig templateConfig) {
        if (templateConfig == null) {
            return this;
        }
        computePath(templateConfig.getEntity(), TemplatePaths::setEntity);
        computePath(templateConfig.getConstant(), TemplatePaths::setConstant);
        computePath(templateConfig.getDao(), TemplatePaths::setMapper);
        computePath(templateConfig.getXml(), TemplatePaths::setXml);
        computePath(templateConfig.getService(), TemplatePaths::setService);
        computePath(templateConfig.getServiceImpl(), TemplatePaths::setServiceImpl);
        computePath(templateConfig.getController(), TemplatePaths::setController);
        excludeIf(templateConfig.getExcludeEntity(), TemplatePaths::setEntity, null);
        excludeIf(templateConfig.getExcludeConstant(), TemplatePaths::setConstant, null);
        excludeIf(templateConfig.getExcludeDao(), TemplatePaths::setMapper, null);
        excludeIf(templateConfig.getExcludeXml(), TemplatePaths::setXml, null);
        excludeIf(templateConfig.getExcludeService(), TemplatePaths::setService, null);
        excludeIf(templateConfig.getExcludeServiceImpl(), TemplatePaths::setServiceImpl, null);
        excludeIf(templateConfig.getExcludeController(), TemplatePaths::setController, null);
        return this;
    }

    private <T> TemplatePaths excludeIf(boolean isExclude, BiFunction<TemplatePaths, T, TemplatePaths> setFunc, T t) {
        return isExclude ? setFunc.apply(this, t) : this;
    }

}
