package work.layman.nettyIPLimit.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName NettyServer
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/29 10:10
 * @Version 3.0
 **/
@Component
public class NettyServer {

//    @Value("${netty.port}")
    private static final int port = 12345;

    private EventLoopGroup mainGroup;

    private EventLoopGroup workGroup;

    private ServerBootstrap serverBootstrap;

    private ChannelFuture future;

    private static class SingletonNettyServer {
        static final NettyServer instance = new NettyServer();
    }

    public static NettyServer getInstance() {
        return SingletonNettyServer.instance;
    }

    public NettyServer() {
        mainGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyInit());
    }

    public void start() {
        this.future = serverBootstrap.bind(port);
    }

}
