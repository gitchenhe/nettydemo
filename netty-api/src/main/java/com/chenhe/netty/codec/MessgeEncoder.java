package com.chenhe.netty.codec;

import com.chenhe.entity.MessageBean;
import com.chenhe.util.HessianSerializerUtil;
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
public class MessgeEncoder extends MessageToMessageEncoder<MessageBean> {
    Charset charset = Charset.forName("UTF-8");

    Logger logger = LoggerFactory.getLogger(MessgeEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageBean messageBean, List<Object> list) throws Exception {

        ByteBuf byteBuf = channelHandlerContext.alloc().buffer(10);
        byte[] contents = //messageBean.getContent().toString().getBytes();
        HessianSerializerUtil.serialize(messageBean.getContent());

        //包体长度不包含长度字段本身!!
        int length = 2+2+4+contents.length ;

        //包总长度
        byteBuf.writeInt(length);

        //通信类型
        byteBuf.writeChar(11);

        //协议版本
        byteBuf.writeChar(21);

        //数据长度
        byteBuf.writeInt(contents.length);

        //数据内容
        byteBuf.writeBytes(contents);

        logger.info("对象序列化完成 {},数据长度={},总长度={}", byteBuf,contents.length,length);
        list.add(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("异常");
        cause.printStackTrace();
    }
}
