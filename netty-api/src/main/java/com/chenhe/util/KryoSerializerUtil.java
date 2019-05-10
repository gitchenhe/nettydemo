package com.chenhe.util;

import com.chenhe.entity.MessageBean;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author chenhe
 * @date 2019-05-08 15:48
 * @desc
 */
public class KryoSerializerUtil {

    static Kryo kryo = new Kryo();
    static {
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public static <T> byte[] serialize(T obj) {

        kryo.register(obj.getClass());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        try {
            kryo.writeClassAndObject(output, obj);
            output.flush();
        } finally {
            output.close();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }

        kryo.register(clazz);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Input input = new Input(bais);
        try {
            return kryo.readObject(input, clazz);
        }finally {
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input.close();
        }
    }

    public KryoPool newKryoPool() {
        return new KryoPool.Builder(() -> {
            final Kryo kryo = new Kryo();
            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(
                    new StdInstantiatorStrategy()));
            return kryo;
        }).softReferences().build();
    }

    public static void main(String[] args) {
        MessageBean messageBean =new MessageBean();
        messageBean.setContent("hhhhhhhhhhhhhh");
        byte[] data = serialize(messageBean.getContent());
        System.out.println(deserialize(data,String.class));
    }
}
