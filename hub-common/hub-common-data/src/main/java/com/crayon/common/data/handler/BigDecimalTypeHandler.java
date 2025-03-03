package com.crayon.common.data.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BigDecimal 处理·
 *
 * @author xuzihui
 * @version 1.0.0
 * @date 2023/5/5 11:29
 */
@MappedTypes(value = {BigDecimal.class})
@MappedJdbcTypes(value = {JdbcType.DECIMAL, JdbcType.NUMERIC})
public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setBigDecimal(i, parameter);
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        BigDecimal result = rs.getBigDecimal(columnName);
        if (result != null) {
            //去除小数点后面尾部多余的0
            result = result.stripTrailingZeros();
        }
        return result;
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        BigDecimal result = rs.getBigDecimal(columnIndex);
        if (result != null) {
            //去除小数点后面尾部多余的0
            result = result.stripTrailingZeros();
        }
        return result;
    }

    @Override
    public BigDecimal getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        BigDecimal result = cs.getBigDecimal(columnIndex);
        if (result != null) {
            //去除小数点后面尾部多余的0
            result = result.stripTrailingZeros();
        }
        return result;
    }
}