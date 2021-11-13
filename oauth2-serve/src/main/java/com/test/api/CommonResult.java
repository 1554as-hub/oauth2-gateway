package com.test.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Long code;
    private String message;
    private T data;


    public CommonResult(Long code , String message){
        this(code , message , null);
    }

    public CommonResult(Long code ){
        this(code , null );
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <E> CommonResult<E>  success(E data){
        return new CommonResult<>(ResultCode.SUCCESS.getCode() , ResultCode.SUCCESS.getMessage() , data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <E> CommonResult<E> success(E data , String message){
        return new CommonResult<>(ResultCode.SUCCESS.getCode() , message , data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <E> CommonResult<E> failed(IErrorCode errorCode){
        return new CommonResult<>(errorCode.getCode() , errorCode.getMessage() , null);
    }

    /**
     * @param errorCode  错误码
     * @param message 错误信息
     * @param <E> 数据
     * @return 返回错误信息
     */
    public static <E> CommonResult<E> failed(IErrorCode errorCode , String message){
        return new CommonResult<>(errorCode.getCode() , message , null);
    }

    /**
     * @param message 错误信息
     * @param <E>
     * @return 返回错误信息
     */
    public static <E> CommonResult<E> failed( String message){
        return new CommonResult<>(ResultCode.FAILED.getCode() , message , null);
    }

    /**
     * @param <E>
     * @return 返回错误信息
     */
    public static <E> CommonResult<E> failed(){
        return failed(ResultCode.FAILED);
    }

    public static <E> CommonResult<E> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    public static <E> CommonResult<E> validateFailed(String message) {
        return new CommonResult<>(ResultCode.VALIDATE_FAILED.getCode() , message ,null);
    }

    public static <E> CommonResult<E> unathorized(E data){
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode() , ResultCode.UNAUTHORIZED.getMessage() , data);
    }

    public static <E> CommonResult<E> forbidden(E data){
        return new CommonResult<>(ResultCode.FORBIDDEN.getCode() , ResultCode.FORBIDDEN.getMessage() , data);
    }

}
