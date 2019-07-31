package ${package.Constant};

import com.google.common.collect.ImmutableMap;

import java.util.Map;

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
    interface ${field.propertyName?capFirst} {
    <#list field.fieldEnums as fieldEnum>
        /**
         * ${fieldEnum.comment}
         */
        ${field.columnType.type} ${fieldEnum.key} = ${fieldEnum.value};
    </#list>
    <#if field.fieldEnums?size lt 6>
        Map<${field.columnType.type}, String> MAP = ImmutableMap.of(
        <#list field.fieldEnums as fieldEnum>
            <#if fieldEnum_has_next>
                ${fieldEnum.key}, "${fieldEnum.comment}",
            <#else >
                ${fieldEnum.key}, "${fieldEnum.comment}");
            </#if>
        </#list>
    <#else>
        Map<Object, Object> MAP = ImmutableMap.builder()
        <#list field.fieldEnums as fieldEnum>
            <#if fieldEnum_has_next>
                .put(${fieldEnum.key}, "${fieldEnum.comment}")
            </#if>
        </#list>
        .build();
    </#if>
    }

    </#if>
</#list>
}
