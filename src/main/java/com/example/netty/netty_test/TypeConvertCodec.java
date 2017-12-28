package com.example.netty.netty_test;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class TypeConvertCodec extends ByteToMessageCodec<Integer> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out)
			throws Exception {
		byte[] bytes = new byte[4];
		int ival = msg.intValue();
		//小端字节序编码
		for(int i=0;i<bytes.length;i++){
			bytes[i] = (byte) (ival>>(i*8)&0xff);
		}
		out.writeBytes(bytes);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes() < 4){
			return ;
		}
		byte[] dst = new byte[4];
		in.readBytes(dst);
		
		int ival = 0;
		for(int i=0;i<dst.length;i++){
			ival += (dst[i]&0xff)<<(i*8);
		}
		out.add(Integer.valueOf(ival));
	}

}
