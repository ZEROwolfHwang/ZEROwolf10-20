package com.zero.wolf.greenroad.manager;

import android.content.Context;

import com.zero.wolf.greenroad.tools.SPUtils;

import static org.litepal.LitePalApplication.getContext;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CarNumberCount {

    public final Context mContext;

    public CarNumberCount(Context context) {
        mContext = context;
    }


    /**
     * 将未上传车辆的数字减去一
     */
    public static void CarNumberCut(Context mContext) {
        SPUtils.cut_one(mContext, SPUtils.CAR_NOT_COUNT);
        int cra_not_count = (int) SPUtils.get(mContext, SPUtils.CAR_NOT_COUNT, 0);
    }
    /**
     * 将车辆的总数加1
     */
    public static void CarNumberAdd(Context mContext) {
        SPUtils.add_one(getContext(), SPUtils.CAR_COUNT);

        SPUtils.add_one(getContext(), SPUtils.CAR_NOT_COUNT);
    }


}
