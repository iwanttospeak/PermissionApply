package com.yzl.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * des: ease权限盛情工具类
 */
public class PermissionUtil {


//    /**
//     * 根据请求code在类中查找注解方法
//     * @param clazz
//     * @param annotation 注解
//     * @param requestCode 返回值
//     */
//    public static <A extends Annotation> Method findMethodWithRequestCode(Class clazz, Class<A> annotation, int requestCode) {
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (method.getAnnotation(annotation) != null &&//有注解方法且注解名称和请求code相同
//                    isEqualRequestCodeFromAnnotation(method, annotation, requestCode)) {
//                return method;
//            }
//        }
//        return null;
//    }

//    public static boolean isEqualRequestCodeFromAnnotation(Method m, Class clazz, int requestCode) {
//        if (clazz.equals(OnPermissionFailed.class)) {
//            return requestCode == m.getAnnotation(OnPermissionFailed.class).value();
//        } else if (clazz.equals(OnPermissionSuccess.class)) {
//            return requestCode == m.getAnnotation(OnPermissionSuccess.class).value();
//        } else {
//            return false;
//        }
//    }

//    @TargetApi(value = Build.VERSION_CODES.M)
//    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
//        List<String> denyPermissions = new ArrayList<>();
//        for (String value : permission) {
//            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
//                denyPermissions.add(value);
//            }
//        }
//        return denyPermissions;
//    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Context context, List<String> permissions) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (ContextCompat.checkSelfPermission(context,value)!= PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

//    @TargetApi(value = Build.VERSION_CODES.M)
//    public static List<String> findDeniedPermissions(Fragment fragment, String... permission) {
//        List<String> denyPermissions = new ArrayList<>();
//        for (String value : permission) {
//            if (fragment.getActivity() == null || fragment.getActivity().checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
//                denyPermissions.add(value);
//            }
//        }
//        return denyPermissions;
//    }

    /**
     * 获取敏感权限列表
     */
    public static String[] getPermissions(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = pi.requestedPermissions;
            Set<String> result = new HashSet<>();
            for (String permission : permissions
                    ) {
                if (checkIsDanderPermission(permission)) {
                    result.add(permission);
                }
            }
            return result.toArray(new String[result.size()]);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    public static boolean checkIsDanderPermission(String permission) {
        if (permission.equals("android.permission.WRITE_CONTACTS")
                || permission.equals("android.permission.GET_ACCOUNTS")
                || permission.equals("android.permission.READ_CONTACTS")
                //联系人权限组
                || permission.equals("android.permission.READ_CALL_LOG")
                || permission.equals("android.permission.READ_PHONE_STATE")
                || permission.equals("android.permission.CALL_PHONE")
                || permission.equals("android.permission.WRITE_CALL_LOG")
                || permission.equals("android.permission.USE_SIP")
                || permission.equals("android.permission.PROCESS_OUTGOING_CALLS")
                || permission.equals("com.android.voicemail.permission.ADD_VOICEMAIL")
                //通话权限组
                || permission.equals("android.permission.READ_CALENDAR")
                || permission.equals("android.permission.WRITE_CALENDAR")
                //日程权限组
                || permission.equals("android.permission.CAMERA")
                //相机权限
                || permission.equals("android.permission.BODY_SENSORS")
                //？
                || permission.equals("android.permission.ACCESS_FINE_LOCATION")
                || permission.equals("android.permission.ACCESS_COARSE_LOCATION")
                //定位权限组
                || permission.equals("android.permission.READ_EXTERNAL_STORAGE")
                || permission.equals("android.permission.WRITE_EXTERNAL_STORAGE")
                //读写存储权限
                || permission.equals("android.permission.RECORD_AUDIO")
                //录音权限
                || permission.equals("android.permission.READ_SMS")
                || permission.equals("android.permission.RECEIVE_WAP_PUSH")
                || permission.equals("android.permission.RECEIVE_MMS")
                || permission.equals("android.permission.RECEIVE_SMS")
                || permission.equals("android.permission.SEND_SMS")
                || permission.equals("android.permission.READ_CELL_BROADCASTS")
            //短信权限组
                ) {
            return true;
        }
        return false;
    }
}
