package com.chenhe.netty2demo.codec;

import com.chenhe.netty2demo.entity.ConstantValue;
import com.chenhe.netty2demo.entity.Message;
import com.chenhe.util.HessianSerializerUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author chenhe
 * @date 2019-05-10 11:15
 * @desc
 */
public class MessageToObjectDecoder extends MessageToMessageEncoder<Message> {

    Logger logger = LoggerFactory.getLogger(MessageToObjectDecoder.class);


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {


        //验证请求版本
        if (message == null) {
            return;
        }
        if (message.getVersion() != ConstantValue.VERSION) {
            logger.error("版本不一致,实际版本:{},要求版本:{}", message.getVersion(), ConstantValue.VERSION);
            throw new RuntimeException("版本不一致");
        }


        if (message.getLength() != message.getContent().length) {
            logger.error("消息不完整,标记长度:{},实际长度:{}", message.getLength(), message.getContent().length);
            throw new RuntimeException("消息不完整");
        }

        try {
            list.add(HessianSerializerUtil.deserialize(message.getContent()));
        } catch (Exception e) {
            throw e;
        }
    }
}
