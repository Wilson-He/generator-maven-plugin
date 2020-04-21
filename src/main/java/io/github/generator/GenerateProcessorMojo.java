package io.github.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.DefaultGeneratorConfig;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
@Slf4j
public class GenerateProcessorMojo extends AbstractMojo {
    /**
     * 文件输出路径,建议使用默认, 默认:target/generated-sources
     */
    @Parameter(defaultValue = "target/generated-sources/")
    private File outputDirectory;
    /**
     * 生成文件的基包
     */
    @Parameter(name = "basePackage", required = true)
    private String basePackage;
    /**
     * 数据库相关配置[url,username,password,driverType,bitToInteger,commentPattern,excludeConstantFields]<br>
     * <li>url: 必须</li>
     * <li>username: 必须</li>
     * <li>password: 必须</li>
     * <li>driverType - 数据库驱动类型,取值范围:[MYSQL,ORACLE,PROGRE_SQL],不配置则默认MYSQL</li>
     * <li>bitToInteger - 是否将数据库bit类型逆向生成为integer类型,默认true</li>
     * <li>commentPattern - 注释样式,不配置则不生成constant,如: key:value:comment  key:value-comment</li>
     * <li>excludeConstantFields - 不生成指定字段常量类,如: is_delete,state</li>
     * <br>
     * Demo:<pre>
     * &lt;dataSource>
     *     &lt;url>jdbc:mysql://localhost:3306/wilson&lt;/url>
     *     &lt;username>root&lt;/username>
     *     &lt;password>tiger&lt;/password>
     * &lt;/dataSource>
     * </pre>
     *
     */
    @Parameter(required = true)
    private DataSourceProperties dataSource;

