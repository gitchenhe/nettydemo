package com.chenhe.util;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author chenhe
 * @date 2019-05-08 15:37
 * @desc 序列化对象
 */
public class HessianSerializerUtil {
    public static <T> byte[] serialize(T obj){
        byte[] bytes = null;

        //1.创建字节输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(bos);

        try{
             hessianOutput.writeObject(obj);
             bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return bytes;
    }

    public static <T> T deserialize(byte[] data){
        if (data == null){
            return null;
        }

        //1.将字节数组转换成字节输入流
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(bis);

        Object object = null;

        try{
            object = hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            hessianInput.close();
        }

        return (T)object;
    }
}
