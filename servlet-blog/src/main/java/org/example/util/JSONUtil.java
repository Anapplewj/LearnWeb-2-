package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JSONUtil {
    private static final ObjectMapper M=new ObjectMapper();

    //序列化: 将java对象序列化为json字符串
    public static String serialize(Object o) throws JsonProcessingException {
        return M.writeValueAsString(o);
    }

    //反序列化: 将json字符串反序列化为java对象
    public static <T> T deserialize(InputStream is, Class<T> c) throws IOException {
        return M.readValue(is,c);
    }
}
