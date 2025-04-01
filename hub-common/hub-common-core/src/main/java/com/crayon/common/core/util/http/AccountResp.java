package com.crayon.common.core.util.http;

import lombok.Data;

/**
 * @author Mengdl
 * @date 2025/03/31
 */
@Data
public class AccountResp<T> {

    private Integer code;

    private String msg;

    private T data;

}
