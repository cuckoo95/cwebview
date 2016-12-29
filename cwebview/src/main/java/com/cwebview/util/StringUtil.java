/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2006 All Rights Reserved.
 */
package com.cwebview.util;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理的工具类 
 * @author stone.zhangjl
 * @version $Id: StringUtil.java, v 0.1 2008-8-21 上午10:47:41 stone.zhangjl Exp $
 */
public class StringUtil {

    /** 空字符串。 */
    public static final String EMPTY_STRING = "";
    /**忽略*/
    public static final String TAG_IGNORE = "ignore";
    /**忽略验签*/
    public static final String TAG_IGNORE_SIGN = "ignoreSign";

    /**
     * 比较两个字符串（大小写敏感）。
     * <pre>
     * StringUtil.equals(null, null)   = true
     * StringUtil.equals(null, "abc")  = false
     * StringUtil.equals("abc", null)  = false
     * StringUtil.equals("abc", "abc") = true
     * StringUtil.equals("abc", "ABC") = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     *
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }
    
    /**
	 * Filter string.
	 * @param str
	 * @return
	 */
	public static String f(String str){
		if( isEmpty(str)){
			return "" ;
		}
		return str.trim();
	}

    /**
     * 将字符串转成小写
     * @param str
     * @return
     */
    public static String f2LowerString(String str){
        str = f(str);
        return str.toLowerCase();
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     * <pre>
     * StringUtil.equalsIgnoreCase(null, null)   = true
     * StringUtil.equalsIgnoreCase(null, "abc")  = false
     * StringUtil.equalsIgnoreCase("abc", null)  = false
     * StringUtil.equalsIgnoreCase("abc", "abc") = true
     * StringUtil.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     *
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isBlank(String str) {
    	if(null==str)
    	{
    		return true;
    	}
    	str=str.trim();
    	if(0==str.length())
    	{
    		return true;
    	}
        return false;
    }

    /**
     * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = false
     * StringUtil.isBlank("")        = false
     * StringUtil.isBlank(" ")       = false
     * StringUtil.isBlank("bob")     = true
     * StringUtil.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    /**
     * 检查字符串是否不是<code>null</code>和空字符串<code>""</code>。
     * <pre>
     * StringUtil.isEmpty(null)      = false
     * StringUtil.isEmpty("")        = false
     * StringUtil.isEmpty(" ")       = true
     * StringUtil.isEmpty("bob")     = true
     * StringUtil.isEmpty("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }

    /**
     * 在字符串中查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <pre>
     * StringUtil.indexOf(null, *)          = -1
     * StringUtil.indexOf(*, null)          = -1
     * StringUtil.indexOf("", "")           = 0
     * StringUtil.indexOf("aabaabaa", "a")  = 0
     * StringUtil.indexOf("aabaabaa", "b")  = 2
     * StringUtil.indexOf("aabaabaa", "ab") = 1
     * StringUtil.indexOf("aabaabaa", "")   = 0
     * </pre>
     *
     * @param str 要扫描的字符串
     * @param searchStr 要查找的字符串
     *
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, String searchStr) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        return str.indexOf(searchStr);
    }

    /**
     * 在字符串中查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <pre>
     * StringUtil.indexOf(null, *, *)          = -1
     * StringUtil.indexOf(*, null, *)          = -1
     * StringUtil.indexOf("", "", 0)           = 0
     * StringUtil.indexOf("aabaabaa", "a", 0)  = 0
     * StringUtil.indexOf("aabaabaa", "b", 0)  = 2
     * StringUtil.indexOf("aabaabaa", "ab", 0) = 1
     * StringUtil.indexOf("aabaabaa", "b", 3)  = 5
     * StringUtil.indexOf("aabaabaa", "b", 9)  = -1
     * StringUtil.indexOf("aabaabaa", "b", -1) = 2
     * StringUtil.indexOf("aabaabaa", "", 2)   = 2
     * StringUtil.indexOf("abc", "", 9)        = 3
     * </pre>
     *
     * @param str 要扫描的字符串
     * @param searchStr 要查找的字符串
     * @param startPos 开始搜索的索引值，如果小于0，则看作0
     *
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, String searchStr, int startPos) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        // JDK1.3及以下版本的bug：不能正确处理下面的情况
        if ((searchStr.length() == 0) && (startPos >= str.length())) {
            return str.length();
        }

        return str.indexOf(searchStr, startPos);
    }

    /**
     * 取指定字符串的子串。
     * 
     * <p>
     * 负的索引代表从尾部开始计算。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <pre>
     * StringUtil.substring(null, *, *)    = null
     * StringUtil.substring("", * ,  *)    = "";
     * StringUtil.substring("abc", 0, 2)   = "ab"
     * StringUtil.substring("abc", 2, 0)   = ""
     * StringUtil.substring("abc", 2, 4)   = "c"
     * StringUtil.substring("abc", 4, 6)   = ""
     * StringUtil.substring("abc", 2, 2)   = ""
     * StringUtil.substring("abc", -2, -1) = "b"
     * StringUtil.substring("abc", -4, 2)  = "ab"
     * </pre>
     * </p>
     *
     * @param str 字符串
     * @param start 起始索引，如果为负数，表示从尾部计算
     * @param end 结束索引（不含），如果为负数，表示从尾部计算
     *
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        if (end < 0) {
            end = str.length() + end;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return EMPTY_STRING;
        }

        if (start < 0) {
            start = 0;
        }

        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 检查字符串中是否包含指定的字符串。如果字符串为<code>null</code>，将返回<code>false</code>。
     * <pre>
     * StringUtil.contains(null, *)     = false
     * StringUtil.contains(*, null)     = false
     * StringUtil.contains("", "")      = true
     * StringUtil.contains("abc", "")   = true
     * StringUtil.contains("abc", "a")  = true
     * StringUtil.contains("abc", "z")  = false
     * </pre>
     *
     * @param str 要扫描的字符串
     * @param searchStr 要查找的字符串
     *
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean contains(String str, String searchStr) {
        if ((str == null) || (searchStr == null)) {
            return false;
        }

        return str.indexOf(searchStr) >= 0;
    }

    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断给定的字符串是否为小数
     * @param str
     * @return
     */
    public static boolean isDecimal(String str)
    {
    	if(isBlank(str))
    	{
    		return false;
    	}
    	return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
    }
    

	/**
	 * 如果传入的字符串是null/""  返回""
	 * 否则返回传入的字符串
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str){
		return isBlank(str)?"":str;
	}

    /**
     * 将字符串按照url的方式转义
     * @param target
     * @return
     */
    public static String urlEncode(String target){
        if( isEmpty(target)){
            return null ;
        }
        target = URLEncoder.encode(target);
        return target ;
    }
    
    /**
     * 将字符串中所有字符转成ascii
     * @param value
     * @return
     */
    public static String parse2Ascii(String value){
    	value = StringUtil.f(value);
    	StringBuffer sb = new StringBuffer();
    	int length = value.length() ;
    	String asciiValue = null ;
    	for(int i = 0; i < length; i++ ){
    		asciiValue = (int)value.charAt(i) +"";
    		sb.append(asciiValue);
    	}
    	return sb.toString();
    }

    
    /**
     * 将原字符串中的非(中文，数字，字母)去掉
     * @param orginal
     * @return
     */
    public static String removeSpecifyString(String orginal){
    	String newString = StringUtil.f(orginal) ;
		if (!isEmpty(orginal)) {
			Pattern p = Pattern.compile("[^\u4e00-\u9fa5 a-z A-Z 0-9]");
			Matcher matcher = p.matcher(orginal);
			newString = matcher.replaceAll("");
		}
    	return newString ;
    }
	
	/**
     * 将value转成整数，当无法转时，返回-1
     * @param value
     * @return
     */
    public static int parseInt(String value){
    	try{
    		return Integer.parseInt(value);
    	}catch (Exception e){
    		
    	}
    	return -1 ;
    }
    
    /**
     * 将value转成long，当无法转时，返回-1
     * @param value
     * @return
     */
    public static long parseLong(String value){
    	try{
    		return Long.parseLong(value);
    	}catch (Exception e){
    		
    	}
    	return -1 ;
    }
    /**
     * 识别字符串长度来计算宽度，汉字为1，其他字符为0.5
     * @param str
     * @return
     */
    public static int countString(String str){
    	int length=0;
    	for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((int) c >= 0x4E00 && (int) c <= 0x9FFF) {
				length+=2;
			} else {
				length++;
			}
		}
    	return length/2;
    }
    
    /**
     * 传入的字符串中是否有汉字
     * @param str
     * @return
     */
    public static boolean isHanzi(String str){
    	return Pattern.compile("[\u4E00-\u9FA5]").matcher(str).find();
    }
    
    /**
     * 传入的字符串中是否有空格
     * @param str
     * @return
     */
    public static boolean isHaveBlank(String str){
    	return Pattern.compile("\\s").matcher(str).find();
    }
    
    /**
     * 判断传入的字符串是否为手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone){
    	if(isBlank(phone)){
    		return false;
    	}
    	if(!phone.startsWith("1")||11!=phone.length()||!isNumeric(phone)){
    		return false;
    	}
    	return true;
    }
}
