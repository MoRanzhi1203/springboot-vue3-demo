package com.example.springbootdemobackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "成功"),
    ERROR(500, "操作失败");
    // 响应状态码
    private Integer code;
    // 响应的提示信息
    private String message;
}
