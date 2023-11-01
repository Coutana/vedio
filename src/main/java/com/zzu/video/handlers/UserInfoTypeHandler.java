package com.zzu.video.handlers;

import com.alibaba.fastjson.JSON;
import com.zzu.video.entity.UserInfo;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(UserInfo.class)
public class UserInfoTypeHandler extends BaseTypeHandler<UserInfo> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserInfo userInfo, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(userInfo));
    }

    @Override
    public UserInfo getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSON.parseObject(resultSet.getString(columnName),UserInfo.class);
    }

    @Override
    public UserInfo getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSON.parseObject(resultSet.getString(columnIndex),UserInfo.class);
    }

    @Override
    public UserInfo getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSON.parseObject(callableStatement.getString(columnIndex),UserInfo.class);
    }
}