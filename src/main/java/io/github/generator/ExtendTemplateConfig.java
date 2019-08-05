package io.github.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wilson
 * @date 2019/8/5
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendTemplateConfig {
    /**
     * 所属层名,首字母大写,将作为ftl包路径变量,如：layerName=Manager
     */
    private String layerName;
    /**
     * 模板在resources下的相对路径
     */
    private String path;
    /**
     * 所在字包，例：subPackage=manager,则ftl文件可通过${package.layerName}获取包路径xxx.xxx.xxx.manager
     */
    private String subPackage;

}
