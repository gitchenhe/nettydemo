package com.chenhe.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author chenhe
 * @date 2019-05-09 14:13
 * @desc
 */
public class MessgeEncoder2 extends MessageToMessageEncoder<ByteBuf> {
    Charset charset = Charset.forName("UTF-8");

    Logger logger = LoggerFactory.getLogger(MessgeEncoder2.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf messageBean, List<Object> list) throws Exception {
        logger.info("对象序列化完成2");
    }
}
