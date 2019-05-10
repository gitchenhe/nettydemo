package com.chenhe.netty2demo.codec;

import com.chenhe.netty2demo.entity.ConstantValue;
import com.chenhe.netty2demo.entity.Message;
import com.chenhe.util.HessianSerializerUtil;
import com.chenhe.util.KryoSerializerUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenhe
 * @date 2019-05-10 11:09
 * @desc
 */
public class ObjectToMessageEncoder extends MessageToMessageEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        if (!(o instanceof Serializable)) {
            throw new RuntimeException("对象不能被序列化,需继承自 Serializable");
        }

        try {
            byte[] bytes = HessianSerializerUtil.serialize(o);
            int length = bytes.length;
            Message message = new Message(ConstantValue.HEAD_TAG, ConstantValue.COMMAND_CODE, ConstantValue.VERSION, length, bytes);

            list.add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
