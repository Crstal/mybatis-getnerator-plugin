package com.crystal.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
* @Author: caoyue
* @Description: 注释
* @Date: 19:00 2018/8/9
**/
public class MyCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * 字段：" + introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" * 含义：" + introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
    }
}
