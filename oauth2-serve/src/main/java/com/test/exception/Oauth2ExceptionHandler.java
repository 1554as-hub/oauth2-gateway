package com.test.exception;


import com.test.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class Oauth2ExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value =  OAuth2Exception.class)
    public  CommonResult<Object> handleOauth2(OAuth2Exception exception) {
        if(exception.getMessage() != null){
            return CommonResult.failed(exception.getMessage());
        }
        return CommonResult.failed();
    }


}
