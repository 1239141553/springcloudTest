package com.huawei.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 学生
 * @date 2020-7-13
 * @author lkx
 */
@Data
public class Student {
    /**
     * id
     */
    private String id;

    /**
     * 年龄
     */
    @NotBlank(message = "年龄不能为空")
    private String age;
}
