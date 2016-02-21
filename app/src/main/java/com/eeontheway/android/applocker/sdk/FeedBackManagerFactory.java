package com.eeontheway.android.applocker.sdk;

import android.content.Context;

import com.eeontheway.android.applocker.main.IFeedBack;
import com.eeontheway.android.applocker.sdk.bmob.BombFeedBackManager;
import com.eeontheway.android.applocker.utils.Configuration;

import java.security.InvalidParameterException;

/**
 * 反馈信息管理器工厂类
 *
 * @author lishutong
 * @version v1.0
 * @Time 2016-2-8
 */
public class FeedBackManagerFactory {
    /**
     * 创建反馈管理器
     * @return 反馈管理器的接口
     */
    public static IFeedBack create (Context context) {
        IFeedBack feedBackManager = null;

        switch (Configuration.FeedBackMangerType) {
            case Configuration.BMOB_FEEDBACK:
                feedBackManager = new BombFeedBackManager(context);
                break;
            default:
                throw new InvalidParameterException("Unkown Manger type");
        }
        return feedBackManager;
    }
}
