package huawei.pojo;


/**
 * 公用参数组件
 * @author lkx
 * @param <T>
 */
public class Result<T> {

    /**
     * 对象
     */
    private T data;
    /**
     * 接口调用信息
     */
    private String message;
    /**
     * code码
     */
    private String code;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 总数
     */
    private Long total;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public static Result ok(){
        Result Result = new Result();
        Result.setSuccess(true);
        return Result;
    }

    public static <T>Result<T> ok(String msg,String code){
        Result<T> Result = new Result<T>();
        Result.setMessage(msg);
        Result.setCode(code);
        Result.setSuccess(true);
        return Result;
    }

    public static <T>Result<T> ok(String msg){
        Result<T> Result = new Result<T>();
        Result.setMessage(msg);
        Result.setSuccess(true);
        return Result;
    }

    public static <T>Result<T> ok(T t){
        Result<T> Result = new Result<T>();
        Result.setData(t);
        Result.setSuccess(true);
        return Result;
    }
    public static <T>Result<T> ok(T t,Long total,String msg){
        Result<T> Result = new Result<T>();
        Result.setData(t);
        Result.setSuccess(true);
        Result.setTotal(total);
        Result.setMessage(msg);
        return Result;
    }
    public static <T>Result<T> ok(T t,String msg){
        Result<T> Result = new Result<T>();
        Result.setData(t);
        Result.setMessage(msg);
        Result.setSuccess(true);
        return Result;
    }

    public static <T>Result<T> ok(T t,String code,String msg){
        Result<T> Result = new Result<T>();
        Result.setData(t);
        Result.setCode(code);
        Result.setMessage(msg);
        Result.setSuccess(true);
        return Result;
    }

    public static <T>Result<T> fail(){
        Result<T> Result = new Result<T>();
        Result.setSuccess(false);
        return Result;
    }

    public static <T>Result<T> fail(String code,String msg){
        Result<T> Result = new Result<T>();
        Result.setCode(code);
        Result.setMessage(msg);
        Result.setSuccess(false);
        return Result;
    }
}
