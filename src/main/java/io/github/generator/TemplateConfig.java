package io.github.generator;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义模板路径
 *
 * @author Wilson
 * @date 2019/7/31
 **/
@Data
@Accessors(chain = true)
public class TemplateConfig {
    private String entityPattern;
    private String constantPattern;
    private String daoPattern;
    private String xmlPattern;
    private String servicePattern;
    private String serviceImplPattern;
    private String controllerPattern;
    private String entityPath;
    private String constantPath;
    private String daoPath;
    private String xmlPath;
    private String servicePath;
    private String serviceImplPath;
    private String controllerPath;
    private Boolean excludeEntity = false;
    private Boolean excludeConstant = false;
    private Boolean excludeDao = false;
    private Boolean excludeXml = false;
    private Boolean excludeService = false;
    private Boolean excludeServiceImpl = false;
    private Boolean excludeController = true;
    private List<ExtendTemplateConfig> customs = new ArrayList<>();
    private File resourcesPath;

    public List<String> templatePaths() {
        List<String> paths = new ArrayList<>();
        addPath(paths, entityPath)
                .addPath(paths, constantPath)
                .addPath(paths, xmlPath)
                .addPath(paths, servicePath)
                .addPath(paths, serviceImplPath)
                .addPath(paths, serviceImplPath)
                .addPath(paths, controllerPath);
        return paths;
    }

    private TemplateConfig addPath(List<String> paths, String path) {
        if (path == null) {
            return this;
        }
        paths.add(path);
        return this;
    }
}
