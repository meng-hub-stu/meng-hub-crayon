package com.crayon.base.test.yu.ed.service;

import org.springframework.stereotype.Service;

/**
 * @author Mengdl
 * @date 2025/04/28
 */
@Service
public abstract class AbstractBase<V extends BaseService>  {

    public AbstractBase(V v) {
    }

    public String test(Long id) {
        return "你是最棒的";
    }

}
