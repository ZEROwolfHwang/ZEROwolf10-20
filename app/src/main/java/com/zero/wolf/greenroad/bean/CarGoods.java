package com.zero.wolf.greenroad.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/6/27.
 */

public class CarGoods implements Serializable, Comparable {

    /**
     * 是否置顶
     */
    public int top;

    /**
     * 花名
     */
    public String scientific_name;

    public String alias;

    public Bitmap cargoimg;



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long time;

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Bitmap getCargoimg() {
        return cargoimg;
    }

    public void setCargoimg(Bitmap cargoimg) {
        this.cargoimg = cargoimg;
    }

    @Override
    public int compareTo(Object another) {
        if (another == null || !(another instanceof CarGoods)) {
            return -1;
        }

        CarGoods otherSession = (CarGoods) another;
        /**置顶判断 ArrayAdapter是按照升序从上到下排序的，就是默认的自然排序
         * 如果是相等的情况下返回0，包括都置顶或者都不置顶，返回0的情况下要
         * 再做判断，拿它们置顶时间进行判断
         * 如果是不相等的情况下，otherSession是置顶的，则当前session是非置顶的，应该在otherSession下面，所以返回1
         *  同样，session是置顶的，则当前otherSession是非置顶的，应该在otherSession上面，所以返回-1
         * */
        int result = 0 - (top - otherSession.getTop());
        if (result == 0) {
            result = 0 - compareToTime(time, otherSession.getTime());
        }
        return result;
    }

    /**
     * 根据时间对比
     * */
    public static int compareToTime(long lhs, long rhs) {
        Calendar cLhs = Calendar.getInstance();
        Calendar cRhs = Calendar.getInstance();
        cLhs.setTimeInMillis(lhs);
        cRhs.setTimeInMillis(rhs);
        return cLhs.compareTo(cRhs);

    }

}