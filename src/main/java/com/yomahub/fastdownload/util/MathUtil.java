package com.yomahub.fastdownload.util;

import java.math.BigDecimal;

public class MathUtil {
	//格式化小数
	public static double formatDouble(double num,int round){
		BigDecimal bd = new BigDecimal(num);
		bd = bd.setScale(round, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static void main(String[] args){
		System.out.println(MathUtil.formatDouble(4.6748, 3));
	}
}
