package ru.app.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

/**Враппер для мапера из джсона в объект и обратно*/
public class ObjectMapperWrapper {

    private static final ObjectMapper mapper;

    static{
       mapper =new ObjectMapper();
       mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
       mapper.registerModule(new JavaTimeModule());
       mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**Преобразует json-строку в объект*/
    @SneakyThrows
    public static <T> T readValue(String content, Class<T> valueType){
        return mapper.readValue(content, valueType);
    }

    /**Преобразует объект в json-строку*/
    @SneakyThrows
    public static String writeValueAsString(Object value) {
        return  mapper.writeValueAsString(value);
    }
}
