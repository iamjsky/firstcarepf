package kr.firstcare.android.app.ui.fragment.faq;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.BoardFaqAdapter;
import kr.firstcare.android.app.model.FaqListModel;
import kr.firstcare.android.app.model.NoticeListModel;
import kr.firstcare.android.app.ui.fragment.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            FaqSignUpFragment
 * Created by JSky on   2020-06-26
 * <p>
 * Description          faq-회원가입
 */
public class FaqSignUpFragment extends BaseFragment {
    private int frag_num;
    public static FaqSignUpFragment instance;

    @BindView(R.id.ev_faqListView)
    ExpandableListView ev_faqListView;
    @BindView(R.id.layout_resultEmpty)
    LinearLayout layout_resultEmpty;

    /*faq 리스트 어댑터*/
    private BoardFaqAdapter boardFaqAdapter;
    int lastClickedPosition = 0;

    public FaqSignUpFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static FaqSignUpFragment newInstance(int num) {
        FaqSignUpFragment fragment = new FaqSignUpFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num", 0);

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq_signup, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        boardFaqAdapter = new BoardFaqAdapter(getActivity());

        ev_faqListView.setAdapter(boardFaqAdapter);
        ev_faqListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {

            Log.d("groupPosition", String.valueOf(groupPosition));
            Log.d("selectedNoticeId", String.valueOf(id));

            Boolean isExpand = (!ev_faqListView.isGroupExpanded(groupPosition));

            ev_faqListView.collapseGroup(lastClickedPosition);

            if(isExpand) {
                ev_faqListView.expandGroup(groupPosition);

            }

            lastClickedPosition = groupPosition;

            return true;

        });

        setBoardDummyData();    // 변경시 제거
//        getFaqSignUpList();   // 변경시 활성

        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
    }   //  onViewCreated

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }


    }     //    onResume

    @Override
    public void onStop() {
        super.onStop();
    }


    /*faq 더미 데이터들*/
    public void setBoardDummyData() {

        List<FaqListModel> faqList = new ArrayList<>();



        for(int i=0; i < 10; i++){


            FaqListModel faqListModel = new FaqListModel();
            faqListModel.setSubject("회원가입 게시글 목록 입니다.("+i+")");
            faqListModel.setContent("간병사와 환자, 보호자를 실시간으로\n연결하는 O2O 커뮤니티 서비스\n환자,보호자\n\n\n환자/보호자가 " +
                    "케어 알림 신청을 통해\n환자의 현재 케어정보와 상태를\n알림받게 되며\n\n\n실시간 채팅을 통해 간병사/요양보호사님께\n체크리스트 " +
                    "외의 정보를 얻을 수 있습니다."+i);
            faqListModel.setIdx(i);
            faqList.add(faqListModel);

        }


        boardFaqAdapter.addData(faqList);
        boardFaqAdapter.notifyDataSetChanged();

    }


    /*FAQ - 회원가입 게시글 목록 가져오기*/
    public void getFaqSignUpList(){

        apiService.getFaqSignUpList().enqueue(new Callback<List<FaqListModel>>() {
            @Override
            public void onResponse(Call<List<FaqListModel>> call, Response<List<FaqListModel>> response) {

                if(response.isSuccessful()){

                    List<FaqListModel> faqSignUpList = new ArrayList<FaqListModel>();
                    faqSignUpList = response.body();

                    if(faqSignUpList.size() > 0){
                        boardFaqAdapter.addData(faqSignUpList);
                        boardFaqAdapter.notifyDataSetChanged();

                    }else{

                        layout_resultEmpty.setVisibility(View.VISIBLE);

                    }

                }else{

                    layout_resultEmpty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<FaqListModel>> call, Throwable t) {

                layout_resultEmpty.setVisibility(View.VISIBLE);

            }

        });


    }



}