    /**
     * 模板文件相关配置, 可不配使用默认选项, 可配选项如下<br>
     * <li>entityPattern: 设置生成entity文件名范式,如: %sDO, 则表user_base生成的文件名为UserBaseDO</li>
     * <li>constantPattern, daoPattern, xmlPattern, servicePattern, serviceImplPattern, controllerPattern类同entityPattern</li>
     * <li>entityPath: resources下的自定义entity模板相对路径,不设置则使用默认模板/li>
     * <li>constantPath,daoPath, xmlPath, servicePath, controllerPath类同entityPath</li>
     * <li>excludeEntity: 不生成entity, 默认false</li>
     * <li>excludeConstant, excludeDao, excludeXml, excludeService, excludeServiceImpl类同excludeEntity</li>
     * <li>excludeController: 默认true, 不生成controller</li>
     * <li>customs: 自定义扩展模板配置列表,默认null
     * <ul>
     * <li>layerName: 所属层名,首字母大写,将作为ftl模板文件所在包的路径变量,如：Manager</li>
     * <li>path: 模板文件在resources下的相对路径</li>
     * <li>subPackage: 所在子包，如: manager, 当layManager=Manager时则ftl文件可通过${package.Manager}获取包路径xxx.xxx.xxx.manager</li>
     * </ul>
     * 完整例子：<br><pre>
     *   &lt;templates>
     *       &lt;!--    生成controller    -->
     *      &lt;excludeController>false&lt;/excludeController>
     *      &lt;customs>
     *        &lt;param>
     *            &lt;!-- 根据resources/templates/manager.ftl模板在{basePackage}.manager包下生成实体对应的Manager -->
     *            &lt;path>/templates/manager.ftl&lt;/path>
     *            &lt;subPackage>manager&lt;/subPackage>
     *            &lt;layerName>Manager&lt;/layerName>
     *        &lt;/param>
     *      &lt;/customs>&lt;
     *   &lt;/templates>
     *   <pre>
     * </li>
     */
    @Parameter
    private TemplateConfig templates;
    /**
     * 逻辑删除属性名称
     */
    @Parameter
    private String logicDeleteFieldName;
    /**
     * 生成文件的author注释值
     */
    @Parameter
    private String author;
    /**
     * 设置实体id @TableId注解IdType, 不配置则不生成实体id的@TableId注解, 默认空不生成@TableId, 可设值:<br>
     * <li>AUTO: 数据库ID自增</li>
     * <li>NONE: 该类型为未设置主键类型</li>
     * <li>INPUT: 手动设置ID</li>
     * <li>ID_WORKER: 雪花算法生成全局唯一ID </li>
     * <li>UUID: 全局唯一ID (UUID)</li>
     * <li>ID_WORKER_STR: 字符串全局唯一ID (idWorker 的字符串表示)</li>
     */
    @Parameter
    private String idType;
    /**
     * id类型(如Long),设置后生成的service将自动生成对应的findById方法,如: Long
     */
    @Parameter
    private String crudIdType;
    /**
     * 在指定字段上添加jackson @JsonIgnore注解,多个字段用逗号分隔,如: password,salt,isDelete
     */
    @Parameter
    private String jsonIgnores;
    /**
     * 生成的entity默认父类,全名(带包名),如:  io.github.entity.BaseDO
     */
    @Parameter
    private String superEntityClass;
    /**
     * 数组类型，根据关键字排除生成表名含关键字的Service、Controller,如:relation,admin 则不生成表名含relation或admin的Service与Controller,两种配置模式：<br>
     * <li>逗号分隔: &lt;upstreamExclusions&gt;user\w+,product\w+&lt;/upstreamExclusions&gt;<br></li>
     * <li>子标签配置：<pre>
     *  &lt;upstreamExclusions>
     *        &lt;upstreamExclusion>user\w+&lt;/upstreamExclusion>
     *        &lt;upstreamExclusion>product\w+&lt;/upstreamExclusion>
     *  &lt;/upstreamExclusions></pre>
     */
    @Parameter
    private String[] upstreamExclusions;
    /**
     * 数组类型，根据关键字生成表名含关键字的Service、Controller,如:relation,admin 则只生成表名含relation或admin的Service与Controller,两种配置模式：<br>
     * <li>逗号分隔: &lt;upstreamInclusions&gt;user\w+,product\w+&lt;/upstreamInclusions&gt;<br></li>
     * <li>子标签配置：<pre>
     *  &lt;upstreamInclusions>
     *        &lt;upstreamInclusion>user\w+&lt;/upstreamInclusion>
     *        &lt;upstreamInclusion>product\w+&lt;/upstreamInclusion>
     *  &lt;/upstreamInclusions></pre>
     */
    @Parameter
    private String[] upstreamInclusions;
    /**
     * 数组类型，生成含指定正则的文件,两种配置模式：<br>
     * <li>逗号分隔: &lt;inclusions&gt;user\w+,product\w+&lt;/inclusions&gt;<br></li>
     * <li>子标签配置：<pre>
     *  &lt;inclusions>
     *        &lt;inclusion>user\w+&lt;/inclusion>
     *        &lt;inclusion>product\w+&lt;/inclusion>
     *  &lt;/inclusions></pre>
     */
    @Parameter
    private String[] inclusions;
    /**
     * 数组类型，不生成含指定正则的文件,两种配置模式：<br>
     * <li>逗号分隔: &lt;exclusion&gt;user\w+,product\w+&lt;/exclusion&gt;<br></li>
     * <li>子标签配置：<pre>
     *  &lt;exclusions>
     *        &lt;exclusion>user\w+&lt;/exclusion>
     *        &lt;exclusion>product\w+&lt;/exclusion>
     *  &lt;/exclusions></pre>
     */
    @Parameter
    private String[] exclusions;
    /**
     * 生成前是否清空当前模块的target目录,默认false
     */
    @Parameter(defaultValue = "false")
    private Boolean isCleanBefore;
    /**
     * 生成文件是否带swagger注解
     */
    @Parameter(defaultValue = "false")
    private Boolean useSwagger;
    /**
     * 是否使用enum常量模板
     */
    @Parameter(defaultValue = "false")
    private Boolean useEnumTemplate;
    /**
     * 数组类型,表名前缀,如tb_user不生成tb_前缀则设为tb_,非必须,多个则用逗号分","隔
     */
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
                    // 全局配置
                    .getGlobalConfig()
                    .initName(templates)
                    .setBaseColumnList(true)
                    .setSwagger2(useSwagger)
                    .setIdType(Optional.ofNullable(idType).map(IdType::valueOf).orElse(null))
                    .setCrudIdType(crudIdType)
                    .setAuthor(Optional.ofNullable(author).orElse(""))
                    .setOutputDir(outputDirectory.getAbsolutePath())
                    .backGenerator()
                    // 策略配置
                    .getStrategy()
                    .setInclusions(inclusions)
                    .setExclusions(exclusions)
                    .setTablePrefix(tablePrefix)
                    .includeKeywords(upstreamInclusions)
                    .excludeKeywords(upstreamExclusions)
                    .setJsonIgnoreFields(jsonIgnores == null ? null : Lists.newArrayList(jsonIgnores.split(",")))
                    .setSuperEntityClass(superEntityClass)
                    .setLogicDeleteFieldName(logicDeleteFieldName)
                    .backGenerator()
                    // 模板路径配置
                    .getTemplatePaths()
                    .parseTemplatePath(templates)
                    .useEnumTemplate(useEnumTemplate)
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
