package com.crystal.mybatis.plugin.noexample;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class BaseMapperExtGeneratorPlugin extends PluginAdapter {
    private static final String DEFAULT_DAO_SUPER_CLASS = "com.crystal.mybatis.example.BaseMapper";
//    private static final String DEFAULT_EXPAND_DAO_SUPER_CLASS = "com.crystal.mybatis.example.BaseMapperExt";

    private String daoTargetDir;
    private String daoTargetPackage;

    private String daoSuperClass;
    // 扩展
    private String expandDaoTargetPackage;
//    private String expandDaoSuperClass;

    private ShellCallback shellCallback = null;

    public BaseMapperExtGeneratorPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    public boolean validate(List<String> warnings) {

        daoTargetDir = properties.getProperty("targetProject");
        boolean valid = stringHasValue(daoTargetDir);

        daoTargetPackage = properties.getProperty("targetPackage");
        boolean valid2 = stringHasValue(daoTargetPackage);

        daoSuperClass = properties.getProperty("daoSuperClass");
        if (!stringHasValue(daoSuperClass)) {
            daoSuperClass = DEFAULT_DAO_SUPER_CLASS;
        }

        expandDaoTargetPackage = properties.getProperty("expandTargetPackage");
//        expandDaoSuperClass = properties.getProperty("expandDaoSuperClass");
//        if (!stringHasValue(expandDaoSuperClass)) {
//            expandDaoSuperClass = DEFAULT_EXPAND_DAO_SUPER_CLASS;
//        }
        return valid && valid2;
    }

    /**
     * 生成mapping 添加自定义sql
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        //创建Select查询
//        XmlElement select = new XmlElement("select");
//        select.addAttribute(new Attribute("id", "selectAll"));
//        select.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//        select.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
//        select.addElement(new TextElement("select * from "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()));
//
//        XmlElement queryPage = new XmlElement("select");
//        queryPage.addAttribute(new Attribute("id", "queryPage"));
//        queryPage.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//        queryPage.addAttribute(new Attribute("parameterType", "com.fendo.bean.Page"));
//        queryPage.addElement(new TextElement("select * from "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()));
//
//        XmlElement parentElement = document.getRootElement();
//        parentElement.addElement(select);
//        parentElement.addElement(queryPage);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            GeneratedJavaFile mapperJavafile = null;

            if (shortName.endsWith("Mapper")) { // 扩展Mapper
                if (stringHasValue(expandDaoTargetPackage)) {

                    FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(shortName.replace("Mapper", "MapperExt"));
                    unit.getSuperInterfaceTypes().add(daoSuperType);
                    unit.getImportedTypes().add(daoSuperType);

                    String baseMapper = shortName;
                    Interface mapperInterface = new Interface(
                            expandDaoTargetPackage + "." + shortName.replace("Mapper", "MapperExt"));
                    mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                    mapperInterface.addJavaDocLine("/**");
                    mapperInterface.addJavaDocLine(" * " + shortName + "扩展");
                    mapperInterface.addJavaDocLine(" */");

                    mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
                    try {
                        File mapperDir = shellCallback.getDirectory(daoTargetDir, expandDaoTargetPackage);
                        File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
                        // 文件不存在
                        if (!mapperFile.exists()) {
                            mapperJavaFiles.add(mapperJavafile);
                        }
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                }
            } else if (!shortName.endsWith("Example")) { // CRUD Mapper
                Interface mapperInterface = new Interface(expandDaoTargetPackage + "." + shortName + "Mapper");

                mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                mapperInterface.addJavaDocLine("/**");
                mapperInterface.addJavaDocLine(" * MyBatis Generator工具自动生成");
                mapperInterface.addJavaDocLine(" */");

                FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(daoSuperClass);
                // 添加泛型支持
                daoSuperType.addTypeArgument(baseModelJavaType);
                mapperInterface.addImportedType(baseModelJavaType);
                mapperInterface.addImportedType(daoSuperType);
                mapperInterface.addSuperInterface(daoSuperType);

                mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
                mapperJavaFiles.add(mapperJavafile);

            }
        }
        return mapperJavaFiles;
    }


    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        XmlFormatter xmlFormatter = context.getXmlFormatter();
        List<GeneratedXmlFile> mapperXmlFiles = new ArrayList<>();
        for (GeneratedXmlFile xmlFile : introspectedTable.getGeneratedXmlFiles()) {
            String fileName = xmlFile.getFileName();
            GeneratedXmlFile generatedXmlFile = null;

            if (fileName.endsWith("Mapper.xml")) { // 扩展Mapper
                Document document = new Document(
                        XmlConstants.MYBATIS3_MAPPER_CONFIG_PUBLIC_ID,
                        XmlConstants.MYBATIS3_MAPPER_CONFIG_SYSTEM_ID);

                XmlElement root = new XmlElement("configuration"); //$NON-NLS-1$
                document.setRootElement(root);

                XmlElement mappers = new XmlElement("mapper"); //$NON-NLS-1$
                String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
                mappers.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                        namespace));

                mappers.addElement(new TextElement("<!--")); //$NON-NLS-1$
                mappers.addElement(new TextElement("  This file is generated by MyBatis Generator.")); //$NON-NLS-1$
                StringBuilder sb = new StringBuilder();
                sb.append("  This file was generated on "); //$NON-NLS-1$
                sb.append(new Date());
                sb.append('.');
                mappers.addElement(new TextElement(sb.toString()));
                mappers.addElement(new TextElement("-->")); //$NON-NLS-1$

                root.addElement(mappers);

                generatedXmlFile = new GeneratedXmlFile(document, properties
                        .getProperty("fileName", fileName.replace(".xml", "Ext.xml")), //$NON-NLS-1$ //$NON-NLS-2$
                        expandDaoTargetPackage, //$NON-NLS-1$
                        daoTargetDir, //$NON-NLS-1$
                        false, xmlFormatter);

                try {
                    File mapperDir = shellCallback.getDirectory(daoTargetDir, expandDaoTargetPackage);
                    File mapperFile = new File(mapperDir, generatedXmlFile.getFileName());
                    // 文件不存在
                    if (!mapperFile.exists()) {
                        mapperXmlFiles.add(generatedXmlFile);
                    }
                } catch (ShellException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapperXmlFiles;
    }
}