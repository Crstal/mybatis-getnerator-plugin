package com.crystal.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class BaseMapperGeneratorPlugin extends PluginAdapter {
    String rootInterface = null;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    public boolean validate(List<String> warnings) {
        rootInterface = properties.getProperty("rootInterface");
        if (!stringHasValue(rootInterface)) {
            warnings.add(getString("ValidationError.99", //$NON-NLS-1$
                    "MyBaseDaoPlugin", //$NON-NLS-1$
                    "targetPackage")); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * 生成dao
     */
    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        /**
         * 主键默认采用java.lang.Integer
         */
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("BaseMapper<"
                + introspectedTable.getBaseRecordType() + ","
                + introspectedTable.getExampleType() + ","
                + "java.lang.Integer" + ">");
        FullyQualifiedJavaType imp = new FullyQualifiedJavaType(rootInterface);
        /**
         * 添加 extends MybatisBaseMapper
         */
        interfaze.addSuperInterface(fqjt);

        /**
         * 添加import BaseMapper;
         */
        interfaze.addImportedType(imp);
        /**
         * 方法不需要
         */
        interfaze.getMethods().clear();
        interfaze.getAnnotations().clear();
        return true;
    }
}
