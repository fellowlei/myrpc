package com.mark.service.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoTool {

    //每个线程的 Kryo 实例
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();

            /**
             * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
             * 上线的同时就必须清除 Redis 里的所有缓存，
             * 否则那些缓存再回来反序列化的时候，就会报错
             */
            //支持对象循环引用（否则会栈溢出）
            kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置

            //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
            kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置

            //Fix the NPE bug when deserializing Collections.
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

            return kryo;
        }
    };

    private static Kryo get() {
        return kryoLocal.get();
    }

    /**
     * 深拷贝
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T clone(T obj) {
        Kryo kryo = get();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);
        kryo.writeObject(output, obj);
        output.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Input input = new Input(is);
        T result = (T) kryo.readObject(input, obj.getClass());
        input.close();
        return result;
    }

    public static String encode(Object obj) {
        if (obj == null) {
            return null;
        }
        Kryo kryo = get();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);
        kryo.writeObject(output, obj);
        output.close();
        return new Base64().encodeAsString(os.toByteArray());
    }

    public static <T> T decode(String encode, Class<T> clazz) {
        if (StringUtils.isEmpty(encode)) {
            return null;
        }
        Kryo kryo = get();
        byte[] decode = new Base64().decode(encode);
        ByteArrayInputStream is = new ByteArrayInputStream(decode);
        Input input = new Input(is);
        T result = (T) kryo.readObject(input, clazz);
        input.close();
        return result;
    }

    public static byte[] encodeByte(Object obj) {
        if (obj == null) {
            return null;
        }
        Kryo kryo = get();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);
        kryo.writeObject(output, obj);
        output.close();
        return os.toByteArray();
    }

    public static <T> T decodeByte(byte[] encode, Class<T> clazz) {
//        Kryo kryo = new Kryo();
        Kryo kryo = get();
        ByteArrayInputStream is = new ByteArrayInputStream(encode);
        Input input = new Input(is);
        T result = (T) kryo.readObject(input, clazz);
        input.close();
        return result;
    }
}
