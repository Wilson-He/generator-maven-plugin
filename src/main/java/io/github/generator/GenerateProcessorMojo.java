package io.github.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.DefaultGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Wilson
 * @date 2019/7/30
 * @phase generate-sources
 * @requiresDependencyResolution compile
 **/
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
@Slf4j
public class GenerateProcessorMojo extends AbstractMojo {

    @Parameter(defaultValue = "target/generated-sources/")
    private File outputDirectory;
    @Parameter(name = "basePackage", required = true)
    private String basePackage;
    /**
     * 数据库配置[url,username,password,driverType]
     */
    @Parameter
    private DataSourceProperties dataSource;
    @Parameter
    private TemplateConfig templates;
    /**
     * 逻辑删除属性名称
     */
    @Parameter
    private String logicDeleteFieldName;
    @Parameter
    private String author;
    @Parameter
    private String idType;
    @Parameter
    private String javaIdType;
    /**
     * entity父类，带包名
     */
    @Parameter
    private String superEntityClass;
    /**
     * 根据关键字排除生成表名含关键字的Service、Controller,
     */
    @Parameter
    private String[] upstreamExclusions;
    /**
     * 根据关键字生成表名含关键字的Service、Controller,
     */
    @Parameter
    private String[] upstreamInclusions;
    /**
     * 生成含指定正则的文件
     */
    @Parameter
    private String[] inclusions;
    /**
     * 生成前是否清空当前模块的target目录
     */
    @Parameter(defaultValue = "false")
    private Boolean isCleanBefore;
    /**
     * 生成文件是否带swagger注解
     */
    @Parameter(defaultValue = "false")
    private Boolean useSwagger;
    /**
     * 不生成含指定正则的文件
     */
    @Parameter
    private String[] exclusions;
    @Parameter
    private String[] tablePrefix;
    private static final String RESOURCE_PATH = "/src/main/resources";

    @Override
    public void execute() {
        MavenProject mavenProject = (MavenProject) getPluginContext().get("project");
        File basedir = mavenProject.getBasedir();
        try {
            if (isCleanBefore) {
                File targetDir = new File(basedir + File.separator + "target");
                if (targetDir.exists() && targetDir.isDirectory()) {
                    FileUtils.cleanDirectory(targetDir);
                }
            }
            DefaultGeneratorConfig.defaultAutoGenerator(basePackage, dataSource.toDataSourceConfig())
                    .getGlobalConfig()
                    .initName(templates)
                    .setBaseColumnList(true)
                    .setSwagger2(useSwagger)
                    .setIdType(Optional.ofNullable(idType).map(IdType::valueOf).orElse(null))
                    .setJavaIdType(javaIdType)
                    .setAuthor(Optional.ofNullable(author).orElse(""))
                    .setOutputDir(outputDirectory.getAbsolutePath())
                    .backGenerator()
                    .getStrategy()
                    .setInclude(inclusions)
                    .setExclude(exclusions)
                    .setTablePrefix(tablePrefix)
                    .includeKeywords(upstreamInclusions)
                    .excludeKeywords(upstreamExclusions)
                    .setSuperEntityClass(superEntityClass)
                    .setInclude()
                    .setLogicDeleteFieldName(logicDeleteFieldName)
                    .includeKeywords()
                    .backGenerator()
                    .getTemplatePaths()
                    .parseTemplatePath(templates)
                    .backGenerator()
                    .execute(Optional.ofNullable(templates)
                            .map(e -> e.setResourcesPath(new File(basedir, RESOURCE_PATH)))
                            .orElse(new TemplateConfig()));
        } catch (IOException e) {
            log.error("生成错误", e);
        }
        log.info("generate finish");
    }

}
