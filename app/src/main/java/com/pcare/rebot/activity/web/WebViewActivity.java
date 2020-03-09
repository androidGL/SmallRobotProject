package com.pcare.rebot.activity.web;

/**
 * @Author: gl
 * @CreateDate: 2020/3/9
 * @Description:
 */
import android.os.Bundle;

import com.pcare.common.base.BaseWebActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.net.Api;
import com.pcare.common.util.LogUtil;
import com.pcare.rebot.R;

public class WebViewActivity extends BaseWebActivity {
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        LogUtil.i("再次执行onCreate");
        switch (type){
            case Api.WEB_DIAGNOSE:
                mWebView.loadUrl(Api.diagnoseURL);
                break;
            case Api.WEB_GUO:
                mWebView.loadUrl(Api.guoURL);
                break;
            case Api.WEB_MEDICINE:
                mWebView.loadUrl(Api.medicineURL);
                break;
            case Api.WEB_NCP:
                mWebView.loadUrl(Api.ncpURL);
                break;

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    @Override
    public void initView() {
        mWebView = findViewById(R.id.webview);
    }

}
