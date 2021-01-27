package com.crystal.mybatis.example;

/**
 * @Description TODO
 * @Author xiaoe
 * @Date 2021/1/27
 * @Version 1.0
 **/
public interface BaseMapper<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);
}
