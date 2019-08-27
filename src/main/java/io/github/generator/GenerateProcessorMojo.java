package io.github.generator;

import com.baomidou.mybatisplus.generator.DefaultGeneratorConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Wilson
 * @date 2019/7/30
 * @phase generate-sources
 * @requiresDependencyResolution compile
 **/
@Data
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
@Slf4j
public class GenerateProcessorMojo extends AbstractMojo {

    @Parameter(defaultValue = "target/generated-sources/")
    private File outputDirectory;
    @Parameter(name = "basePackage", required = true)
    private String basePackage;
    @Parameter
    private DataSourceProperties dataSource;
    @Parameter
    private TemplateConfig templates;
    /**
     * 逻辑删除属性名称
     */
    @Parameter
    private String logicDeleteFieldName;
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
            DefaultGeneratorConfig.defaultAutoGenerator(basePackage, dataSource.toDataSourceConfig())
                    .getGlobalConfig()
                    .initName(templates)
                    .setOutputDir(outputDirectory.getAbsolutePath())
                    .backGenerator()
                    .getStrategy()
                    .setInclude(inclusions)
                    .setExclude(exclusions)
                    .setTablePrefix(tablePrefix)
                    .includeKeywords(upstreamInclusions)
                    .excludeKeywords(upstreamExclusions)
                    .setInclude()
                    .setLogicDeleteFieldName(logicDeleteFieldName)
                    .includeKeywords()
                    .backGenerator()
                    .getTemplatePaths()
                    .parseTemplatePath(templates)
                    .backGenerator()
                    .execute(Optional.ofNullable(templates)
                            .map(e -> e.setResourcesPath(new File(basedir, RESOURCE_PATH)))
                            .orElse(null));
        } catch (IOException e) {
            log.error("生成错误", e);
        }
        log.info("generate finish");
    }
}
