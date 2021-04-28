package com.nivelle.rpc.netty.protocol;

import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 说明：自定义协议客户端
 **/
public class ProtocolClient {

    private String host;
    private int port;

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 6;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    /**
     *
     */
    public ProtocolClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            "decoder",
                            new ProtocolDecoder(MAX_FRAME_LENGTH,
                                    LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
                                    LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                    ch.pipeline().addLast("encoder", new ProtocolEncoder());
                    ch.pipeline().addLast(new ProtocolClientHandler());

                }
            });

            // 启动客户端
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            while (true) {

                // 发送消息给服务器
                MsgObject msg = new MsgObject();
                MsgHeader msgHeader = new MsgHeader();
                msgHeader.setMagic((byte) 0x01);
                msgHeader.setMsgType((byte) 0x01);
                msgHeader.setReserve((short) 0);
                msgHeader.setSn((short) 0);
                String body = "床前明月光疑是地上霜";
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < 2700; i++) {
                    sb.append(body);
                }

                byte[] bodyBytes = sb.toString().getBytes(
                        Charset.forName("utf-8"));
                int bodySize = bodyBytes.length;
                msgHeader.setLen(bodySize);

                msg.setProtocolHeader(msgHeader);
                msg.setBody(sb.toString());

                f.channel().writeAndFlush(msg);
                Thread.sleep(2000);
            }
            // 等待连接关闭
            // f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static final int POOL_SIZE_SEND = 100;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        Executor executor = Executors.newFixedThreadPool(POOL_SIZE_SEND);
        for (int i = 0; i < POOL_SIZE_SEND; i++) {
            executor.execute(new ClientTask());
            Thread.sleep(100);
        }

    }


}
