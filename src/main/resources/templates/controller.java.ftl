package ${package.Controller};

import ${package.Service}.${table.serviceName};
<#if swagger2>
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
</#if>
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import javax.annotation.Resource;

/**
 * <p>
 * <#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if><#else >${table.comment}控制器</#if>
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")<#if swagger2>
 @Api(tags = "<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if><#else >${table.comment}控制器</#if>")</#if>
<#if kotlin>
public class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>
    @Resource
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @PostMapping("/")
    public Object add(@Validated @RequestBody Object vo) {
        return null;
    }

    @GetMapping("/")
    <#if swagger2>
    @ApiOperation("根据id查询<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if></#if>")
    </#if>
    public Object get(<#if swagger2>@ApiParam(name = "id", value = "<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","")}<#else>${table.comment}</#if></#if>id") </#if>@RequestParam String id) {
        return null;
    }

    @GetMapping("/page")
    <#if swagger2>
    @ApiOperation("分页查询<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if></#if>")
    </#if>
    public Object page(<#if swagger2>@ApiParam(name = "page", value = "页码", defaultValue = "1") </#if>@RequestParam(defaultValue = "1") Integer page,
                       <#if swagger2>@ApiParam(name = "size", value = "每页返回数", defaultValue = "15") </#if>@RequestParam(defaultValue = "15") Integer size) {
        return null;
    }

    @PutMapping("/")
    <#if swagger2>
    @ApiOperation("根据id更新<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if></#if>")
    </#if>
    public Object update(@Validated @RequestBody Object vo) {
        return null;
    }

    @DeleteMapping("/")
    <#if swagger2>
    @ApiOperation("根据id删除<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if></#if>")
    </#if>
    public Object delete(<#if swagger2>@ApiParam(name = "id", value = "<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","")}<#else>${table.comment}</#if></#if>id") </#if>@RequestParam String id) {
        return null;
    }
}
</#if>
