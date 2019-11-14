package ${package.Service};

import ${package.Entity}.${entity};

/**<#if table.comment??>${"\n"} * <p>
 * ${entity}-${table.comment!}业务接口
 * </p>
 * </#if>
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} {
    <#if crudIdType??>
    ${crudIdType} insert(${entity} ${entity?uncap_first});

    boolean updateById(${entity} ${entity?uncap_first});

    ${entity} findById(${crudIdType} id);

    boolean deleteById(${crudIdType} id);
    </#if>
}
</#if>
