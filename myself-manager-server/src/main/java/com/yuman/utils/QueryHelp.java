package com.yuman.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuman.annotation.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class QueryHelp {

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static <R, Q> QueryWrapper<R> getQueryWrapper(Q query, Class<R> rClass) {
        QueryWrapper<R> queryWrapper = new QueryWrapper<>();
        if (query == null) {
            return queryWrapper;
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();

                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String blurry = q.blurry();

                    String attributeName = isBlank(propName) ? convertAttribute(field.getName()) : propName;
                    Object val = field.get(query);
                    if (isNull(val) || "".equals(val)) {
                        continue;
                    }

                    // 排序
                    switch (q.order()){
                        case ASC:
                            queryWrapper.orderByAsc(attributeName);
                            break;
                        case DESC:
                            queryWrapper.orderByDesc(attributeName);
                            break;
                        default:
                            break;
                    }

                    // 模糊多字段
                    if (StringUtils.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        for (String s : blurrys) {
                            queryWrapper.like(s, val.toString());
                        }
                        continue;
                    }

                    switch (q.type()) {
                        case EQUAL:
                            queryWrapper.eq(attributeName, val);
                            break;
                        case NOT_EQUAL:
                            queryWrapper.ne(attributeName, val);
                            break;
                        case LESS_THAN:
                            queryWrapper.lt(attributeName, val);
                            break;
                        case LESS_THAN_EQ:
                            queryWrapper.le(attributeName, val);
                            break;
                        case GREATER_THAN:
                            queryWrapper.gt(attributeName, val);
                            break;
                        case GREATER_THAN_EQ:
                            queryWrapper.ge(attributeName, val);
                            break;
                        case INNER_LIKE:
                            queryWrapper.like(attributeName, val);
                            break;
                        case LEFT_LIKE:
                            queryWrapper.likeLeft(attributeName, val);
                            break;
                        case RIGHT_LIKE:
                            queryWrapper.likeRight(attributeName, val);
                            break;
                        case IN:
                            if (!isEmpty((Collection<Object>) val)) {
                                queryWrapper.in(attributeName,val);
                            }
                            break;
                        case NOT_IN:
                            if (!isEmpty((Collection<Object>) val)) {
                                queryWrapper.notIn(attributeName,val);
                            }
                            break;
                        case NOT_NULL:
                            queryWrapper.isNotNull(attributeName);
                            break;
                        case IS_NULL:
                            queryWrapper.isNull(attributeName);
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            queryWrapper.between(attributeName,between.get(0),between.get(1));
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return queryWrapper;
    }

    /**
     * 驼峰转化成下划线
     *
     * @param fieldName
     * @return
     */
    private static String convertAttribute(String fieldName) {
        Matcher matcher = humpPattern.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNull(Object obj) {
        return null == obj || obj.equals((Object) null);
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
