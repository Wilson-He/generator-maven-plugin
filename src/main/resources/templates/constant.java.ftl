package ${package.Constant};

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * ${entity}常量类
 *
 * @author ${author}
 * @since ${date}
 */
public final class ${entity}Constant {

<#list table.fields as field>
    <#if field.constantField>
    <#if field.fieldEnums?size lt 6>
    private static final Map<${field.columnType.type}, String> ${field.propertyCamelName?upperCase}_MAP = ImmutableMap.of(
    <#list field.fieldEnums as fieldEnum>
        <#if fieldEnum_has_next>
            ${field.propertyName?capFirst}.${fieldEnum.key}, "${fieldEnum.comment}",
    <#else >
            ${field.propertyName?capFirst}.${fieldEnum.key}, "${fieldEnum.comment}");
    </#if>
</#list>
    <#else>
    private static final Map<Object, Object> ${field.propertyCamelName?upperCase}_MAP = ImmutableMap.builder()
    <#list field.fieldEnums as fieldEnum>
        <#if fieldEnum_has_next>
            .put(${field.propertyName?capFirst}.${fieldEnum.key}, "${fieldEnum.comment}")
        </#if>
    </#list>
            .build();
    </#if>
    </#if>
</#list>

<#list table.fields as field>
    <#if field.constantField>

    /**
     * ${field.excludeKeyComment}
     */
    public interface ${field.propertyName?capFirst} {
    <#list field.fieldEnums as fieldEnum>
        /**
         * ${fieldEnum.comment}
         */
        ${field.columnType.type} ${fieldEnum.key} = ${fieldEnum.value};
    </#list>
        String COMMENT = "${field.excludeKeyComment}";
    }
    </#if>
</#list>

<#list table.fields as field>
    <#if field.constantField>
    public static String get${field.propertyName?capFirst}Comment(${field.columnType.type} ${field.propertyName}) {
        return ${field.propertyCamelName?upperCase}_MAP.get(${field.propertyName});
    }

    </#if>
</#list>
}
