package com.crayon.common.data.cache;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alicp.jetcache.anno.KeyConvertor;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/04/22
 */
@Component(value = "tenantCacheKeyGenerator")
public class TenantCacheKeyGenerator implements KeyConvertor {
    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        Long tenantIdAuth = 1L;
        if (originalKey instanceof String) {
            return StrUtil.COLON + tenantIdAuth.toString() + StrUtil.COLON + originalKey;
        }
        return StrUtil.COLON + tenantIdAuth + StrUtil.COLON + JSON.toJSONString(originalKey);
    }

}
