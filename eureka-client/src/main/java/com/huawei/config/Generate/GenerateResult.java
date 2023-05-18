package com.huawei.config.Generate;

import lombok.Data;

@Data
public class GenerateResult<T> {

    private T data;
    private String msg;
    private String code;

    public static <T>GenerateResult<T> ok(T t){
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setData(t);
        generateResult.setCode("000");
        generateResult.setMsg("操作成功");
        return generateResult;
    }

    public static <T>GenerateResult<T> fail(String code,String msg){
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setCode(code);
        generateResult.setMsg(msg);
        return generateResult;
    }
}
