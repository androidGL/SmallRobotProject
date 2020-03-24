package com.pcare.common.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: gl
 * @CreateDate: 2020/3/19
 * @Description:
 */
public class CommonToast {
    public static final  int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int WARRING = 2;
    @IntDef({SUCCESS,FAILED,WARRING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tag{}

    public static void showShortToast(Context context,String msg,@Tag int tag){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context,String msg,int tag){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
