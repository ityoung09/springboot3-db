package com.kedaya.springboot3mongodb.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理用户不存在异常
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("用户不存在异常: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "用户不存在");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "服务器内部错误");
        errorResponse.put("message", "处理请求时发生错误");
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ClientAbortException.class, IOException.class})
    public void handleClientAbortException(Exception ex) {
        // 判断是否为客户端中断异常
        if (ex instanceof ClientAbortException ||
                (ex instanceof IOException && ex.getMessage() != null &&
                        (ex.getMessage().contains("Broken pipe") || ex.getMessage().contains("Connection reset")))) {
            log.debug("客户端中断连接: {}", ex.getMessage());
            // 对于客户端中断，只记录日志，不需要返回响应
            // 返回void表示Spring MVC不会尝试创建响应
        } else {
            // 其他IO异常需要记录并再次抛出
            log.error("IO异常: ", ex);
            throw new RuntimeException(ex);
        }
    }
}