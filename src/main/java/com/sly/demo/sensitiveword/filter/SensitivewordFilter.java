package com.sly.demo.sensitiveword.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sly.demo.sensitiveword.init.SensitiveWordInit;

/**
 * 敏感词过滤器
 * 
 * @author sly
 * @time 2019年3月1日
 */
public class SensitivewordFilter {
	private Map<Object, Object> sensitiveWordMap = null;
	public Set<String> sensitiveWordset = null;
	// 无效字符:空格、*、#、@
	public char invaChar[] = { ' ', '*', '#', '@' };
	// 最小匹配规则
	public static int minMatchType = 1;
	// 最大匹配规则
	public static int maxMatchType = 2;

	/**
	 * _构造函数，初始化敏感词库
	 * 
	 * @author sly
	 * @time 2019年3月1日
	 */
	public SensitivewordFilter() {
		SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
		sensitiveWordMap = sensitiveWordInit.initKeyWord();
		sensitiveWordset = sensitiveWordInit.sensitiveWordset;
	}

	/**
	 * _获取文字中的敏感词
	 * 
	 * @param txt
	 * @param matchType
	 * @return
	 * @author sly
	 * @time 2019年3月1日
	 */
	public Set<String> getSensitiveWord(String txt, int matchType) {
		Set<String> sensitiveWordList = new HashSet<String>();
		for (int i = 0; i < txt.length(); i++) {
			// 判断是否包含敏感字符
			int length = CheckSensitiveWord(txt, i, matchType);
			// 存在,加入list中
			if (length > 0) {
				String sensitiveWord = txt.substring(i, i + length);
				// 去除两边的无效字符
				sensitiveWord = trimInvaChar(sensitiveWord);
				sensitiveWordList.add(sensitiveWord);
				// 减1的原因，是因为for会自增
				i = i + length - 1;
			}
		}

		return sensitiveWordList;
	}

	/**
	 * _替换敏感字字符
	 * 
	 * @param txt
	 * @param matchType
	 * @param replaceChar
	 * @return
	 * @author sly
	 * @time 2019年3月1日
	 */
	public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
		String resultTxt = txt;
		// 获取所有的敏感词
		Set<String> set = getSensitiveWord(txt, matchType);
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}

		return resultTxt;
	}

	/**
	 * _获取替换字符串
	 * 
	 * @param replaceChar
	 * @param length
	 * @return
	 * @author sly
	 * @time 2019年3月1日
	 */
	private String getReplaceChars(String replaceChar, int length) {
		String resultReplace = replaceChar;
		for (int i = 1; i < length; i++) {
			resultReplace += replaceChar;
		}

		return resultReplace;
	}

	/**
	 * _检查文字中是否包含敏感字符，检查规则如下
	 * 
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return
	 * @author sly
	 * @time 2019年3月1日
	 */
	@SuppressWarnings("unchecked")
	public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
		// 敏感词结束标识位：用于敏感词只有1位的情况
		boolean flag = false;
		// 匹配标识数默认为0
		int matchFlag = 0;
		char word = 0;
		Map<Object, Object> nowMap = sensitiveWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.charAt(i);

			// 无效字符跳过:空格、*、#、@
			if (isInvaChar(word)) {
				matchFlag++;
				continue;
			}

			if (txt.charAt(i) >= 'A' && txt.charAt(i) <= 'Z') {
				// 如果字母是大写(我们需要用小写去查)
				word = Character.toLowerCase(word);
			}
			;

			// 获取指定key
			nowMap = (Map<Object, Object>) nowMap.get(word);
			// 存在，则判断是否为最后一个
			if (nowMap != null) {
				// 找到相应key，匹配标识+1
				matchFlag++;
				// 如果为最后一个匹配规则,结束循环，返回匹配标识数
				if ("1".equals(nowMap.get("isEnd"))) {
					// 结束标志位为true
					flag = true;
					// 最小规则，直接返回,最大规则还需继续查找
					if (SensitivewordFilter.minMatchType == matchType) {
						break;
					}
				}
			} else {
				// 不存在，直接返回
				break;
			}
		}
		if (matchFlag < 2 || !flag) {
			// 长度必须大于等于1，为词
			matchFlag = 0;
		}
		return matchFlag;
	}

	/**
	 * _判断是否为无效字符
	 * 
	 * @param ch
	 * @return 是无效字符返回true 不是无效字符返回false
	 * @author sly
	 * @time 2019年3月4日
	 */
	private boolean isInvaChar(char ch) {
		for (int i = 0; i < invaChar.length; i++) {
			if (ch == invaChar[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * _截取敏感词两边的无效字符
	 * 
	 * @param sensitiveWord
	 * @return
	 * @author sly
	 * @time 2019年3月7日
	 */
	private String trimInvaChar(String sensitiveWord) {
		if (sensitiveWord == null || sensitiveWord.length() == 0) {
			return "";
		}
		int start = 0;
		int len = sensitiveWord.length();
		char[] strchars = sensitiveWord.toCharArray();

		while ((start < len) && isInvaChar(strchars[start])) {
			start++;
		}
		while ((start < len) && isInvaChar(strchars[len - 1])) {
			len--;
		}
		return sensitiveWord.substring(start, len);
	}
	
}
