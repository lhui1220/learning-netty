package com.example.netty.netty_test;

public class ByteTest {

	public static void main(String[] args) {
		byte l = (byte) (184&0xff);
		byte h = (byte)((184>>8)&0xff);
		System.out.println("l="+l+" h="+h);
		
		byte bval = -2;
		//[-2]原=10000010
		//[-2]反=11111101
		//[-2]补=11111110
		System.out.println("x2="+((bval<<1)&0xff));
		System.out.println(Integer.toBinaryString((bval<<1)&0xff));
		
		byte val = -121;
		System.out.println(val);
		System.out.println((val&0xff));
		
		System.out.println("-121B="+Integer.toBinaryString(-121));
		System.out.println("115B="+Integer.toBinaryString(115));
	}
}
