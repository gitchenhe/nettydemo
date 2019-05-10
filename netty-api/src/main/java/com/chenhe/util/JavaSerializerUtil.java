package com.chenhe.util;

import java.io.*;

/**
 * @author chenhe
 * @date 2019-05-08 16:56
 * @desc
 */
public class JavaSerializerUtil {

    public static <T> byte[] serialize(T obj){
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public static <T> T deserialize(byte[] data){
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (data);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (T)obj;
    }

}
