package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
//返回给前端用的统一数据格式(响应体)
public class JSONResponse {
    //业务操作是否成功
    private boolean success;
    //业务数据
    private Object data;
    //业务操作失败
    //错误码
    private String code;
    //错误消息
    private String message;
}
