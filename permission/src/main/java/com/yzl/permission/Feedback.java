package com.yzl.permission;

import java.io.Serializable;
import java.util.List;

/**
 *可以反馈结果的回调
 */
public interface Feedback extends Serializable {
    void onRefuse(List<String> permission);
    //允许
    void onAllow(List<String> permission);
    //权限申请结束
    void onFinish();
}
