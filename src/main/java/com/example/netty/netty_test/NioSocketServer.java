package com.example.netty.netty_test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NioSocketServer {
	
	public static void main(String[] args) {
		new NioSocketServer().listen(8000);
	}

	public void listen(int port){
		//boss用于监听客户端连接
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap starter = new ServerBootstrap();
			starter.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
//							ch.pipeline().addLast(new TextLineBasedCodec());
							ch.pipeline().addLast(new TypeConvertCodec());
							ch.pipeline().addLast(new PingPongServerHandler());
						}
					})
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			
					ChannelFuture future = starter.bind(port).sync(); //listen at port
					System.out.println("server listen at "+port);
					future.channel().closeFuture().sync();
					System.out.println("server closed.");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}
	
	/**
	 * I/O处理
	 * @author Administrator
	 *
	 */
	private class PingPongServerHandler extends ChannelInboundHandlerAdapter{
		
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			super.handlerAdded(ctx);
		}
		
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			super.handlerRemoved(ctx);
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			Integer i = (Integer) msg;
			System.out.println("recv ival="+i.intValue());
//			String str = (String) msg;
//			str = "pong "+str.split(" ")[1];
//			ctx.writeAndFlush(str);
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
//			super.exceptionCaught(ctx, cause);
			System.out.println("ERR:"+cause.getMessage());
			ctx.close();
		}
		
	}
	
	private class PingPongServerOutHandler extends ChannelOutboundHandlerAdapter{
		
	}
}
