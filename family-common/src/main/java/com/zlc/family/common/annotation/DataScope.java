package com.zlc.family.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author family
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表的别名
     */
    public String deptAlias() default "";

    /**
     * 用户表的别名
     */
    public String userAlias() default "";

    /**
     * 是否存在别名 默认true 如果false表示不存在别名即单表查询
     * @return
     */
    public boolean existsAlias() default true;

    /**
     * 是否自定义Sql查询
     * @return
     */
    public boolean useSql() default true;

    /**
     * 权限字符（用于多个角色匹配符合要求的权限）默认根据权限注解@ss获取，多个权限用逗号分隔开来
     */
    public String permission() default "";
}
