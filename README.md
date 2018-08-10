# mybatis-getnerator-plugin 用于使用mybatis-generator自动生成代码时的插件

##类说明：
  LombokPlugin类会添加一些Lombok的注解，让代码更简洁
  IgnorePlugin类可以生成Model的时候忽略一些字段
  ModelFieldCommentGenerator类在生成Model的时候添加中文注释
  NoExampleJavaMapperGenerator类生成的Mapper类不包含Example
  NoExampleXMLMapperGenerator类生成的Mapper.xml文件不包含Example
  
##在Maven项目中使用
1.引入插件
```
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.5</version>
    <configuration>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
        <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${jdbc-version}</version>
        </dependency>
        <dependency>
            <groupId>com.crystal.mybatis.plugin</groupId>
            <artifactId>mybatis-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</plugin>
```
2.generatorConfig.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- mvn mybatis-generator:generate -->
    <context id="blog" targetRuntime="Mybatis3">
        <property name="javaFileEncoding" value="UTF-8"/>
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>

        <plugin type="com.crystal.mybatis.plugin.LombokPlugin"/>

        <!-- 自定义方法 -->
        <plugin type="com.crystal.mybatis.plugin.IgnorePlugin">
            <property name="ignoreFields" value="id,create_By,create_Time,update_By,update_Time"/>
        </plugin>

        <commentGenerator type="com.crystal.mybatis.ModelFieldCommentGenerator">
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/blog?useUnicode=true"
                        userId="crystal"
                        password="123456">
        </jdbcConnection>

        <javaTypeResolver type="org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl">
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 生成实体类文件 -->
        <javaModelGenerator targetPackage="com.crystal.blog.dao.model"
                            targetProject="src/main/java" >
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
            <property name="rootClass" value="com.crystal.blog.dao.model.base.BaseModel" />
        </javaModelGenerator>

        <!-- 该配置生成 mybatis 映射文件，即Mapper.XML-->
        <sqlMapGenerator targetPackage="mapper"
                         targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!-- 生成 java 接口代码,即Mapper  -->
        <javaClientGenerator type="com.crystal.mybatis.NoExampleJavaMapperGenerator"
                             targetPackage="com.crystal.blog.dao.mapper"
                             targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="rootInterface" value="com.crystal.blog.dao.mapper.base.BaseMapper"/>
        </javaClientGenerator>

        <table schema="blog" tableName="article" domainObjectName="Article">
            <property name="useActualColumnNames" value="false"/>
            <generatedKey column="id" sqlStatement="MySql" identity="true"/>
        </table>
 
    </context>
</generatorConfiguration>
```

            
