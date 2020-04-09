package ${package.Constant};

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ${entity}常量类
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${entity}Constant {

<#list table.fields as field>
    <#if field.constantField>
    /**
     * ${field.comment}
     */
    @AllArgsConstructor
    @Getter
    enum ${field.propertyName?capFirst} {
    <#list field.fieldEnums as fieldEnum>
        <#if fieldEnum_has_next>
        ${fieldEnum.key}(${fieldEnum.value}, "${fieldEnum.comment}"),
        <#else >
        ${fieldEnum.key}(${fieldEnum.value}, "${fieldEnum.comment}");
        </#if>
    </#list>
        private final ${field.columnType.type} value;
        private final String comment;
        private static final Map<${field.columnType.type}, String> MAP = Collections.unmodifiableMap(Arrays.stream(${field.propertyName?capFirst}.values())
                .collect(Collectors.toMap(${field.propertyName?capFirst}::getValue, ${field.propertyName?capFirst}::getComment)));

        public static String comment(${field.columnType.type} value) {
            return MAP.get(value);
        }

        public boolean equalsVal(${field.columnType.type} val) {
            return this.value.equals(val);
        }
    }

    </#if>
</#list>
}
