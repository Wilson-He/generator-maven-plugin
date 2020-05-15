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
    ${table.idType} insert(${entity} ${entity?uncap_first});

    boolean updateById(${entity} ${entity?uncap_first});

    ${entity} findById(${table.idType} id);

    boolean deleteById(${table.idType} id);
}
</#if>
