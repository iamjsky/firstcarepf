package kr.firstcare.android.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.BoardFaqAdapter;
import kr.firstcare.android.app.adapter.BoardNoticeAdapter;
import kr.firstcare.android.app.adapter.CommuteInfoAdapter;
import kr.firstcare.android.app.adapter.FaqTabFragmentAdapter;
import kr.firstcare.android.app.adapter.SearchAgencyAdapter;
import kr.firstcare.android.app.adapter.SignUpFragmentAdapter;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.BoardListModel;
import kr.firstcare.android.app.model.FaqListModel;
import kr.firstcare.android.app.model.NoticeListModel;
import kr.firstcare.android.app.ui.dialog.NoticeContentDialog;
import kr.firstcare.android.app.ui.view.TabIndicatorRectF;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            BoardActivity
 * Created by JSky on   2020-06-24
 * <p>
 * Description          공지사항, FAQ 방식 다른 레이아웃 백업
 */

public class BoardActivity_backup extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    /*공지사항*/
    @BindView(R.id.layout_board)
    View layout_board;
    @BindView(R.id.rv_noticeListView)
    RecyclerView rv_noticeListView;
    @BindView(R.id.layout_resultEmpty)
    LinearLayout layout_resultEmpty;

    private BoardNoticeAdapter boardNoticeAdapter;
    private NoticeContentDialog noticeContentDialog;

    /**/

    /*FAQ*/
    @BindView(R.id.layout_faq)
    View layout_faq;
    @BindView(R.id.tab_faqTabMenu)
    TabLayout tab_faqTabMenu;
    @BindView(R.id.vp_boardFaqPager)
    ViewPager2 vp_boardFaqPager;

    private FragmentStateAdapter pagerAdapter;
    private int numPage = 3;
    private int nowPage = 0;

    private View createTabView(String tabName) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.tab_faq_menu, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;

    }

    /**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_backup);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        boardNoticeAdapter = new BoardNoticeAdapter(this);
        boardNoticeAdapter.setClickListener(noticeItemClickListener);

        rv_noticeListView.setLayoutManager(new LinearLayoutManager(this));
        rv_noticeListView.setAdapter(boardNoticeAdapter);

        tab_faqTabMenu.addTab(tab_faqTabMenu.newTab().setCustomView(createTabView("회원가입")));
        tab_faqTabMenu.addTab(tab_faqTabMenu.newTab().setCustomView(createTabView("출퇴근 기록하기")));
        tab_faqTabMenu.addTab(tab_faqTabMenu.newTab().setCustomView(createTabView("기타")));
        new TabIndicatorRectF(
                new TabIndicatorRectF.FixedWidthModifier(
                        getResources().getDimension(R.dimen._24sdp)))
                .replaceBoundsRectF(tab_faqTabMenu);

        tab_faqTabMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_boardFaqPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pagerAdapter = new FaqTabFragmentAdapter(this, numPage);

        /*가로스크롤유무*/
        vp_boardFaqPager.setUserInputEnabled(true);

        vp_boardFaqPager.setAdapter(pagerAdapter);
        vp_boardFaqPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp_boardFaqPager.setCurrentItem(3);
        vp_boardFaqPager.setOffscreenPageLimit(3);
        vp_boardFaqPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    //   vp_boardFaqPager.setCurrentItem(position);
                    tab_faqTabMenu.getTabAt(position).select();

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nowPage = position;

            }

        });

        vp_boardFaqPager.setCurrentItem(0);

    }   //  onCreate

    /*네비 메뉴 타입*/
    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout();

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

    /*타입에 따른 레이아웃 세팅*/
    private void setLayout() {

        /**
         * 0 : err
         * 1 : 공지사항
         * 2 : FAQ
         * */

        int type = getIntent().getIntExtra("menuType", 0);
        switch (type) {
            case 0:

                break;

            case 1:

                tv_title.setText("공지사항");
                getNewNoticeList();
                layout_board.setVisibility(View.VISIBLE);
                layout_faq.setVisibility(View.GONE);

                break;

            case 2:

                tv_title.setText("FAQ");

                layout_board.setVisibility(View.GONE);
                layout_faq.setVisibility(View.VISIBLE);
                break;

        }

    }

    public void getNewNoticeList(){

        apiService.getNewNoticeList().enqueue(new Callback<List<NoticeListModel>>() {
            @Override
            public void onResponse(Call<List<NoticeListModel>> call, Response<List<NoticeListModel>> response) {

                if(response.isSuccessful()){

                    List<NoticeListModel> noticeList = new ArrayList<NoticeListModel>();
                    noticeList = response.body();

                    if(noticeList.size() > 0){
                        boardNoticeAdapter.addItem(noticeList);
                        boardNoticeAdapter.notifyDataSetChanged();

                    }else{

                        layout_resultEmpty.setVisibility(View.VISIBLE);

                    }

                }else{

                    layout_resultEmpty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<NoticeListModel>> call, Throwable t) {

                layout_resultEmpty.setVisibility(View.VISIBLE);

            }


        });


    }


    /*공지사항 게시물 아이템 클릭 리스너*/
    public BoardNoticeAdapter.ItemClickListener noticeItemClickListener = new BoardNoticeAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NoticeListModel selectedItem = boardNoticeAdapter.getItem(position);

            noticeContentDialog = new NoticeContentDialog(BoardActivity_backup.this, selectedItem, noticeConfirmButtonClickListner);
            noticeContentDialog.show();

        }

    };

    /*공지사항 게시물 내용 닫기 리스너*/
    public View.OnClickListener noticeConfirmButtonClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            noticeContentDialog.dismiss();

        }

    };

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked() {
        onBackPressed();
    }

    /*타입별 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        /**
         *  0   회원가입 프래그먼트
         *  1   출퇴근 기록하기 프래그먼트
         *  2   기타 프래그먼트
         */

        switch (num) {

            case 0:

                vp_boardFaqPager.setCurrentItem(0);

                break;

            case 1:

                vp_boardFaqPager.setCurrentItem(1);

                break;

            case 2:

                vp_boardFaqPager.setCurrentItem(2);

                break;

        }

    }

}