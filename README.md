# generator-maven-plugin

- ## 依赖添加

      <plugin>
          <groupId>io.github.wilson-he</groupId>
          <artifactId>generator-maven-plugin</artifactId>
          <version>0.0.1</version>
          <configuration>
              <basePackage>io.github.test</basePackage>
              <dataSource>
                  <url>db-url</url>
                  <username>db-username</username>
                  <password>db-password</password>
                  <driverType>db-driver-type</driverType>
              </dataSource>
          <executions>
              <execution>
                  <goals>
                      <goal>generate</goal>
                  </goals>
              </execution>
          </executions>
      </plugin>

- ## 快速开始(mvn generator:generate)

      <plugin>
          <groupId>io.github.wilson-he</groupId>
          <artifactId>generator-maven-plugin</artifactId>
          <version>0.0.1</version>
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
  - exclusions: 字符串数组,不生成表名含数组内字符串的所有文件,默认空
  - inclusions: 字符串数组,只生成表名含数组内字符串的所有文件,默认空
  - upstreamExclusions: 字符串数组,不生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - upstreamInclusions: 字符串数组,只生成表名含数组内字符串的service、serviceImpl、controller文件,默认空
  - logicDeleteFieldName: 全局逻辑删除字段名,默认空
  - tablePrefix: 字符串数组,表名前缀,默认空
    
    