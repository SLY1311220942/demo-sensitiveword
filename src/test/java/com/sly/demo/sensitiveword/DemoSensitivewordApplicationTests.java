package com.sly.demo.sensitiveword;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoSensitivewordApplicationTests {

	/**
	 * _敏感词过滤测试
	 * 
	 * @author sly
	 * @time 2019年3月7日
	 */
	@Test
	public void sensitiveWordFilterTest() {

		SensitivewordFilter filter = new SensitivewordFilter();
		System.out.println("敏感词库数量：" + filter.sensitiveWordset.size());

		String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节红客联盟，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
				+ "然后法轮 我们的扮####演的角色就是跟随着主人公的喜红客 #联盟####融资怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
				+ "难过就躺在某一个人的怀里尽aaaaaaaaaaaaaaaaaaaaaaassssssssssssssfwefasfasfasefasgasgagsfgsdfgsdgaegagagaagg情的阐述心你妈扉或者手"
				+ "机卡复制器一个人一杯fu#ck红酒一部电影在夜 深人静的晚上，关上电话静静的发呆着FInancing。";
		
		System.out.println("待检测语句字数：" + string.length());

		long beginTime = System.currentTimeMillis();
		Set<String> set = filter.getSensitiveWord(string, SensitivewordFilter.maxMatchType);

		long endTime = System.currentTimeMillis();
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);

		System.out.println("总共消耗时间为：" + (endTime - beginTime));
	}

	
	
	/**
	 * _trim测试
	 * 
	 * @author sly
	 * @time 2019年3月7日
	 */
	@Test
	public void contextLoads() {
		String str = "-# asdf- #";
		String trim = trim(trim(trim(str)));
		System.out.println(trim);
	}

	public String trim(String str) {

		if (str == null || str.length() == 0) {
			return str;
		}
		int start = 0;
		int len = str.length();
		char[] strchars = str.toCharArray();

		while ((start < len) && isInvaChar(strchars[start])) {
			start++;
		}
		while ((start < len) && isInvaChar(strchars[len - 1])) {
			len--;
		}
		return str.substring(start, len);
	}

	private boolean isInvaChar(char ch) {
		char invaChar[] = { ' ', '*', '#', '@', '-' };
		for (int i = 0; i < invaChar.length; i++) {
			if (ch == invaChar[i]) {
				return true;
			}
		}
		return false;
	}

}
