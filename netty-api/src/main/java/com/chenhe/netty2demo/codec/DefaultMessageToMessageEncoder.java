package com.chenhe.netty2demo.codec;

import com.chenhe.netty2demo.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author chenhe
 * @date 2019-05-10 11:05
 * @desc 对象转移成二进制
 */
public class DefaultMessageToMessageEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
        byteBuf.writeInt(message.getTag());
        byteBuf.writeInt(message.getCommandCode());
        byteBuf.writeInt(message.getVersion());
        byteBuf.writeInt(message.getLength());
        byteBuf.writeBytes(message.getContent());

        list.add(byteBuf);
    }
}
