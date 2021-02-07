package com.fox.stockhelper.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 股票数据工具类
 *
 * @author lusongsong
 * @date 2021/2/2 16:28
 */
public class StockValueUtil {
    /**
     * 整数默认选项
     */
    public static int INT_OPT_DEFAULT = 10;
    /**
     * 价格默认选项
     */
    public static int PRICE_OPT_DEFAULT = 10;
    /**
     * 百分比默认选线
     */
    public static int RATE_OPT_DEFAULT = 7;
    /**
     * 是否显示负号
     */
    public static int OPT_SIGN_PLUS = 1;
    /**
     * 是否显示正号
     */
    public static int OPT_SIGN_MINUS = 2;
    /**
     * 是否显示百分号
     */
    public static int OPT_SIGN_PERCENT = 4;
    /**
     * 是否显示计量单位
     */
    public static int OPT_SCOPE = 8;
    /**
     * 小数掉位数
     */
    public static final int NUMBER_SCALE = 2;
    /**
     * 数值计量单位范围
     */
    public static final List<List<Object>> NUMBER_SCOPE_LIST = new ArrayList<>(Arrays.asList(
            Arrays.asList(new BigDecimal("1"), ""),
            Arrays.asList(new BigDecimal("10000"), "万"),
            Arrays.asList(new BigDecimal("100000000"), "亿"),
            Arrays.asList(new BigDecimal("1000000000000"), "万亿")
    ));

    /**
     * 获取计量范围
     *
     * @param val
     * @return
     */
    private static List<Object> getScope(BigDecimal val) {
        if (null == val) {
            return null;
        }
        val = val.abs();
        List<Object> currentScope = null;
        for (List<Object> scopeList : NUMBER_SCOPE_LIST) {
            currentScope = null == currentScope ? scopeList : currentScope;
            if (val.compareTo((BigDecimal) scopeList.get(0)) < 0) {
                break;
            }
            currentScope = scopeList;
        }
        return currentScope;
    }

    /**
     * 整数文本
     *
     * @param val
     * @return
     */
    public static String longToStr(long val) {
        return longToStr(val, INT_OPT_DEFAULT);
    }

    /**
     * 整数文本
     *
     * @param val
     * @param opt
     * @return
     */
    public static String longToStr(long val, int opt) {
        return bdToStr(new BigDecimal(String.valueOf(val)).setScale(0), opt);
    }

    /**
     * 百分比文本
     *
     * @param val
     * @return
     */
    public static String rateToStr(BigDecimal val) {
        return bdToStr(val, RATE_OPT_DEFAULT);
    }

    /**
     * 浮点数文本
     *
     * @param val
     * @return
     */
    public static String bdToStr(BigDecimal val) {
        return bdToStr(val, PRICE_OPT_DEFAULT);
    }

    /**
     * 浮点数文本
     *
     * @param val
     * @param opt
     * @return
     */
    public static String bdToStr(BigDecimal val, int opt) {
        if (null == val) {
            return "";
        }

        //获取计量范围
        List<Object> scopeList = getScope(val);
        if (null == scopeList) {
            return "";
        }
        BigDecimal scopeNum = (BigDecimal) scopeList.get(0);
        String scopeStr = (String) scopeList.get(1);

        //如果整除则去掉小数
        BigDecimal[] remainder = val.divideAndRemainder(scopeNum);
        int numScale = 0 == scopeNum.compareTo(new BigDecimal("1"))
                && 0 == remainder[1].compareTo(BigDecimal.ZERO) ? val.scale() : NUMBER_SCALE;

        val = val.divide(scopeNum, numScale, BigDecimal.ROUND_HALF_UP);

        return getStr(val, opt, scopeStr);
    }

    /**
     * 拼接字符串
     *
     * @param val
     * @param opt
     * @param scopeStr
     * @return
     */
    public static String getStr(BigDecimal val, int opt, String scopeStr) {
        //与0比较
        int zeroCom = val.compareTo(BigDecimal.ZERO);
        StringBuffer stringBuffer = new StringBuffer();
        if (zeroCom > 0) {//大于0
            if (OPT_SIGN_PLUS == (OPT_SIGN_PLUS & opt)) {
                stringBuffer.append('+');
            }
        } else if (zeroCom < 0) {//小于0
            if (OPT_SIGN_MINUS == (OPT_SIGN_MINUS & opt)) {
                stringBuffer.append('-');
            }
        } else {//等于0

        }

        stringBuffer.append(val.abs().toString());

        if (OPT_SIGN_PERCENT == (OPT_SIGN_PERCENT & opt)) {
            stringBuffer.append('%');
        }

        if (OPT_SCOPE == (OPT_SCOPE & opt)) {
            stringBuffer.append(scopeStr);
        }

        return stringBuffer.toString();
    }
}
