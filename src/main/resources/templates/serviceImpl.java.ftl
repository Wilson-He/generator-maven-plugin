package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**<#if table.comment??>${"\n"} * <p>
 * ${entity}-${table.comment!}业务接口
 * </p>
 * </#if>
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} implements ${table.serviceName}{
    @Resource
    private ${table.mapperName} ${table.mapperName?uncap_first};

    @Override
    public ${table.idType} insert(${entity} ${entity?uncap_first}){
        ${table.mapperName?uncap_first}.insert(${entity?uncap_first});
        return ${entity?uncap_first}.getId();
    }

    @Override
    public boolean updateById(${entity} ${entity?uncap_first}){
        return ${table.mapperName?uncap_first}.updateById(${entity?uncap_first}) > 0;
    }

    @Override
    public ${entity} findById(${table.idType} id){
        return ${table.mapperName?uncap_first}.selectById(id);
    }

    @Override
    public boolean deleteById(${table.idType} id){
        return ${table.mapperName?uncap_first}.deleteById(id) > 0;
    }
    </#if>
}
