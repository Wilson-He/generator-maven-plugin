package com.baomidou.mybatisplus.generator.config.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TableFieldComment-字段注释解析类
 *
 * @author Wilson
 * @since 18-7-12
 */
@Setter
@Getter
@ToString
public class TableFieldComment {
    /**
     * 常量名,如String ENABLE = 1,则key = 1
     */
    private String key;
    private String value;
    private String originValue;
    /**
     * 枚举注释
     */
    private String comment;

    public TableFieldComment(String key, String value, String comment, String clazz) {
        this.key = key;
        this.originValue = value;
        this.value = String.class.getSimpleName().equals(clazz) ? "\"" + value + "\"" : value;
        this.comment = comment;
    }
}
