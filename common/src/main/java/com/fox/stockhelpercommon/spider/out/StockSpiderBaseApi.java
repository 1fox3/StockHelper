package com.fox.stockhelpercommon.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.spider.in.StockSpiderApiBaseInterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.RandomUtil;

/**
 * 股票接口服务基础接口
 *
 * @author lusongsong
 * @date 2021/1/26 16:52
 */
public class StockSpiderBaseApi {
    /**
     * 主
     */
    public static final int CHOOSE_METHOD_PRIMARY = 1;
    /**
     * 随机
     */
    public static final int CHOOSE_METHOD_RANDOM = 2;
    /**
     * 轮询
     */
    public static final int CHOOSE_METHOD_POLL = 3;
    /**
     * 权重
     */
    public static final int CHOOSE_METHOD_WEIGHT = 4;
    /**
     * bean选择方案
     */
    protected int chooseMethod = CHOOSE_METHOD_PRIMARY;
    /**
     * 当前位置
     */
    private int currentPos = 0;
    /**
     * 当前权重计数
     */
    private int currentWeightCount = 0;
    /**
     * 所有实现类
     */
    private LinkedHashMap<String, StockSpiderApiBaseInterface> beanMap = null;
    /**
     * 无用的实现类
     */
    private Map<String, StockSpiderApiBaseInterface> uselessBeanMap = null;
    /**
     * 无用的实现类的失效时间
     */
    private Map<String, Long> uselessTimeMap = null;
    /**
     * 主
     */
    private StockSpiderApiBaseInterface primaryBean = null;
    /**
     * 样例股票
     */
    protected StockVo demoStockVo;
    /**
     * 实现类所在的包
     */
    public List<List<Serializable>> implClassList;

    /**
     * 设置选择方式
     *
     * @param method
     */
    public void setChooseMethod(int method) {
        chooseMethod = method;
    }

    /**
     * 根据全限定类名去寻找一个spring管理的bean实例
     *
     * @return
     */
    private <T extends StockSpiderApiBaseInterface> void getSpiderBean() {
        if (null != implClassList && !implClassList.isEmpty()) {
            beanMap = new LinkedHashMap<String, StockSpiderApiBaseInterface>();
            for (List<Serializable> implList : implClassList) {
                try {
                    String beanName = ((Class) implList.get(0)).getSimpleName();
                    StockSpiderApiBaseInterface bean =
                            ((Class<StockSpiderApiBaseInterface>) implList.get(0)).newInstance();
                    beanMap.put(beanName, bean);
                    if ((boolean) implList.get(1)) {
                        primaryBean = bean;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据规则获取实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends StockSpiderApiBaseInterface> T getBean(Class<T> clazz) {
        if (null == beanMap) {
            getSpiderBean();
        }
        if (chooseMethod == CHOOSE_METHOD_PRIMARY) {
            return (T) this.primaryBean;
        }
        if (null == beanMap || beanMap.isEmpty()) {
            return null;
        }
        //选择之前将失效的bean重新启用
        reuse();
        //按照不同的选取方式计算位置
        switch (chooseMethod) {
            case CHOOSE_METHOD_RANDOM:
                getPosByRandom();
                break;
            case CHOOSE_METHOD_WEIGHT:
                getPosByWeight();
                break;
            default:
                getPosByPool();
        }
        currentPos %= beanMap.values().size();
        searchVerifyBean();
        if (-1 == currentPos) {
            return null;
        }
        System.out.println(beanMap.keySet().toArray()[currentPos]);
        return (T) beanMap.values().toArray()[currentPos];
    }

    /**
     * 随机获取实例位置
     *
     * @return
     */
    private void getPosByRandom() {
        currentPos += RandomUtil.randomInt(0, 100);
    }

    /**
     * 轮询获取实例位置
     *
     * @return
     */
    private void getPosByPool() {
        currentPos += 1;
    }

    /**
     * 根据权重获取实例位置
     *
     * @return
     */
    private void getPosByWeight() {
        currentWeightCount += 1;
        int weight = ((StockSpiderApiBaseInterface) (beanMap.values().toArray()[currentPos])).weight();
        ;
        if (currentWeightCount > weight) {
            currentPos += 1;
            currentWeightCount = 0;
        }
    }

    /**
     * 设置不可用
     *
     * @param beanName
     * @param time
     */
    public void setUseless(String beanName, Long time) {
        if (null != beanMap && beanMap.containsKey(beanName)) {
            if (null == uselessBeanMap) {
                uselessBeanMap = new HashMap<>(1);
            }
            uselessBeanMap.put(beanName, beanMap.get(beanName));
            uselessTimeMap.put(beanName, System.currentTimeMillis() + time);
            beanMap.remove(beanName);
        }
    }

    /**
     * 失效的bean重新启用
     */
    private void reuse() {
        if (null != uselessTimeMap && !uselessTimeMap.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            for (String beanName : uselessTimeMap.keySet()) {
                if (uselessTimeMap.get(beanName) <= currentTime) {
                    beanMap.put(beanName, uselessBeanMap.get(beanName));
                    uselessBeanMap.remove(beanName);
                    uselessTimeMap.remove(beanName);
                }
            }
        }
    }

    /**
     * 判断bean是否适合
     */
    public void searchVerifyBean() {
        int currPos = currentPos;
        while (true) {
            if (verifyBean(beanMap.values().toArray()[currPos])) {
                currentPos = currPos;
                break;
            }
            currPos += 1;
            currPos %= beanMap.values().size();
            if (currPos == currentPos) {
                currentPos = -1;
                break;
            }
        }
    }

    /**
     * 判断bean是否适合
     *
     * @return
     */
    public boolean verifyBean(Object object) {
        return ((StockSpiderApiBaseInterface) object).isSupport(demoStockVo.getStockMarket());
    }
}
