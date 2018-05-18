package com.yzl.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 动态权限申请门面类,支持全局申请权限
 */
public class PermissionApply {
    private final Context mContext;
    //用户申请的权限
    private List<String> permissions;
    //回调
    private Callback mCallback;
    private Feedback mFeedback;
    //权限请求类型
    private int mPermissionType;
    //需要检查的权限
    private List<String> mCheckPermissions;
    private PermissionApply(Context context) {
        this.mContext = context;
    }
    /**
     * 在fragment 中申请的时候
     */
    public static PermissionApply with(Context context) {
        return new PermissionApply(context);
    }
    public PermissionApply permissions(List<String> permissions) {
        this.permissions = permissions;
        return this;
    }
    /**
     * 单个权限申请
     */
    public PermissionApply permission(String permission) {
        permissions = new ArrayList<>();
        permissions.add(permission);
        return this;
    }
    /**
     * 发起权限请求
     */
    public void request(Callback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null)
                callback.onAllow();
            return;
        }
        if (null == this.permissions) {
            //获取敏感权限列表
            this.permissions = Arrays.asList(PermissionUtil.getPermissions(mContext));
        }
        List<String> deniedPermissions = PermissionUtil.findDeniedPermissions(mContext, permissions);
        if (deniedPermissions.size() <= 0) {
            if (callback != null)
                callback.onAllow();
            return;
        }
        mCallback = callback;
        requestPermissions(deniedPermissions);
    }
    /**
     * 发起权限请求
     */
    public void request(Feedback feedback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (feedback != null)
                feedback.onAllow(permissions);
            return;
        }
        if (null == this.permissions) {
            //获取敏感权限列表
            this.permissions = Arrays.asList(PermissionUtil.getPermissions(mContext));
        }
        List<String> deniedPermissions = PermissionUtil.findDeniedPermissions(mContext, permissions);
        if (deniedPermissions.size() <= 0) {
            if (feedback != null)
                feedback.onAllow(permissions);
            return;
        }
        mFeedback = feedback;
        requestPermissions(deniedPermissions);
    }

    /**
     * 请求权限
     */
    private void requestPermissions(List<String> permissions) {
        if (permissions.size() ==1){
            mPermissionType = PermissionApplyActivity.PERMISSION_TYPE_SINGLE;
        }else {
            mPermissionType = PermissionApplyActivity.PERMISSION_TYPE_MULTI;
        }
        mCheckPermissions = new ArrayList<>();
        mCheckPermissions.addAll(permissions);
        startActivity();
    }

    private void startActivity() {
        if (mFeedback !=null){
            PermissionApplyActivity.setFeedBack(mFeedback);
        }
        if (mCallback !=null){
            PermissionApplyActivity.setCallBack(mCallback);
        }
        Intent intent = new Intent(mContext, PermissionApplyActivity.class);
        intent.putExtra(PermissionApplyActivity.DATA_PERMISSION_TYPE, mPermissionType);
        intent.putExtra(PermissionApplyActivity.DATA_PERMISSIONS, (Serializable) mCheckPermissions);
        mContext.startActivity(intent);

    }

}
