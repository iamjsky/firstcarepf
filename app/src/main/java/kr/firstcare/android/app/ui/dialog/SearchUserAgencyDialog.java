package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.SearchAgencyAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpSecondFragment;


/**
 * ClassName            SearchUserAgencyDialog
 * Created by JSky on   2020-06-22
 *
 * Description          매칭된 제공기관 주소로 검색 다이얼로그 (사용안함)
 */

public class SearchUserAgencyDialog extends BaseDialog {
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

    private SearchAgencyAdapter searchAgencyAdapter;

    public SearchUserAgencyDialog(Context context, String title, View.OnClickListener closeButtonListener) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;

    }


    public SearchAgencyAdapter.ItemClickListener agencyItemClickListener = new SearchAgencyAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            AgencyListModel selectedItem = searchAgencyAdapter.getItem(position);
            selectedItem.setSelected(selectedItem.getSelected() == 0 ? 1 : 0);
            searchAgencyAdapter.notifyItemChanged(position);

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

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        rv_agencyListView.setLayoutManager(new LinearLayoutManager(mContext));
        searchAgencyAdapter = new SearchAgencyAdapter(mContext);
        searchAgencyAdapter.setClickListener(agencyItemClickListener);
        rv_agencyListView.setAdapter(searchAgencyAdapter);
//        getAgencyList(UserInfo.getInstance().idx);
        setDummyData();
        edtxt_inputAgency.addTextChangedListener(inputAgencyTextWatcher);

    }

    public void setDummyData() {
        List<AgencyListModel> agencyList = new ArrayList<AgencyListModel>();

        String[] agencyName = { "a","b","c","d","e","f","g","h" };

        for(int i=0; i < agencyName.length; i++){
            AgencyListModel agencyListModel = new AgencyListModel();
            agencyListModel.setSubAgencyName(agencyName[i]);
            agencyListModel.setSubAgencyNumber("051-123-1234"+i);
            agencyList.add(agencyListModel);

        }


        searchAgencyAdapter.addItem(agencyList);
        searchAgencyAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < searchAgencyAdapter.getBackUpItemCount(); i++){
            if(searchAgencyAdapter.getBackUpItem(i).getSelected() == 1){
                Constant.LOG(TAG, "getSelected("+i+") : " + searchAgencyAdapter.getBackUpItem(i).getSubAgencyName());
                sb.append(searchAgencyAdapter.getBackUpItem(i).getSubAgencyName() + ", ");

            }
        }

        String selectedAgencyName = sb.toString();
        Constant.LOG(TAG, " selectedAgencyName LENGTH : " + selectedAgencyName.length());

        if(selectedAgencyName.length() > 1){
            selectedAgencyName = selectedAgencyName.substring(0,selectedAgencyName.length()-2);
            Constant.LOG(TAG, "selectedAgencyName : " + selectedAgencyName);
           // TypeVSignUpSecondFragment.instance.setAgency(selectedAgencyName);

            dismiss();

        }else{
            /*선택한 소속기관이 없을 때 메시지*/
        }




    }




}
