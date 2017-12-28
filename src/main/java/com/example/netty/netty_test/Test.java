package com.example.netty.netty_test;

public class Test {

	public static void main(String[] args) {
		int val = Integer.parseInt("0A", 16);
		System.out.println("val="+val);
		byte[] data = {85, -88, 27, -89, -111, 39, 119, 2};
        final StringBuilder stringBuilder = new StringBuilder(data.length);
        for(byte byteChar : data)
            stringBuilder.append(String.format("%02X ", byteChar));
        System.out.println("str="+stringBuilder.toString());
	}
}
