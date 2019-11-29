package work.layman.nettyIPLimit.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ClassName NettyInit
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/29 10:22
 * @Version 3.0
 **/
public class NettyInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();


        //websocket基于http协议，所以要有http编解码器
        //酒店大楼负责编解码的入口
        pipeline.addLast(new HttpServerCodec());

        //对大数据流的支持
        //酒店大楼负责大数据流处理的入口
        pipeline.addLast(new ChunkedWriteHandler());

        //对httpMessage进行聚合，聚合成FullHttpResponse/FullHttpRequest
        //几乎在netty中的编程都会用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*60));

        /***********************************增加心跳检测机制***********************************/

        //针对客户端，如果在1分钟没有向服务端发送读写心跳（ALL），则主动断开，如果是读空闲或者写空闲，不处理，20，40，60
        pipeline.addLast(new IdleStateHandler(20000 , 40000 , 60000));
        //自定义空闲状态检测(自定义心跳检测handler)
        pipeline.addLast(new HeartBeatHandler());

        //handler会处理一些繁重的复杂事件
        //会处理握手动作：handshaking(close , ping , pong)
        //对于websocket来讲，都是以frame进行传输的，不同的数据类型对应的frame也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        /***********************************业务相关handler***********************************/

        //自定义handler()
        pipeline.addLast(new NettyHandler());
    }
}
