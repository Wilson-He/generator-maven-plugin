# generator-maven-plugin([版本更新信息](https://github.com/Wilson-He/generator-maven-plugin/blob/master/%E7%89%88%E6%9C%AC%E6%9B%B4%E6%96%B0%E4%BF%A1%E6%81%AF.md))

- ## 快速开始(mvn generator:generate)

      <plugin>
          <groupId>io.github.wilson-he</groupId>
          <artifactId>generator-maven-plugin</artifactId>
          <version>LATEST</version>
          <configuration>
              <basePackage>io.github.test</basePackage>
              <!-- 生成前是否清空target/generate-sources目录 -->
              <isCleanBefore>true</isCleanBefore>
              <dataSource>
                  <url><![CDATA[jdbc:mysql://localhost:3306/wilson?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false]]></url>
                  <username>root</username>
                  <password>tiger</password>
              </dataSource>
          </configuration>
      </plugin>

- ## 目前支持的配置项
  - outputDirectory: 输出绝对路径,默认生成到当前pom项目模块的target/generate-sources下
  - basePackage: 各层文件生成的基包[**必填项**]
  - isCleanBefore(boolean): 文件生成前是否清空当前模块下的target目录,默认false
  - useSwagger(boolean):生成文件是否带swagger注解,默认false
  - superEntityClass:entity父类，完整包路径.类名
  - author:文件author值,默认空
  - idType:String类型,用于设置实体id注解IdType,默认空-不生成mybatis-plus id注解,可取值如下：
    - AUTO:数据库ID自增
    - NONE:该类型为未设置主键类型
    - INPUT:用户输入ID,该类型可以通过自己注册自动填充插件进行填充
    - ID_WORKER:全局唯一ID(只有当插入对象ID为空，才自动填充)
    - UUID:全局唯一ID(只有当插入对象ID为空，才自动填充)
    - ID_WORKER_STR:字符串全局唯一ID(idWorker的字符串表示,只有当插入对象ID为空，才自动填充)
  - crudIdType:实体id类型，如Long
  - dataSource: 数据库配置[**必填项**]
    - url
    - username
    - password
    - driverType:数据库驱动类型,默认MYSQL(MYSQL,ORACLE,POSTGRE_SQL)
    - excludeConstantFields: 不生成指定字段常量类,如: is_delete,state
    - commentPattern: db字段注释中括号内的常量模板样式配置,key为常量的java变量名,value为变量名对应的常量值,comment为常量值注释,key、value、comment之间有且只有一个任意分隔符(逗号","除外),
    若db字段注释只需key、comment(key=value)但仍需生成常量类,则key、comment间的分割符需为commentPattern中key、value、comment之间的分隔符之一，用例如下：
      - commentPattern为key-value:comment,可匹配如下db字段注释
        - 数据库is_delete(bit)字段注释：删除标志量(YES-1:已删除,NO-1:未删除),生成java常量为 /\*\*已删除**/Integer YES = 1;/\*\*未删除**/Integer NO = 0;
        - 删除标志量(YES-已删除,NO-未删除),生成java常量为 /\*\*已删除**/String YES = "YES";/\*\*未删除**/String NO = "NO";
        - 删除标志量(YES:已删除,NO:未删除),生成java常量为 /\*\*已删除**/String YES = "YES";/\*\*未删除**/String NO = "NO";
      - commentPattern为key:value:comment,可匹配如下db字段注释
        - 数据库is_delete(bit)字段注释：删除标志量(YES:1:已删除,NO:1:未删除),生成java常量为 /\*\*已删除**/Integer YES = 1;/\*\*未删除**/Integer NO = 0;
        - 删除标志量(YES:已删除,NO:未删除),生成java常量为 /\*\*已删除**/String YES = "YES";/\*\*未删除**/String NO = "NO";
    - bitToInteger:数据库bit类型转为java Integer类型,默认true,false将使bit转Boolean
  - templates: 自定义模板配置、模板生成配置,不配置则使用默认模板配置生成
    - entityPath: resources下的自定义entity模板相对路径
    - constantPath
    - daoPath
    - xmlPath
    - servicePath
    - serviceImplPath
    - controllerPath
    - entityPattern: entity名称样式，如:%sDO将以{tableName}DO命名生成
    - daoPattern
    - xmlPattern
    - servicePattern
    - serviceImplPattern
    - controllerPattern
    - excludeEntity: true/false,是否生成entity模板
    - excludeXxx: 同excludeEntity
    - excludeController: 默认true,其它层默认false
    - customs: 对象列表,自定义模板
      - layerName: 模板分层名(请勿使用已存在ftl变量)
      - subPackage: 所在子包
      - path: 模板在resources下的相对路径
  - exclusions: 字符串数组,不生成表名含数组内字符串的所有文件,默认空
  - inclusions: 字符串数组,只生成表名含数组内字符串的所有文件,默认空
  - upstreamExclusions: 字符串数组,不生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - upstreamInclusions: 字符串数组,只生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - logicDeleteFieldName: 全局逻辑删除字段名,默认空
  - tablePrefix: 字符串数组,表名前缀,默认空
  - jsonIgnores:在指定字段上添加jackson @JsonIgnore注解,多个字段用逗号分隔,例: password,salt,isDelete
    
- ## ftl模板常用变量
  - package: 包路径变量,${package.xxx}
    - Entity: entity完整包路径,如io.github.test.entity
    - Mapper
    - Service
    - ServiceImpl
    - Controller
  - table: 表信息变量,${table.xxx}
    - comment: 注释
    - name: 表名
    - entityName: 当前表对应生成的entity文件名,如UserBase.java
    - constantName: 当前表对应生成的constant文件名,如UserBaseConstant.java
    - mapperName: 同上
    - xmlName: 同上
    - serviceName: 同上
    - serviceImplName: 同上
    - controllerName: 同上
    - fields: 字段列表,模板遍历<#list table.fields as field>,以下为field变量
      - table: 表名
      - propertyType: 属性类型
      - propertyName: 属性名
      - name: db字段名
      - comment: 字段注释
      
  - ## 详细配置例子
    - pom配置
  
          <plugin>
              <groupId>io.github.wilson-he</groupId>
              <artifactId>generator-maven-plugin</artifactId>
              <version>LATEST</version>
              <configuration>
                  <basePackage>io.github.test</basePackage>
                  <dataSource>
                      <url><![CDATA[jdbc:mysql://localhost:3306/wilson?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false]]></url>
                      <username>root</username>
                      <password>tiger</password>
                      <driverType>MYSQL</driverType>
                      <commentPattern>key:value-comment</commentPattern>
                  </dataSource>
                  <exclusions>
                      <!-- 不生成表名含relation字符串的所有文件,默认空 -->
                      <param>relation</param>
                  </exclusions>
                  <upstreamExclusions>
		              <!-- 不生成表名含一下关键字的自定义模板(如manager) 、service、serviceImpl、controller-->
		              <!-- <upstreamExclusion>detail</upstreamExclusion>效果同param-->
                      <param>detail</param>
                  </upstreamExclusions>
                  <templates>
                      <entityPath>/templates/custom-entity.java.ftl</entity>
                      <excludeServiceImpl>true</excludeServiceImpl>
                      <customs>
                          <param>
                              <path>/templates/manager.ftl</path>
                              <subPackage>manager</subPackage>
                              <layerName>Manager</layerName>
                          </param>
                      </customs>
                  </templates>
              </configuration>
              <executions>
                  <execution>
                      <goals>
                          <goal>generate</goal>
                      </goals>
                  </execution>
              </executions>
          </plugin>
     - /src/resources/templates/manager.ftl

           package ${package.Manager};
       
           /**<#if table.comment??>${"\n"} * <p>
            * ${entity}-${table.comment!}业务接口
            * </p>
            * </#if>
            * @author ${author}
            * @since ${date}
            */
           public class ${entity}Manager {
           
           }
  
