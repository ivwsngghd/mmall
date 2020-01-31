package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;


/**
 * 复用性极强
 * 注意简单的转换有限，如果将已经序列化为字符串的实例对象链表，再反序列化为类链表，则该链表里的类只是一个LinkedHashMap对象,而不是具体的类；
 * 譬如ArrayList<User>的实例对象 转换为  String 再转回  listObj,那么里面存放的是LinkedHashMap，而不是User的实例对象，因此也没有getId的方法
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    //初始化
    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        /*
        ALWAYS：JSON总是序列化，无论是否NULL值
        NON_NULL：值为NULL的，则不序列化
        NON_DEFAULT：忽略默认值，只序列化对应赋值
        NON_EMPTY：非空，即空链表，空字符串也不序列化，比non_null更严格
         */

        //取消默认转换的timestamps形式；如果为true，则返回一段时间戳(？年至今的毫秒数)
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为一下样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略在json字符串中存在，但是java对象中不存在对应属性的情况，防止错误 **
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    }

    public static <T> String objToString (T obj){
        if (obj == null){
            return null ;
        }
        try{
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    public static <T> String objToStringPretty (T obj){
        if (obj == null){
            return null ;
        }
        try{
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }


    /**
     * 传入一个字符串，以及要转换成的类的类型
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToObj (String str,Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null){
            return null ;
        }
        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null ;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType =  objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try{
            return objectMapper.readValue(str,javaType);
        }catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }




}
