package com.huawei.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huawei.config.testField.TransferField;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 凌凯轩
 * @since 2021-11-09
 */
@Data
@Getter
@TableName("t_student")
public class Student implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 名字
     */
    @TransferField(target = "123")
    private String studentName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer sex;


}
