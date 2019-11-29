package work.layman.nettyIPLimit.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @ClassName HeartBeatHandler
 * @Description TODO 心跳检测handler
 * @Author lizhangjun
 * @Data 2019/5/10 9:54
 * @Version 3.0.0-beta.1
 **/
public class HeartBeatHandler extends ChannelInboundHandlerAdapter
{

    /**
     * @Author lizhangjun
     * @Description 用户事件触发
     * @Date 2019/5/12 8:53
     * @Param [ctx, evt]
     * @return void
     **/
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent event = (IdleStateEvent)evt;
            //channel读空闲
            if (event.state() == IdleState.READER_IDLE)
            {
                System.out.println("客户端[" + ctx.channel().id() +"]进入读空闲。。。");
            }
            //channel写空闲
            else if (event.state() == IdleState.WRITER_IDLE)
            {
                System.out.println("客户端[" + ctx.channel().id() + "]进入写空闲。。。");
            }
            //channel读写空闲
            else if (event.state() == IdleState.ALL_IDLE)
            {
                //关闭channel
                Channel channel = ctx.channel();
                channel.close();
            }
        }
    }
}