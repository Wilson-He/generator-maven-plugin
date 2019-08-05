# generator-maven-plugin([版本更新信息](https://github.com/Wilson-He/generator-maven-plugin/blob/master/%E7%89%88%E6%9C%AC%E6%9B%B4%E6%96%B0%E4%BF%A1%E6%81%AF.md))

- ## 快速开始(mvn generator:generate)

      <plugin>
          <groupId>io.github.wilson-he</groupId>
          <artifactId>generator-maven-plugin</artifactId>
          <version>0.0.2</version>
          <configuration>
              <basePackage>io.github.test</basePackage>
              <dataSource>
                  <url><![CDATA[jdbc:mysql://localhost:3306/wilson?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false]]></url>
                  <username>root</username>
                  <password>tiger</password>
                  <driverType>MYSQL</driverType>
              </dataSource>
          </configuration>
          <executions>
              <execution>
                  <goals>
                      <goal>generate</goal>
                  </goals>
              </execution>
          </executions>
      </plugin>

- ## 目前支持的配置项
  - outputDirectory: 输出绝对路径,默认生成到当前pom项目模块的target/generate-sources下
  - basePackage: 各层文件生成的基包[**必填项**]
  - dataSource: 数据库配置[**必填项**]
    - url
    - username
    - password
    - driverType:数据库驱动类型(MYSQL,ORACLE,POSTGRE_SQL)
  - templates: 自定义模板配置、模板生成配置,不配置则使用默认模板配置生成
    - entity: resources下的自定义entity模板相对路径
    - constant
    - dao
    - xml
    - service
    - serviceImpl
    - excludeEntity: true/false,是否生成entity模板
    - excludeXxx: 同excludeEntity
    - excludeController: 默认true,其它层默认false
    - customs: 对象列表,自定义模板
      - layerName: 模板分层名
      - subPackage: 所在子包
      - path: 模板在resources下的相对路径
  - exclusions: 字符串数组,不生成表名含数组内字符串的所有文件,默认空
  - inclusions: 字符串数组,只生成表名含数组内字符串的所有文件,默认空
  - upstreamExclusions: 字符串数组,不生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - upstreamInclusions: 字符串数组,只生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - logicDeleteFieldName: 全局逻辑删除字段名,默认空
  - tablePrefix: 字符串数组,表名前缀,默认空
    
- ## ftl模板常用变量
  - package: 包路径变量
    - Entity: entity完整包路径,如io.github.test.entity
    - Mapper
    - Service
    - ServiceImpl
    - Controller
  - table: 表信息
    - comment: 注释
    - name: 表名
    - entityName: 当前表对应生成的entity文件名,如UserBase.java
    - constantName: 当前表对应生成的constant文件名,如UserBase.java
    - mapperName: 同上
    - xmlName: 同上
    - serviceName: 同上
    - serviceImplName: 同上
    - controllerName: 同上
    - fields: 字段列表(以下为每个字段遍历时的常用属性)
      - table: 表名
      - propertyType: 属性类型
      - propertyName: 属性名
      - name: db字段名
      - comment: 字段注释
      
  - ## 详细配置例子
  
        <plugin>
            <groupId>io.github.wilson-he</groupId>
            <artifactId>generator-maven-plugin</artifactId>
            <version>0.0.2</version>
            <configuration>
                <basePackage>io.github.test</basePackage>
                <dataSource>
                    <url><![CDATA[jdbc:mysql://localhost:3306/wilson?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false]]></url>
                    <username>root</username>
                    <password>tiger</password>
                    <driverType>MYSQL</driverType>
                </dataSource>
                <exclusions>
                    <param>relation</param>
                </exclusions>
                <upstreamExclusions>
                    <param>detail</param>
                </upstreamExclusions>
                <templates>
                    <entity>/templates/custom-entity.java.ftl</entity>
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