package com.example.netty.netty_test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NioSocketClient {
	
	private Channel channel;
	
	private int sendCount;
	private int recvCount;

	public static void main(String[] args) {
		new NioSocketClient().connect();
	}
	
	public void startMessageThread(){
		new Thread(){
			public void run() {
				while(sendCount < Integer.MAX_VALUE){
					try {
//						String msg = "ping \nfa"+(++sendCount);
//						channel.writeAndFlush(msg).sync();
//						System.out.println("send msg="+msg);
						channel.writeAndFlush(Integer.valueOf(++sendCount));
						System.out.println("send ival="+sendCount);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}.start();
	}
	
	public void connect(){
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			Bootstrap starter = new Bootstrap();
			
			starter.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
//							ch.pipeline().addLast(new TextLineBasedCodec());
							ch.pipeline().addLast(new TypeConvertCodec());
							ch.pipeline().addLast(new PingPongClientHandler());
							System.out.println("initChannel.");
						}
						
					});
			ChannelFuture f = starter.connect("localhost", 8000).sync();
			channel = f.channel();
			startMessageThread();
			System.out.println("connect success.");
			f.channel().closeFuture().sync();
			System.out.println("connection closed.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			workerGroup.shutdownGracefully();
		}

	}
	
	private class PingPongClientHandler extends ChannelInboundHandlerAdapter{
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			System.out.println("recv msg="+msg);
		}
	}
}
