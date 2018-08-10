package com.crystal.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
* @Author: caoyue
* @Description: 字段不显示
* @Date: 21:09 2018/8/9
**/
public class IgnorePlugin extends PluginAdapter {

    private String[] ignoreFields;

    /**
     * 验证参数是否有效
     * @param warnings
     * @return
     */
    public boolean validate(List<String> warnings) {
        String ignoreField = properties.getProperty("ignoreFields");
        if (stringHasValue(ignoreField)) {
            ignoreFields = ignoreField.split(",");
        }

        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (ignoreFields != null) {
            for (String ignoreField : ignoreFields) {
                if (introspectedColumn.getActualColumnName().equalsIgnoreCase(ignoreField)) {
                    return false;
                }
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
