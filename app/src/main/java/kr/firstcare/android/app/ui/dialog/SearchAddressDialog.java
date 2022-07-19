package kr.firstcare.android.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpSecondFragment;
import kr.firstcare.android.app.ui.fragment.userinfo.ModifyUserInfoFragment;


/**
 * ClassName            SearchAddressDialog
 * Created by JSky on   2020-06-22
 *
 * Description          도로명주소검색 다이얼로그
 */

public class SearchAddressDialog extends Dialog {
    public static final String TAG = "fc_debug";
    private static String BASEURL = "";

    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.wv_webView)
    WebView wv_webView;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    private Handler webViewHandler;
    private int parentType;


    public SearchAddressDialog(Context context, String title, String baseUrl, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        BASEURL = baseUrl;
        this.parentType = parentType;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_address);
        ButterKnife.bind(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        webViewHandler = new Handler();

    }

    public void init_webView() {

        // JavaScript 허용
        wv_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        wv_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        wv_webView.addJavascriptInterface(new AndroidBridge(), "FirstCare");

        // web client 를 chrome 으로 설정
        wv_webView.setWebChromeClient(new WebChromeClient());

        wv_webView.getSettings().setDatabaseEnabled(false);
        wv_webView.getSettings().setAllowFileAccess(false);
        wv_webView.getSettings().setDomStorageEnabled(false);
        wv_webView.getSettings().setAppCacheEnabled(false);

        // webview url load. php 파일 주소
        wv_webView.loadUrl(BASEURL);

    }

    private class AndroidBridge extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith(BASEURL)) {
                url = BASEURL + "postCode.html";
            }

            view.loadUrl(url);

            return true;

        }

        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            webViewHandler.post(new Runnable() {

                /*선택한 주소 값 가져오기*/
                @Override
                public void run() {
                    if (arg1 != null) {
                        Constant.LOG(TAG, "arg1 : " + String.format("(%s)", arg1));
                    }
                    if (arg2 != null) {
                        Constant.LOG(TAG, "arg2 : " + String.format("%s", arg2));
                    }
                    if (arg3 != null) {
                        Constant.LOG(TAG, "arg3 : " + String.format("%s", arg3));
                    }

                    /*arg1  (우편번호) 사용 안함*/

                    dismiss();

                    String address = String.format("%s %s", arg2, arg3);
                    if(parentType == 0){

                        TypeVSignUpSecondFragment.instance.setAddress(arg1, address);

                    }else if(parentType == 1){

                        ModifyUserInfoFragment.instance.setAddress(arg1, address);

                    }


                }

            });

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);



    }


}
