package com.chenhe.server.nettydemo2.handler;

import com.chenhe.netty2demo.codec.DefaultByteToMessageDecoder;
import com.chenhe.netty2demo.codec.MessageToObjectDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenhe
 * @date 2019-05-10 09:39
 * @desc
 */
public class ServerChannelInitHandler extends ChannelInitializer {

    Logger logger = LoggerFactory.getLogger(ServerChannelInitHandler.class);


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder",new DefaultByteToMessageDecoder(2048));
        pipeline.addLast("resultHandler",new MessageToObjectDecoder());
    }
}
