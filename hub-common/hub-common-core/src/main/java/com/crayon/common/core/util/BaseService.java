package com.crayon.common.core.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author Mengdl
 * @date 2024/8/3
 */
public interface BaseService<T> {

    <V> IPage<V> total(IPage<V> dataVO, QueryWrapper<T> query);

}
