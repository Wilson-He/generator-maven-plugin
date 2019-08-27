package ${package.Controller};


import io.swagger.annotations.ApiParam;
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

/**
 * <p>
 * <#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","控制器")}<#else>${table.comment}</#if></#if>
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
    public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
    public class ${table.controllerName} {
    </#if>
    @PostMapping("/")
    public Object add(@Validated @RequestBody Object vo){
        return null;
    }

    @GetMapping("/")
    public Object get(@ApiParam(name = "id", value = "<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","")}<#else>${table.comment}</#if></#if>id") @RequestParam String id){
        return null;
    }

    @GetMapping("/page")
    public Object page(@ApiParam(name = "page", value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
    @ApiParam(name = "size", value = "每页返回数", defaultValue = "15") @RequestParam(defaultValue = "15") Integer size){
        return null;
    }

    @PutMapping("/")
    public Object update(@Validated @RequestBody Object vo){
        return null;
    }

    @DeleteMapping("/")
    public Object delete(@ApiParam(name = "id", value = "<#if table.comment??><#if table.comment?ends_with("表")>${table.comment?replace("表","")}<#else>${table.comment}</#if></#if>id") @RequestParam String id){
        return null;
    }
}
</#if>
