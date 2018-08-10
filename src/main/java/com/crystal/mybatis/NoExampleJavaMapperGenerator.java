package com.crystal.mybatis;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
* @Author: caoyue
* @Description: Java 类 不适用Example
* @Date: 19:01 2018/8/9
**/
public class NoExampleJavaMapperGenerator extends JavaMapperGenerator {

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(Messages.getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }
        System.out.println(rootInterface);
        if (stringHasValue(rootInterface)) { // 有继承类
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
                    rootInterface.substring(rootInterface.lastIndexOf(".") + 1)  + "<" + introspectedTable.getBaseRecordType().replace("Mapper", "") + ">");
            interfaze.addSuperInterface(fqjt);
            interfaze.getAnnotations().clear();
            interfaze.addImportedType(new FullyQualifiedJavaType(rootInterface));
            interfaze.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        } else {
            // 去掉example
            addDeleteByPrimaryKeyMethod(interfaze);
            addInsertMethod(interfaze);
            addInsertSelectiveMethod(interfaze);
            addSelectByPrimaryKeyMethod(interfaze);
            addUpdateByPrimaryKeySelectiveMethod(interfaze);
            addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
            addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().clientGenerated(interfaze, null,
                introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new NoExampleXMLMapperGenerator();
    }
}
