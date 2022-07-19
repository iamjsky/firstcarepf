package kr.firstcare.android.app.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.BoardAdapter;
import kr.firstcare.android.app.model.BoardListModel;
import kr.firstcare.android.app.ui.dialog.BoardContentDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            BoardActivity
 * Created by JSky on   2020-06-24
 * 
 * Description          네비메뉴 - 각종 게시판 (공지사항, FAQ 같은 방식)
 */

public class BoardActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    /*공지사항, FAQ*/
    @BindView(R.id.layout_board)
    View layout_board;
    @BindView(R.id.rv_boardListView)
    RecyclerView rv_boardListView;
    @BindView(R.id.layout_resultEmpty)
    LinearLayout layout_resultEmpty;

    private BoardAdapter boardAdapter;
    private BoardContentDialog boardContentDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        boardAdapter = new BoardAdapter(this);
        boardAdapter.setClickListener(noticeItemClickListener);

        rv_boardListView.setLayoutManager(new LinearLayoutManager(this));
        rv_boardListView.setAdapter(boardAdapter);




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

    }   //  onCreate

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

                tv_title.setText(getString(R.string.notice));
                getNoticeList();

                break;

            case 2:

                tv_title.setText(getString(R.string.faq));
                getFaqList();

                break;

        }

    }

    /*공지사항 게시물 아이템 클릭 리스너*/
    public BoardAdapter.ItemClickListener noticeItemClickListener = new BoardAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            BoardListModel selectedItem = boardAdapter.getItem(position);

            boardContentDialog = new BoardContentDialog(BoardActivity.this, selectedItem, noticeConfirmButtonClickListner);
            boardContentDialog.show();

        }

    };

    /*공지사항 게시물 내용 닫기 리스너*/
    public View.OnClickListener noticeConfirmButtonClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boardContentDialog.dismiss();

        }

    };

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        onBackPressed();
    }


    public void getNoticeList(){

        apiService.getNoticeList().enqueue(new Callback<List<BoardListModel>>() {
            @Override
            public void onResponse(Call<List<BoardListModel>> call, Response<List<BoardListModel>> response) {

                if(response.isSuccessful()){

                    List<BoardListModel> boardList = new ArrayList<BoardListModel>();
                    boardList = response.body();

                    if(boardList.size() > 0){
                        boardAdapter.addItem(boardList);
                        boardAdapter.notifyDataSetChanged();

                    }else{

                        layout_resultEmpty.setVisibility(View.VISIBLE);

                    }



                }else{

                    layout_resultEmpty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<BoardListModel>> call, Throwable t) {

                layout_resultEmpty.setVisibility(View.VISIBLE);

            }


        });


    }

    public void getFaqList(){

        apiService.getFaqList().enqueue(new Callback<List<BoardListModel>>() {
            @Override
            public void onResponse(Call<List<BoardListModel>> call, Response<List<BoardListModel>> response) {

                if(response.isSuccessful()){

                    List<BoardListModel> boardList = new ArrayList<BoardListModel>();
                    boardList = response.body();

                    if(boardList.size() > 0){
                        boardAdapter.addItem(boardList);
                        boardAdapter.notifyDataSetChanged();

                    }else{

                        layout_resultEmpty.setVisibility(View.VISIBLE);

                    }

                }else{

                    layout_resultEmpty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<BoardListModel>> call, Throwable t) {

                layout_resultEmpty.setVisibility(View.VISIBLE);

            }


        });


    }

}