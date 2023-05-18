package com.huawei.config.testSql;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlLogsInterceptor extends AbstractSqlParserHandler implements Interceptor {

    private DataSource dataSource;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("进入sql拦截器");
        Boolean hasSqlLogs = false;
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement =
                (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        final Object[] args = invocation.getArgs();
        //获取执行方法的位置
        String namespace = mappedStatement.getId();
        //获取mapper名称
        String className = namespace.substring(0,namespace.lastIndexOf("."));
        //获取方法名
        String methedName= namespace.substring(namespace.lastIndexOf(".") + 1,namespace.length());
        //获取当前mapper 的方法
        Method[] ms = Class.forName(className).getMethods();
        for(Method m : ms){
            if(m.getName().equals(methedName)){
                SqlLogs annotation = m.getAnnotation(SqlLogs.class);
                if(annotation != null){
                    hasSqlLogs = annotation.taget();
                }
            }
        }
        //如果是有注解值为true，便获取sql处理参数
        if(hasSqlLogs){
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            // 执行的SQL语句
            String originalSql = boundSql.getSql();
            // SQL语句的参数
            Object parameterObject = boundSql.getParameterObject();
//            if(parameterObject != null){
//                originalSql = showSql(boundSql);
//            }
            originalSql = showSql(boundSql);
            System.err.println("sql :"+originalSql);
            //通过反射修改sql语句
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, originalSql);
        }
        return invocation.proceed();

    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 获取参数值
     * @param obj
     * @return
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "to_date('" + formatter.format(obj) + "','yyyy-MM-dd hh24:mi:ss')";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        System.err.println("获取值: "+value);
        return value;
    }

    /***
     * sql 参数替换
     * @param boundSql
     * @return
     */
    public static String showSql(BoundSql boundSql) {
//        //获取参数对象
//        Object parameterObject = boundSql.getParameterObject();
//        //获取当前的sql语句有绑定的所有parameterMapping属性
//        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //去除空格
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        sql=sql+" limit 1";
        return sql;
    }

}
