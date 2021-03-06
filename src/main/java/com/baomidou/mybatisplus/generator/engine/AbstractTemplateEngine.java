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
package com.baomidou.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.PackageHelper;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.TemplatePaths;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import freemarker.template.TemplateException;
import io.github.generator.ExtendTemplateConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 模板引擎抽象类
 *
 * @author hubin
 * @since 2018-01-10
 */
@Data
@Accessors(chain = true)
public abstract class AbstractTemplateEngine {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTemplateEngine.class);
    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;
    private File resourcesDir;
    private List<String> customTemplates = new ArrayList<>();

    /**
     * 模板引擎初始化
     *
     * @return this
     */
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) throws IOException {
        this.configBuilder = configBuilder;
        return this;
    }

    /**
     * 输出 java mapperXml 文件
     *
     * @return this
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            String excludeKeywords = configBuilder.getStrategyConfig().getExcludeKeywordsPattern();
            String includeKeywords = configBuilder.getStrategyConfig().getIncludeKeywordsPattern();
            Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
            TemplatePaths template = getConfigBuilder().getTemplate();
            InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
            final boolean hasInjectionConfig = injectionConfig != null;
            List<FileOutConfig> focList = null;
            if (hasInjectionConfig) {
                injectionConfig.initMap();
                focList = injectionConfig.getFileOutConfigList();
            }
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                objectMap.put("cfg", injectionConfig != null ? injectionConfig.getMap() : null);
                // 自定义内容
                if (CollectionUtils.isNotEmpty(focList)) {
                    for (FileOutConfig foc : focList) {
                        if (isCreate(foc.outputFile(tableInfo))) {
                            writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                String tableName = tableInfo.getName();
                final boolean isIncludeKeywords = includeKeywords == null || StringUtils.matches(includeKeywords, tableName);
                final boolean isExcludeKeywords = excludeKeywords == null || StringUtils.matches(includeKeywords, tableName);
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(entityFile)) {
                        writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                    }
                }
                // constantPath
                final boolean isWriteConstant = tableInfo.isHasEnums() && null != pathInfo.get(ConstVal.CONSTANT_PATH);
                write(isWriteConstant, pathInfo, objectMap, ConstVal.CONSTANT_PATH, tableInfo.getConstantName(), tableInfo, template.getConstant());
                // MpMapper.java
                final boolean isWriteMapper = null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH);
                write(isWriteMapper, pathInfo, objectMap, ConstVal.MAPPER_PATH, tableInfo.getMapperName(), tableInfo, template.getMapper());
                // MpMapper.mapperXml
                final boolean isWriteXml = null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH);
                writeXml(isWriteXml, pathInfo, objectMap, ConstVal.XML_PATH, tableInfo.getXmlName(), tableInfo, template.getXml());
                // IMpService.java
                final boolean isWriteService = isIncludeKeywords && isExcludeKeywords && null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH);
                write(isWriteService, pathInfo, objectMap, ConstVal.SERVICE_PATH, tableInfo.getServiceName(), tableInfo, template.getService());
                // MpServiceImpl.java
                final boolean isWriteServiceImpl = isIncludeKeywords && isExcludeKeywords && null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH);
                write(isWriteServiceImpl, pathInfo, objectMap, ConstVal.SERVICE_IMPL_PATH, tableInfo.getServiceImplName(), tableInfo, template.getServiceImpl());
                // MpController.java
                final boolean isWriteController = isIncludeKeywords && isExcludeKeywords
                        && null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH);
                write(isWriteController, pathInfo, objectMap, ConstVal.CONTROLLER_PATH, tableInfo.getControllerName(), tableInfo, template.getController());
                if (configBuilder.getTemplateConfig() != null) {
                    List<ExtendTemplateConfig> templateConfigs = configBuilder.getTemplateConfig().getCustoms();
                    templateConfigs.forEach(e -> {
                        try {
                            write(isIncludeKeywords && isExcludeKeywords, pathInfo,
                                    objectMap, e.getLayerName(), entityName + e.getLayerName(), tableInfo,
                                    e.getPath().endsWith(".ftl") ? e.getPath().replace(".ftl", "") : e.getPath());
                        } catch (Exception ex) {
                            logger.error("自定义模板文件生成失败", ex);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    /**
     * 文件输出
     *
     * @param isWrite
     * @param pathInfo
     * @param objectMap
     * @param pathConst
     * @param fileName
     * @param tableInfo
     * @param templatePath
     * @throws Exception
     */
    private void write(boolean isWrite, Map<String, String> pathInfo, Map<String, Object> objectMap, String pathConst,
                       String fileName, TableInfo tableInfo, String templatePath) throws Exception {
        if (isWrite) {
            String file = String.format((pathInfo.get(pathConst) + File.separator + fileName + suffixJavaOrKt()), tableInfo.getEntityName());
            if (isCreate(file)) {
                writer(objectMap, templateFilePath(templatePath), file);
            }
        }
    }

    private void writeXml(boolean isWrite, Map<String, String> pathInfo, Map<String, Object> objectMap, String pathConst,
                          String fileName, TableInfo tableInfo, String templatePath) throws Exception {
        if (isWrite) {
            String file = String.format((pathInfo.get(pathConst) + File.separator + fileName + ConstVal.XML_SUFFIX), tableInfo.getEntityName());
            if (isCreate(file)) {
                writer(objectMap, templateFilePath(templatePath), file);
            }
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws IOException, TemplateException;

    /**
     * 处理输出目录
     *
     * @return this
     */
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            File dir = new File(value);
            if ((!dir.exists() && dir.mkdirs())) {
                logger.debug("创建目录： [{}]", value);
            }
        });
        return this;
    }

    /**
     * 渲染对象 MAP 信息
     *
     * @param tableInfo 表信息对象
     * @return ignore
     */
    private Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap = new HashMap<>(30);
        ConfigBuilder config = getConfigBuilder();
        if (config.getStrategyConfig().isControllerMappingHyphenStyle()) {
            objectMap.put("controllerMappingHyphenStyle", config.getStrategyConfig().isControllerMappingHyphenStyle());
            objectMap.put("controllerMappingHyphen", StringUtils.camelToHyphen(tableInfo.getEntityPath()));
        }
        objectMap.put("restControllerStyle", config.getStrategyConfig().isRestControllerStyle());
        objectMap.put("config", config);
        objectMap.put("package", config.getPackageInfo());
        GlobalConfig globalConfig = config.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("idType", globalConfig.getIdType() == null ? null : globalConfig.getIdType().toString());
        objectMap.put("logicDeleteFieldName", config.getStrategyConfig().getLogicDeleteFieldName());
        objectMap.put("jsonIgnoreFields", config.getStrategyConfig().getJsonIgnoreFields());
        objectMap.put("versionFieldName", config.getStrategyConfig().getVersionFieldName());
        objectMap.put("activeRecord", globalConfig.isActiveRecord());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger2", globalConfig.isSwagger2());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("table", tableInfo);
        objectMap.put("enableCache", globalConfig.isEnableCache());
        objectMap.put("baseResultMap", globalConfig.isBaseResultMap());
        objectMap.put("baseColumnList", globalConfig.isBaseColumnList());
        objectMap.put("entity", tableInfo.getEntityName());
        objectMap.put("entitySerialVersionUID", config.getStrategyConfig().isEntitySerialVersionUID());
        objectMap.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
        objectMap.put("entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel());
        objectMap.put("entityLombokModel", config.getStrategyConfig().isEntityLombokModel());
        objectMap.put("entityBooleanColumnRemoveIsPrefix", config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
        objectMap.put("superEntityClass", getSuperClassName(config.getSuperEntityClass()));
        objectMap.put("superMapperClassPackage", config.getSuperMapperClass());
        objectMap.put("superMapperClass", getSuperClassName(config.getSuperMapperClass()));
        objectMap.put("superServiceClassPackage", config.getSuperServiceClass());
        objectMap.put("superServiceClass", getSuperClassName(config.getSuperServiceClass()));
        objectMap.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
        objectMap.put("superServiceImplClass", getSuperClassName(config.getSuperServiceImplClass()));
        objectMap.put("superControllerClassPackage", config.getSuperControllerClass());
        objectMap.put("superControllerClass", getSuperClassName(config.getSuperControllerClass()));
        return objectMap;
    }


    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isEmpty(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);


    /**
     * 检测文件是否存在
     *
     * @param filePath
     * @return 文件是否存在
     */
    private boolean isCreate(String filePath) {
        // 自定义判断
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            PackageHelper.mkDir(file.getParentFile());
        }
        return !exist || getConfigBuilder().getGlobalConfig().isFileOverride();
    }

    /**
     * 文件后缀
     *
     * @return 文件后缀
     */
    private String suffixJavaOrKt() {
        return getConfigBuilder().getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }

}
