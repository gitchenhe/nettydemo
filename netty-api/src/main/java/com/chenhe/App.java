package com.chenhe;

import com.chenhe.entity.MessageData;
import com.chenhe.entity.MessageTypeEnum;
import com.chenhe.util.HessianSerializerUtil;
import com.chenhe.util.JavaSerializerUtil;
import com.chenhe.util.KryoSerializerUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        MessageData messageData = new MessageData();
        messageData.setMessageType(MessageTypeEnum.GENERAL);
        messageData.setMessage("1231231231231231212312312312312312123123123123123121231231231231231212312312312312312");
        messageData.setTo("12334123123123123123121231231231231231212312312312312312123123123123123122342");
        messageData.setFrom("53451451231231231231231212312312312312312123123123123123121231231231231231214515");

        //List<MessageData> list = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < 10; i++) {

            long start = System.nanoTime();
            byte[] bytes0 = JavaSerializerUtil.serialize(messageData);
            logger.info("JAVA    序列化耗时={},长度={}", (System.nanoTime() - start), bytes0.length);

            start = System.nanoTime();
            String data = gson.toJson(messageData);
            logger.info("gson    序列化耗时={},长度={}", (System.nanoTime() - start), data.getBytes().length);
            start = System.nanoTime();
            byte[] bytes = HessianSerializerUtil.serialize(messageData);
            logger.info("hession 序列化耗时={},长度={}", (System.nanoTime() - start), bytes.length);

            start = System.nanoTime();
            byte[] byte2 = KryoSerializerUtil.serialize(messageData);
            logger.info("kyro    序列化耗时={},长度={}", (System.nanoTime() - start), byte2.length);

            System.out.println();

            start = System.nanoTime();
            JavaSerializerUtil.deserialize(bytes0);
            logger.info("JAVA    反序列化耗时={}", (System.nanoTime() - start));

            start = System.nanoTime();
            gson.fromJson(new String(data.getBytes()), MessageData.class);
            logger.info("gson    反序列化耗时={}", (System.nanoTime() - start));

            start = System.nanoTime();
            HessianSerializerUtil.deserialize(bytes);
            logger.info("hession 反序列化耗时={}", (System.nanoTime() - start));

            start = System.nanoTime();
            KryoSerializerUtil.deserialize(byte2, MessageData.class);
            logger.info("kyro    反序列化耗时={}", (System.nanoTime() - start), byte2.length);


            System.out.println("********************************************************");
        }
    }
}
