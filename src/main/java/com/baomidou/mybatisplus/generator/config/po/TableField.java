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

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import io.github.generator.ConstantCommentConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表字段信息
 *
 * @author YangHu
 * @since 2016-12-03
 */
@Data
@Accessors(chain = true)
@Slf4j
public class TableField {

    private boolean convert;
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String table;
    private String name;
    private String type;
    private String propertyName;
    private IColumnType columnType;
    private String comment;
    private String excludeKeyComment;
    /**
     * 字段枚举值
     */
    private List<TableFieldComment> fieldEnums;
    /**
     * 常量配置,如 ENABLE:1-可用,DISABLE:0-禁用
     */
    private String fieldEnumsString;
    private String fill;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    /**
     * 正则匹配常量注释范式: ({db_val}:val_comment-map.key), 例：(1:已删除-YES, 0:未删除-NO)
     */
    private static final String COMMENT_REGEX_SUFFIX = "((\\W+|\\w+):\\W+-[a-zA-Z]+,?)+";
    /**
     * 正则匹配常量注释范式: ({db_val}:val_comment), 例：(YES-已删除, NO-未删除)   \W+-\W+
     */
    private static final String COMMENT_REGEX_PURE = "((\\W+|\\w+)-(\\W+|\\w+,?)+)";
    private static final String COMMENT_REGEX = COMMENT_REGEX_PURE + "|" + COMMENT_REGEX_SUFFIX;

    public boolean isConstantField() {
        if (ConstantCommentConfig.getInstance() == null) {
            return false;
        }
        if (null == comment || !comment.matches(ConstantCommentConfig.getInstance().getRegexp())) {
            return false;
        }
        if (ConstantCommentConfig.EXCLUDE_CONSTANT_FIELDS != null && ConstantCommentConfig.EXCLUDE_CONSTANT_FIELDS.contains(name)) {
            return false;
        }
        if (!(comment.contains("(") && comment.endsWith(")"))) {
            return false;
        }
        fieldEnumsString = org.apache.commons.lang3.StringUtils.substringBetween(comment, "(", ")")
                .replaceAll("\\s", "");
        if (null == fieldEnums) {
            initCommentConstantList();
        }
        return !fieldEnums.isEmpty();
    }

    private void initCommentConstantList() {
        String clazz = columnType.getType();
        ConstantCommentConfig commentConfig = ConstantCommentConfig.getInstance();
        try {
            // key1:val1:comment1,key2:val2:comment2  key1:comment1 key2:comment2 key3:comment3
            fieldEnums = commentConfig.fetchCommentList(fieldEnumsString, clazz);
            excludeKeyComment = comment.substring(0, comment.indexOf("(") + 1) +
                    fieldEnums.stream()
                            .map(fieldEnum -> fieldEnum.getValue() + "-" + fieldEnum.getComment())
                            .reduce((a, b) -> a + "," + b)
                            .orElse("") + ")";
        } catch (Exception e) {
            String exceptionMsg = "%s.%s常量范式错误,请检查范式: %s";
            System.err.println(String.format(exceptionMsg, table, name, fieldEnumsString));
            log.error(String.format(exceptionMsg, table, name, fieldEnumsString), e);
        }
    }

    public TableField setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    protected TableField setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isEntityTableFieldAnnotationEnable()) {
            this.convert = true;
            return this;
        }
        if (strategyConfig.isCapitalModeNaming(name)) {
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (StringUtils.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!name.equals(propertyName)) {
                this.convert = true;
            }
        }
        return this;
    }

    public TableField setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
        return this;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }

    /**
     * 按JavaBean规则来生成get和set方法
     *
     * @return capitalName
     */
    public String getCapitalName() {
        if (propertyName.length() <= 1) {
            return propertyName.toUpperCase();
        }
        String setGetName = propertyName;
        if (DbColumnType.BASE_BOOLEAN.getType().equalsIgnoreCase(columnType.getType())) {
            setGetName = StringUtils.removeIsPrefixIfBoolean(setGetName, Boolean.class);
        }
        // 第一个字母 小写、 第二个字母 大写 ，特殊处理
        String firstChar = setGetName.substring(0, 1);
        if (Character.isLowerCase(firstChar.toCharArray()[0])
                && Character.isUpperCase(setGetName.substring(1, 2).toCharArray()[0])) {
            return firstChar.toLowerCase() + setGetName.substring(1);
        }
        return firstChar.toUpperCase() + setGetName.substring(1);
    }

    private static String[] substringsBetween(String str, String open, String close) {
        if (str != null && !StringUtils.isEmpty(open) && !StringUtils.isEmpty(close)) {
            int strLen = str.length();
            if (strLen == 0) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            } else {
                int closeLen = close.length();
                int openLen = open.length();
                List<String> list = new ArrayList();

                int end;
                for (int pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
                    int start = str.indexOf(open, pos);
                    if (start < 0) {
                        break;
                    }

                    start += openLen;
                    end = str.indexOf(close, start);
                    if (end < 0) {
                        break;
                    }

                    list.add(str.substring(start, end));
                }

                return list.isEmpty() ? null : list.toArray(new String[list.size()]);
            }
        } else {
            return null;
        }
    }
}
