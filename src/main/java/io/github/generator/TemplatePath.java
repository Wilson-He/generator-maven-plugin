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
public class TemplatePath {
    private String entity;
    private String constant;
    private String service;
    private String serviceImpl;
    private String dao;
    private String xml;
    private String controller;
    private File resourcesPath;

    public List<String> templatePaths() {
        List<String> paths = new ArrayList<>();
        addPath(paths, entity)
                .addPath(paths, constant)
                .addPath(paths, xml)
                .addPath(paths, service)
                .addPath(paths, serviceImpl)
                .addPath(paths, controller);
        return paths;
    }

    private TemplatePath addPath(List<String> paths, String path) {
        if (path == null) {
            return this;
        }
        paths.add(path);
        return this;
    }
}
