package com.sly.demo.sensitiveword.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 敏感词初始化
 * @author sly
 * @time 2019年3月1日
 */
public class SensitiveWordInit {
	private static final String ENCODING = "UTF-8";    //字符编码
	
    public HashMap<Object,Object> sensitiveWordMap;
    public Set<String> sensitiveWordset;
    
    public SensitiveWordInit(){
    	
    }
    
    
    /**
     * 初始化关键词
     * @return
     * @author sly
     * @time 2019年3月1日
     */
    public Map<Object,Object> initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }
    
    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     *  中 = {
     *      isEnd = 0
     *      国 = {<br>
     *           isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *                  isEnd = 0
     *                   人 = {
     *                        isEnd = 1
     *                       }
     *               }
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *          isEnd = 0
     *          红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *          }
     *      }
     * @param keyWordSet
     * @author sly
     * @time 2019年3月1日
     */
	@SuppressWarnings("unchecked")
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap<>(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
		String key = null;
		Map<Object, Object> nowMap = null;
		Map<Object, Object> newWorMap = null;
		// 迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			// 关键字 转换为小写
			key = iterator.next().toLowerCase();
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				// 转换成char型
				char keyChar = key.charAt(i);
				// 获取
				Object wordMap = nowMap.get(keyChar);
				// 如果存在该key，直接赋值
				if (wordMap != null) {
					nowMap = (Map<Object, Object>) wordMap;
				} else {
					// 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<Object, Object>();
					// 不是最后一个
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if (i == key.length() - 1) {
					// 最后一个
					nowMap.put("isEnd", "1");
				}
			}
		}
	}
    
    /**
     * _读取敏感词库中的内容，将内容添加到set集合中
     * @return
     * @throws Exception
     * @author sly
     * @time 2019年3月1日
     */
	private Set<String> readSensitiveWordFile() {
		BufferedReader bufferedReader = null;
		try {
			// 读取文件
			// File file = new File("D:\\SensitiveWord.txt");
			File file = new File("src/main/resources/SensitiveWord.txt");

			InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);

			// 文件流是否存在
			if (file.isFile() && file.exists()) {
				sensitiveWordset = new HashSet<String>();
				bufferedReader = new BufferedReader(read);
				String txt = null;
				// 读取文件，将文件内容放入到set中
				while ((txt = bufferedReader.readLine()) != null) {
					sensitiveWordset.add(txt);
				}
			} else {
				// 不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sensitiveWordset;
	}
}

