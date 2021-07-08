package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.FragmentEnrollmentBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseFragment;

import java.util.HashMap;

import javax.inject.Inject;

public class NewEnrollmentFrag extends BaseFragment implements NewEnrollmentFragMvpView {
    @Inject
    NewEnrollmentFragMvpPresenter<NewEnrollmentFragMvpView> mpresenter;
    private FragmentEnrollmentBinding fragmentEnrollmentBinding;
    private float m_downX;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentEnrollmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_enrollment, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(NewEnrollmentFrag.this);
        setHasOptionsMenu(true);
        return fragmentEnrollmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    protected void setUp(View view) {
        WebSettings webSettings = fragmentEnrollmentBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("authorization", mpresenter.getAccessToken());
        initWebView();
        fragmentEnrollmentBinding.webView.loadUrl("https://www.google.com/",map);
    }

    @Override
    public void anotherizedToken() {

    }


    private void initWebView() {
        fragmentEnrollmentBinding.webView.setWebChromeClient(new MyWebChromeClient(getBaseActivity()));
        fragmentEnrollmentBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                fragmentEnrollmentBinding.progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                fragmentEnrollmentBinding.webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                fragmentEnrollmentBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                fragmentEnrollmentBinding.progressBar.setVisibility(View.GONE);
            }
        });
        fragmentEnrollmentBinding.webView.clearCache(true);
        fragmentEnrollmentBinding.webView.clearHistory();
        fragmentEnrollmentBinding.webView.getSettings().setJavaScriptEnabled(true);

    }


    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }
}
