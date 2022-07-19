package kr.firstcare.android.app.ui.activity;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.SignUpFragmentAdapter;
import kr.firstcare.android.app.data.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            SignUpActivity
 * Created by JSky on   2020-06-22
 * 
 * Description          회원가입 정보 입력
 */

public class SignUpActivity extends BaseActivity {

    int selectSignUpType = -1;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.vp_fragmentPager)
    ViewPager2 vp_fragmentPager;
    @BindView(R.id.ic_indicator)
    CircleIndicator ic_indicator;

    private FragmentStateAdapter pagerAdapter;
    private int numPage = 2;
    private int nowPage = 0;

    private String[] titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        titleList = new String[] { getString(R.string.SignUpActivity_text_signUpTitle_01), getString(R.string.SignUpActivity_text_signUpTitle_02) };

        pagerAdapter = new SignUpFragmentAdapter(this, numPage);

        vp_fragmentPager.setAdapter(pagerAdapter);
        vp_fragmentPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp_fragmentPager.setCurrentItem(2);
        vp_fragmentPager.setOffscreenPageLimit(2);
        vp_fragmentPager.setUserInputEnabled(false);

        ic_indicator.setWithViewPager2(vp_fragmentPager,false);
        ic_indicator.setItemCount(numPage);
        ic_indicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE);

        vp_fragmentPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {

                    vp_fragmentPager.setCurrentItem(position);

                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nowPage = position;
                Constant.LOG(TAG, "nowPage" + nowPage + "position" + position + " titleList[position]" + titleList[position]);
                tv_title.setText(titleList[position]);

            }

        });

    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();

        selectSignUpType = PreferenceManager.getInt(mContext, "selectSignUpType");
        if (selectSignUpType > -1) {
            Constant.LOG(TAG, "selectSignUpType : " + selectSignUpType);
        }

    }   //  onResume

    @Override
    public void onBackPressed() {
        switch (vp_fragmentPager.getCurrentItem()){

            case 0:
                finish();
                break;

            case 1:
                vp_fragmentPager.setCurrentItem(0);
                break;
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    /*회원가입 정보 입력(기본정보1,2) 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        /**
         *  0   회원가입 프래그먼트 1
         *  1   회원가입 프래그먼트 2
         */

        switch (num) {

            case 0:
                vp_fragmentPager.setCurrentItem(0);

                break;

            case 1:
                vp_fragmentPager.setCurrentItem(1);

                break;

        }

    }

    /*다음 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        onBackPressed();
    }




}