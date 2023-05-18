package com.huawei.pojo;

import lombok.Data;

import javax.validation.Valid;

/**
 * 用户
 * @date 2020-7-13
 * @author lkx
 */
@Data
public class UserRsp {
    /**
     * 人
     */
    @Valid
    private User user;


    /**
     * 学生
     */
    @Valid
    private Student student;
}
