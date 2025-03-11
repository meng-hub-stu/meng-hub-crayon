package com.crayon.common.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author Mengdl
 * @date 2024/7/15
 */
public abstract class BaseEntityWrapper<E, V> {
    public BaseEntityWrapper() {
    }

    public abstract V entityVO(E entity);

    public List<V> listVO(List<E> list) {
        return list.stream().map(this::entityVO).toList();
    }

    public Page<V> pageVO(IPage<E> pages) {
        List<V> records = this.listVO(pages.getRecords());
        Page<V> pageVo = new Page(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        pageVo.setOrders(pages.orders());
        return pageVo;
    }

}
