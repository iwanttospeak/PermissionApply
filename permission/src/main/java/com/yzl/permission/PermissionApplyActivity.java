package com.yzl.permission;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * 申请权限activity
 */
public class PermissionApplyActivity extends AppCompatActivity {

    public static final String DATA_PERMISSION_TYPE = "data_permission_type";
    public static final String DATA_PERMISSIONS = "data_permissions";
    public static final int PERMISSION_TYPE_SINGLE = 1;
    public static final int PERMISSION_TYPE_MULTI = 2;

    private int mPermissionType;
    private static Callback mCallback;
    private static Feedback mFeedback;
    private List<String> mCheckPermissions;

    //单个权限申请
    private static final int REQUEST_CODE_SINGLE = 1;
    //多个权限申请
    private static final int REQUEST_CODE_MULTI = 2;

    public static void setCallBack(Callback callBack) {
        PermissionApplyActivity.mCallback = callBack;
    }
    public static void setFeedBack(Feedback feedback) {
        PermissionApplyActivity.mFeedback = feedback;
    }
    /**
     * 截获按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCallback !=null){
                mCallback.onRefuse();
            }
            if (mFeedback !=null){
                mFeedback.onRefuse(mCheckPermissions);
                mFeedback.onFinish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallback = null;
        mFeedback=null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        if (mPermissionType == PERMISSION_TYPE_SINGLE) { //单个权限申请
            if (mCheckPermissions == null || mCheckPermissions.size() == 0)
                return;
            requestPermission(new String[]{mCheckPermissions.get(0)}, REQUEST_CODE_SINGLE);
        } else {
            String[] permissions = (String[]) mCheckPermissions.toArray();
            requestPermission(permissions,REQUEST_CODE_SINGLE);
        }
    }

    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(PermissionApplyActivity.this, permissions, requestCode);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mPermissionType = intent.getIntExtra(DATA_PERMISSION_TYPE, PERMISSION_TYPE_SINGLE);
        mCheckPermissions = (List<String>) intent.getSerializableExtra(DATA_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_SINGLE:
                String permission = permissions[0];
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<String> result = new ArrayList<>();
                    result.add(permission);
                    onAllow(result);
                } else {
                    List<String> result = new ArrayList<>();
                    result.add(permission);
                    onRefuse(result);
                }
                finish();
                break;
            case REQUEST_CODE_MULTI:
                List<String> guaranteeList = new ArrayList<>();
                List<String> denyList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    //权限允许后，删除需要检查的权限
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        guaranteeList.add(permissions[i]);
                    }else {
                        denyList.add(permissions[0]);
                    }
                }
                //有同意的权限
                if (guaranteeList.size()>0){
                    onAllow(guaranteeList);
                }
                //有拒绝的权限
                if (denyList.size()>0){
                    onRefuse(denyList);
                }
                finishApply();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void onRefuse(List<String> permission) {
        if (mCallback != null){
            mCallback.onRefuse();
            finish();
        }
        if (mFeedback !=null){
            mFeedback.onRefuse(permission);
        }
    }

    private void onAllow(List<String> permissions) {
        if (mCallback != null){
            mCallback.onAllow();
            finish();
        }
        if (mFeedback !=null){
            mFeedback.onAllow(permissions);
        }
    }
    private void finishApply() {
        if (mFeedback !=null){
            mFeedback.onFinish();
        }
    }
}
