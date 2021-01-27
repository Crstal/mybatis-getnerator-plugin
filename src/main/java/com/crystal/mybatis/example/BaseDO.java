package com.crystal.mybatis.example;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaoe
 * @Date 2021/1/27
 * @Version 1.0
 **/
@Data
public class BaseDO {
    private Long id;
    private Long creator;
    private Long updator;
    private Date createTime;
    private Date updateTime;
}
