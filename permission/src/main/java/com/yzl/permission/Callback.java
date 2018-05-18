package com.yzl.permission;

import java.io.Serializable;

/**
 *结果回调
 */
public interface Callback extends Serializable {
    void onRefuse();
    //允许
    void onAllow();
}
