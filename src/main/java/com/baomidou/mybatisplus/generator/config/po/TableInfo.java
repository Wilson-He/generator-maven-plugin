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
package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import io.github.generator.ConstantCommentConfig;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @since 2016/8/30
 */
@Data
@ToString(exclude = "hasEnums")
@Accessors(chain = true)
public class TableInfo {

    private final Set<String> importPackages = new HashSet<>();
    private boolean convert;
    private boolean hasEnums;
    private String name;
    private String comment;
    private String entityName;
    private String constantName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private List<TableField> fields;
    /**
     * 公共字段
     */
    private List<TableField> commonFields;
    private String fieldNames;
    private Boolean hasJsonIgnore;
    private StrategyConfig strategyConfig;

    public TableInfo setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    public String getIdType() {
        return fields != null ? fields.stream()
                .filter(field -> field.getName().equals("id"))
                .map(TableField::getPropertyType)
                .findFirst()
                .orElse("String") : "String";
    }

    public boolean isHasJsonIgnore() {
        return Optional.ofNullable(strategyConfig.getJsonIgnoreFields())
                .map(Collection::stream)
                .map(stream -> stream.anyMatch(ignoreField -> getFieldNames().contains(ignoreField)))
                .orElse(false);
    }

    protected TableInfo setConvert(StrategyConfig strategyConfig) {
//        StringUtils.repl
        this.strategyConfig = strategyConfig;
        if (strategyConfig.containsTablePrefix(name)) {
            // 包含前缀
            this.convert = true;
        } else if (strategyConfig.isCapitalModeNaming(name)) {
            // 包含
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (StringUtils.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!entityName.equalsIgnoreCase(name)) {
                this.convert = true;
            }
        }
        return this;
    }

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    public TableInfo setEntityName(StrategyConfig strategyConfig, String entityName) {
        this.entityName = entityName;
        this.setConvert(strategyConfig);
        return this;
    }

    public TableInfo setFields(List<TableField> fields) {
        if (CollectionUtils.isNotEmpty(fields)) {
            this.fields = fields;
            // 收集导入包信息
            for (TableField field : fields) {
                if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                    importPackages.add(field.getColumnType().getPkg());
                }
                if (field.isKeyFlag()) {
                    // 主键
                    if (field.isConvert() || field.isKeyIdentityFlag()) {
                        importPackages.add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
                    }
                    // 自增
                    if (field.isKeyIdentityFlag()) {
                        importPackages.add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
                    }
                } else if (field.isConvert()) {
                    // 普通字段
                    importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                }
                if (null != field.getFill()) {
                    // 填充字段
                    importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                    importPackages.add(com.baomidou.mybatisplus.annotation.FieldFill.class.getCanonicalName());
                }
                field.setTable(this.name);
            }
        }
        return this;
    }

    public TableInfo setImportPackages(String pkg) {
        importPackages.add(pkg);
        return this;
    }

    /**
     * 逻辑删除
     *
     * @return Boolean
     */
    public boolean isLogicDelete(String logicDeletePropertyName) {
        return fields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
    }

    /**
     * 转换filed实体为 mapperXml mapper 中的 base column 字符串信息
     *
     * @return fieldNames
     */
    public String getFieldNames() {
        if (StringUtils.isEmpty(fieldNames) && fields != null) {
            StringBuilder names = new StringBuilder();
            IntStream.range(0, fields.size()).forEach(i -> {
                TableField fd = fields.get(i);
                if (i == fields.size() - 1) {
                    names.append(fd.getName());
                } else {
                    names.append(fd.getName()).append(", ");
                }
            });
            fieldNames = names.toString();
        }
        return fieldNames;
    }

    public boolean isHasEnums() {
        this.hasEnums = ConstantCommentConfig.getInstance() != null
                && Optional.ofNullable(fields).map(e -> e.stream().anyMatch(TableField::isConstantField))
                .orElse(false);
        return hasEnums;
    }
}
