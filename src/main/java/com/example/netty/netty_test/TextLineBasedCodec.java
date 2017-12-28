package com.example.netty.netty_test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class TextLineBasedCodec extends ByteToMessageCodec<String>{

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out)
			throws Exception {
		//对换行符进行转码
		msg = URLEncoder.encode(msg, "UTF-8");
		msg = msg+'\n';
		byte[] encoded = msg.getBytes();
		out.writeBytes(encoded);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		int index = -1;
		if((index = findMessage(in)) != -1){
			String msg = decodeMessage(in, index);
			msg = URLDecoder.decode(msg, "UTF-8");
			out.add(msg);
		}
	}
	
	private String decodeMessage(ByteBuf in,int end){
		int  len = end-in.readerIndex();
		byte[] dst = new byte[len];
		in.readBytes(dst);
		in.readByte(); //抛弃换行符
		return new String(dst);
	}

	private int findMessage(ByteBuf in){
		int i = in.readerIndex();
		int len = i+in.readableBytes();
		for(;i<len;i++){
			if(in.getByte(i) == '\n'){
				return i;
			}
		}
		return -1;
	}
}
