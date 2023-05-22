package com.huawei.config.Generate;

import lombok.Data;

@Data
public class GenerateResult<T> {

    private T data;
    private String message;
    private String code;
    private Boolean success;
    private Long total;

    public GenerateResult() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public static GenerateResult ok() {
        GenerateResult generateResult = new GenerateResult();
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(String msg, String code) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setMessage(msg);
        generateResult.setCode(code);
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(String msg) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setMessage(msg);
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(T t) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setData(t);
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(T t, Long total, String msg) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setData(t);
        generateResult.setSuccess(true);
        generateResult.setTotal(total);
        generateResult.setMessage(msg);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(T t, String msg) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setData(t);
        generateResult.setMessage(msg);
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> ok(T t, String code, String msg) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setData(t);
        generateResult.setCode(code);
        generateResult.setMessage(msg);
        generateResult.setSuccess(true);
        return generateResult;
    }

    public static <T> GenerateResult<T> fail() {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setSuccess(false);
        return generateResult;
    }

    public static <T> GenerateResult<T> fail(String code, String msg) {
        GenerateResult<T> generateResult = new GenerateResult();
        generateResult.setCode(code);
        generateResult.setMessage(msg);
        generateResult.setSuccess(false);
        return generateResult;
    }
}
