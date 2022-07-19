package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.SearchAgencyAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.SelectAgencyItem;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.activity.MainActivity;
import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpSecondFragment;
import kr.firstcare.android.app.ui.fragment.userinfo.ModifyUserInfoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            SearchAgencyDialog
 * Created by JSky on   2020-06-17
 *
 * Description          소속(제공)기관 검색 다이얼로그
 */

public class SearchAgencyDialog extends BaseDialog {
    public static final String TAG = "fc_debug";
    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";

    private Context mContext;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.edtxt_inputAgency)
    EditText edtxt_inputAgency;
    @BindView(R.id.rv_agencyListView)
    RecyclerView rv_agencyListView;
    @BindView(R.id.layout_selectAgencyArea)
    FlowLayout layout_selectAgencyArea;
    @BindView(R.id.layout_progressBar)
    LinearLayout layout_progressBar;

    private SearchAgencyAdapter searchAgencyAdapter;

    List<SelectAgencyItem> selectedItemList = new ArrayList<>();
    private int parentType;

    public SearchAgencyDialog(Context context, String title, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        this.parentType = parentType;

    }

    public SearchAgencyAdapter.ItemClickListener agencyItemClickListener = new SearchAgencyAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            AgencyListModel selectedItem = searchAgencyAdapter.getItem(position);
            String agencyName = selectedItem.getSubAgencyName();

            if(selectedItem.getSelected() == 0){

                selectedItem.setSelected(1);

                ViewGroup selectItemView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_select_agency_item, null, false);
                TextView tv_agnecyName = (TextView) selectItemView.findViewById(R.id.tv_agencyName);
                tv_agnecyName.setText(agencyName);
                selectItemView.setTag(agencyName);

                selectItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItem.setSelected(0);
                        String agencyName = v.getTag().toString();

                        for(int i=0; i < selectedItemList.size(); i++){
                            if(selectedItemList.get(i).getAgencyName() == agencyName){

                                layout_selectAgencyArea.removeView(selectedItemList.get(i).getItem());
                                searchAgencyAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                });

                SelectAgencyItem selectAgencyItem = new SelectAgencyItem();
                selectAgencyItem.setItem(selectItemView);
                selectAgencyItem.setAgencyName(agencyName);
                selectedItemList.add(selectAgencyItem);

                layout_selectAgencyArea.addView(selectItemView);

            }else if(selectedItem.getSelected() == 1){

                selectedItem.setSelected(0);

                for(int i=0; i < selectedItemList.size(); i++){
                    if(selectedItemList.get(i).getAgencyName() == agencyName){

                        layout_selectAgencyArea.removeView(selectedItemList.get(i).getItem());

                    }

                }

            }

            searchAgencyAdapter.notifyDataSetChanged();

        }

    };



    public TextWatcher inputAgencyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = edtxt_inputAgency.getText().toString()+"";

            searchAgencyAdapter.filter(text);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_agency);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        rv_agencyListView.setLayoutManager(new LinearLayoutManager(mContext));
        searchAgencyAdapter = new SearchAgencyAdapter(mContext);
        searchAgencyAdapter.setClickListener(agencyItemClickListener);

        edtxt_inputAgency.addTextChangedListener(inputAgencyTextWatcher);
        rv_agencyListView.setAdapter(searchAgencyAdapter);

        getAgencyList();


    }




//    public void setDummyData() {
//        List<AgencyListModel> agencyList = new ArrayList<AgencyListModel>();
//
//        String[] agencyName = { "a","b","c","d","e","f","g","h" };
//
//        for(int i=0; i < agencyName.length; i++){
//            AgencyListModel agencyListModel = new AgencyListModel();
//            agencyListModel.setSubAgencyName(agencyName[i]);
//            agencyListModel.setSubAgencyNumber("051-123-1234"+i);
//            agencyList.add(agencyListModel);
//
//        }
//
//
//        searchAgencyAdapter.addItem(agencyList);
//        searchAgencyAdapter.notifyDataSetChanged();
//    }

    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){
        StringBuilder sb = new StringBuilder();

        ArrayList<String> selectedIdx = new ArrayList();

        for(int i = 0; i < searchAgencyAdapter.getBackUpItemCount(); i++){
            if(searchAgencyAdapter.getBackUpItem(i).getSelected() == 1){

                Constant.LOG(TAG, "getSelected("+i+") : " + searchAgencyAdapter.getBackUpItem(i).getSubAgencyName());
                sb.append(searchAgencyAdapter.getBackUpItem(i).getSubAgencyName() + ", ");
                selectedIdx.add(searchAgencyAdapter.getBackUpItem(i).getIdx().toString());

            }

        }

        String selectedAgencyName = sb.toString();
        Constant.LOG(TAG, " selectedAgencyName LENGTH : " + selectedAgencyName.length());

        if(selectedAgencyName.length() > 1){

            selectedAgencyName = selectedAgencyName.substring(0,selectedAgencyName.length()-2);
            Constant.LOG(TAG, "selectedAgencyName : " + selectedAgencyName);
            if(parentType == 0){

                TypeVSignUpSecondFragment.instance.setAgency(selectedAgencyName, selectedIdx);

            }else if(parentType == 1){

                ModifyUserInfoFragment.instance.setAgency(selectedAgencyName, selectedIdx);

            }


            dismiss();

        }else{
            /*선택한 소속기관이 없을 때 메시지*/
            if(parentType == 0){

                TypeVSignUpSecondFragment.instance.showSnackbar(mainLayout, mContext.getString(R.string.TypeVSignUpSecondFragment_msg_selectSearchAgency_err));

            }else if(parentType == 1){

                ModifyUserInfoFragment.instance.showSnackbar(mainLayout, mContext.getString(R.string.TypeVSignUpSecondFragment_msg_selectSearchAgency_err));

            }


        }

    }

    public void getAgencyList(){
        layout_progressBar.setVisibility(View.VISIBLE);
        apiService.getAgencyList().enqueue(new Callback<List<AgencyListModel>>() {
            @Override
            public void onResponse(Call<List<AgencyListModel>> call, Response<List<AgencyListModel>> response) {
                if(response.isSuccessful()){

                    List<AgencyListModel> agencyList = new ArrayList<AgencyListModel>();
                    agencyList = response.body();
                        Constant.LOG(TAG, "getAgencyList : " + agencyList);
                    if(agencyList.size() > 0){

                        searchAgencyAdapter.addItem(agencyList);
                        searchAgencyAdapter.notifyDataSetChanged();
                        layout_progressBar.setVisibility(View.GONE);

                    }else{

                        if(parentType == 0) {

                            TypeVSignUpSecondFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_01));

                        }else if(parentType == 1){

                            ModifyUserInfoFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_01));

                        }

                    }


                }else{

                    if(parentType == 0) {

                        TypeVSignUpSecondFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_02));

                    }else if(parentType == 1){

                        ModifyUserInfoFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_02));

                    }

                }

            }

            @Override
            public void onFailure(Call<List<AgencyListModel>> call, Throwable t) {
                if(parentType == 0) {

                    TypeVSignUpSecondFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_03));

                }else if(parentType == 1){

                    ModifyUserInfoFragment.instance.showSnackbar(mContext.getString(R.string.getAgencyList_msg_err_03));

                }
            }

        });

    }


}
