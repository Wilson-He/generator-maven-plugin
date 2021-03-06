- # 版本
  #### (注：中位版本变更意味原配置属性进行变更或生成bug修复)
  - ### 0.2.7
    - 删除crudIdType设置，修改为默认读取entity id字段类型
    - 添加生成所在包配置项 `packageConfig`
  - ### 0.2.6
    - 修改添加enum常量模板,添加useEnumTemplate配置项
  - ### 0.2.5
    - 修改常量模板
    - 修复Controller模板swagger类导入问题
  - ### 0.2.4
    - 修复常量字段注释校验bug
    - 更改Mysql依赖版本为8.0.16
  - ### 0.2.3
    - 修复inclusions无效问题
    - 部分代码优化
    - 提示文档完善
  - ### 0.2.2
    - 修复常量模板变量出现字符串符号"错误
  - ### 0.2.1
    - 实体模板添加常量判断方法
    - 修复controller文件模板swagger问题
    - 修改常量注释,去除常量key
    - 修改entity模板注释
  - ### 0.2.0
    - javaIdType重命名为crudIdType
    - controller文件模板进行更改,可通过设置useSwagger生成swagger注解
    - 修复不配置dataSource.commentPattern会导致NPE问题
    - 常量类生成字段注释字符串常量
    - 补充templateConfig配置注释
  - ### 0.1.4
    - 完善注释，Ctrl+Q查看完整标签注释用法,二级配置标签注释通过一级标签查看,如dataSources.excludeConstantFields用法可通过查看dataSources注释查看
    - 添加dataSource.excludeConstantFields: 不生成指定字段常量类,如: is_delete,state
    - 添加jsonIgnores:在指定字段上添加jackson @JsonIgnore注解,多个字段用逗号分隔,例: password,salt,isDelete
  - ### 0.1.3
    - 更改mysql依赖版本
    - 添加sql常量字段模板配置dataSource.commentPattern
    - 添加dataSource.bitToInteger配置,将数据库类型bit转java Integer配置bitToInteger,默认true,false则转Boolean
    - 添加idType配置,用于设置实体id注解IdType,不配置则不生成实体id的注解
    - 添加javaIdType配置,设置后生成的service将自动生成对应的findById方法
  - ### 0.1.2
    - 修改sql常量字段注释格式,Demo[删除标志量(1:已删除-YES, 0:未删除-NO),删除标志量(YES-已删除, NO-未删除)]
  - ### 0.1.1
    - 修复生成NPE
  - ### 0.1.0
    - 修复生成mapper xml文件后缀错误
  - ### 0.0.5
    - 添加useSwagger(boolean)参数
    - 添加superEntityClass参数
    - 设置mapper.xml默认生成所有db字段拼接字符串
    - 修改部分模板无用参数
  - ### 0.0.4
    - 添加author参数
    - 修改部分模板样式问题
    - 添加isCleanBefore参数，设置生成前是否清空当前模块target目录,默认false
  - ### 0.0.3
    - 将templates路径变量更改为xxxPath
    - 添加生成文件名样式设置
  - ### 0.0.2
    - #### 添加自定义模板, ftl模板文件可通过${package.layerName}获取所在包,例：
       - 模板文件例子/resources/templates/manager.ftl:
        
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
        - pom配置
        
              <templates>
                  <customs>
                      <param>
                          <path>/templates/manager.ftl</path>
                          <subPackage>manager</subPackage>
                          <layerName>Manager</layerName>
                      </param>
                  </customs>
              </templates>