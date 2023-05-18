package com.huawei.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hjf
 * @date 2022-10-19 10:24
 * @describe 用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="user_info")
public class UserInfo extends BaseEntity {

    @ApiModelProperty(value = "用户名称")
    public String userName;

    @ApiModelProperty(value = "登录密码 加密")
    public String password;

    @ApiModelProperty(value = "登录密码 原始密码")
    public String originalPassword;

    @ApiModelProperty(value = "头像")
    public String avatar;

    /**
     *
     */
    @ApiModelProperty(value = "性别")
    public Integer gender;

    /**
     *
     */
    @ApiModelProperty(value = "状态")
    public Integer status;

    @ApiModelProperty(value = "备注")
    public String remark;

}
