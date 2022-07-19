package kr.firstcare.android.app.ui.activity;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.DefaultSpinnerAdapter;
import kr.firstcare.android.app.adapter.UserInfoTabFragmentAdapter;
import kr.firstcare.android.app.ui.view.TabIndicatorRectF;

/**
 * ClassName            UserInfoActivity
 * Created by JSky on   2020-07-06
 *
 * Description          기본 정보
 */

public class UserInfoActivity extends BaseActivity {



    @BindView(R.id.tab_userInfoTabMenu)
    TabLayout tab_userInfoTabMenu;
    @BindView(R.id.vp_userInfoPager)
    ViewPager2 vp_userInfoPager;

    private FragmentStateAdapter pagerAdapter;
    private int numPage = 2;
    private int nowPage = 0;

    /*권한 선택 스피너 (아이템 디자인이 없어서 기본 디자인 적용)*/
    DefaultSpinnerAdapter typeSpinnerAdapter;
    List<String> typeArrayData = new ArrayList<>();

    private View createTabView(String tabName) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.tab_userinfo_menu, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        tab_userInfoTabMenu.addTab(tab_userInfoTabMenu.newTab().setCustomView(createTabView(getString(R.string.UserInfoActivity_text_modifyUserInfo))));
        tab_userInfoTabMenu.addTab(tab_userInfoTabMenu.newTab().setCustomView(createTabView(getString(R.string.UserInfoActivity_text_changePassword))));

        new TabIndicatorRectF(
                new TabIndicatorRectF.FixedWidthModifier(
                        getResources().getDimension(R.dimen._90sdp)))
                .replaceBoundsRectF(tab_userInfoTabMenu);

        tab_userInfoTabMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_userInfoPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pagerAdapter = new UserInfoTabFragmentAdapter(this, numPage);

        /*가로스크롤유무*/
        vp_userInfoPager.setUserInputEnabled(true);

        vp_userInfoPager.setAdapter(pagerAdapter);
        vp_userInfoPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp_userInfoPager.setCurrentItem(2);
        vp_userInfoPager.setOffscreenPageLimit(2);
        vp_userInfoPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    //   vp_boardFaqPager.setCurrentItem(position);
                    tab_userInfoTabMenu.getTabAt(position).select();

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nowPage = position;

            }

        });

        vp_userInfoPager.setCurrentItem(0);




    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();


    }   //  onResume

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked() {
        onBackPressed();
    }

    /*타입별 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        /**
         *  0   기본정보 수정 프래그먼트
         *  1   비밀번호 변경 프래그먼트
         */

        switch (num) {

            case 0:

                vp_userInfoPager.setCurrentItem(0);

                break;

            case 1:

                vp_userInfoPager.setCurrentItem(1);

                break;


        }

    }

}