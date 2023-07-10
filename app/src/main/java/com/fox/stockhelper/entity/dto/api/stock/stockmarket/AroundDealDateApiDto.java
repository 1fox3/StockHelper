package com.fox.stockhelper.entity.dto.api.stock.stockmarket;

/**
 * 股市交易日接口
 *
 * @author lusongsong
 * @date 2020/11/30 16:55
 */
public class AroundDealDateApiDto {
    /**
     * 当前交易日
     */
    String last;
    /**
     * 下一个交易日
     */
    String next;
    /**
     * 上一个交易日
     */
    String pre;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }
}
