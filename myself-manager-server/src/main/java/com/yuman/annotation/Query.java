package com.yuman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {


    String propName() default "";

    Type type() default Type.EQUAL;

    Order order() default Order.NOT_ORDER;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    String blurry() default "";

    enum Type {
        // 相等
        EQUAL
        // 不等于
        , NOT_EQUAL
        // 小于
        , LESS_THAN
        // 小于等于
        , LESS_THAN_EQ
        // 大于
        , GREATER_THAN
        // 大于等于
        , GREATER_THAN_EQ
        // 中模糊查询
        , INNER_LIKE
        // 左模糊查询
        , LEFT_LIKE
        // 右模糊查询
        , RIGHT_LIKE
        // 包含
        , IN
        // 不包含
        , NOT_IN
        // between
        , BETWEEN
        // 不为空
        , NOT_NULL
        // 为空
        , IS_NULL
        //只排序
        , ONLY_ORDER
    }

    /**
     * 排序
     */
    enum Order {
        NOT_ORDER, ASC, DESC
    }

}

