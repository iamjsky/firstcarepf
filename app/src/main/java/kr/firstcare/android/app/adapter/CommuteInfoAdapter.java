package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.ui.activity.MainActivity;
import kr.firstcare.android.app.ui.dialog.DatePickerDialog;

/**
 * ClassName            SearchAgencyAdapter
 * Created by JSky on   2020-06-16
 * <p>
 * Description          소속기관 검색 어댑터
 */
public class CommuteInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommuteInfoListModel> mData;
    private LayoutInflater mInflater;


    private CommuteInfoAdapter.OnDatePickerButtonClickListener onDatePickerButtonClickListener;
    private CommuteInfoAdapter.OnGetToWorkButtonClickListener onGetToWorkButtonClickListener;
    private CommuteInfoAdapter.OnFinishWorkButtonClickListener onFinishWorkButtonClickListener;
    private CommuteInfoAdapter.OnUserRollListButtonClickListener onUserRollListButtonClickListener;

    public interface OnDatePickerButtonClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnGetToWorkButtonClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnFinishWorkButtonClickListener {
        public void onItemClick(View view, int position);
    }
    public interface OnUserRollListButtonClickListener {
        public void onItemClick(View view, int position);
    }
    public CommuteInfoAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);



    }

    public void addItem(List<CommuteInfoListModel> data) {
            this.mData = data;


        notifyDataSetChanged();
    }

    public void removeItem(int id){
        mData.remove(id);
        notifyDataSetChanged();
    }

    public void clearItem(){
        if(mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

            view = mInflater.inflate(R.layout.item_commute_info, parent, false);
            holder = new ItemViewHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            CommuteInfoListModel list = mData.get(position);
            String actDate = "";

            if (list.getActDate() != null && !list.getActDate().equals("")) {
                itemViewHolder.tv_attDate.setText(list.getActDate());
                actDate = itemViewHolder.tv_attDate.getText().toString()+"";
            } else {
                itemViewHolder.tv_attDate.setText("-");
            }

            if (list.getSubAgencyName() != null && !list.getSubAgencyName().equals("")) {
                itemViewHolder.tv_attAgency.setText(list.getSubAgencyName());
            } else {
                itemViewHolder.tv_attAgency.setText("-");
            }

            if (list.getTitle() != null && !list.getTitle().equals("")) {
                itemViewHolder.tv_attService.setText(list.getTitle());
            } else {
                itemViewHolder.tv_attService.setText("-");
            }

            if (list.getAttTime() != null && !list.getAttTime().equals("00:00:00")) {
                itemViewHolder.tv_attTime.setText(list.getAttTime());
                itemViewHolder.iv_getToWorkInfo.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.tv_attTime.setText("-");
                itemViewHolder.iv_getToWorkInfo.setVisibility(View.GONE);
            }

            if (list.getAttAddr() != null && !list.getAttAddr().equals("")) {
                itemViewHolder.tv_attAddr.setText(list.getAttAddr());
            } else {
                itemViewHolder.tv_attAddr.setText("-");
                itemViewHolder.iv_getToWorkInfo.setVisibility(View.GONE);
            }

            if (list.getLeaveTime() != null && !list.getLeaveTime().equals("00:00:00")) {
                itemViewHolder.tv_leaveTime.setText(list.getLeaveTime());
                itemViewHolder.iv_finishWorkInfo.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.tv_leaveTime.setText("-");
                itemViewHolder.iv_finishWorkInfo.setVisibility(View.GONE);
            }
        if (list.getLeaveAddr() != null && !list.getLeaveAddr().equals("") && !list.getLeaveTime().equals("00:00:00")) {
            itemViewHolder.tv_leaveAddr.setText(list.getLeaveAddr());
        } else {
            itemViewHolder.tv_leaveAddr.setText("-");
            itemViewHolder.iv_finishWorkInfo.setVisibility(View.GONE);
        }
//        if (list.getAttLatitude() != null) {
//            itemViewHolder.iv_getToWorkInfo.setVisibility(View.VISIBLE);
//        } else {
//            itemViewHolder.iv_getToWorkInfo.setVisibility(View.GONE);
//        }
//
//        if (list.getLeaveLatitude() != null) {
//            itemViewHolder.iv_finishWorkInfo.setVisibility(View.VISIBLE);
//        } else {
//            itemViewHolder.iv_finishWorkInfo.setVisibility(View.GONE);
//        }

        itemViewHolder.tv_commuteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDatePickerButtonClickListener.onItemClick(v, position);
                waitTwoSec(v);
            }
        });

        itemViewHolder.iv_getToWorkInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetToWorkButtonClickListener.onItemClick(v, position);
                waitTwoSec(v);
            }
        });

        itemViewHolder.iv_finishWorkInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishWorkButtonClickListener.onItemClick(v, position);
                waitTwoSec(v);
            }
        });



        /*오늘 날짜가 아니라면 이용자 출석 관리 버튼 비활성화 - 백업용으로 만들어 둠*/
//        if(actDate.equals(Utils.getNowDateKR()) || UserInfo.getInstance().TEST_MODE){
//
//            itemViewHolder.btn_userRollList.setEnabled(true);
//            itemViewHolder.btn_userRollList.setTextColor(mContext.getResources().getColor(R.color.fc_boldColor));
//            itemViewHolder.btn_userRollList.setBackground(mContext.getDrawable(R.drawable.bg_rounded_01));
//
//
//        }else{
//            itemViewHolder.btn_userRollList.setEnabled(false);
//            itemViewHolder.btn_userRollList.setTextColor(mContext.getResources().getColor(R.color.fc_typeSelectOffTextColor));
//            itemViewHolder.btn_userRollList.setBackground(mContext.getDrawable(R.drawable.bg_rounded_04));
//        }

        itemViewHolder.btn_userRollList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserRollListButtonClickListener.onItemClick(v, position);
                waitTwoSec(v);
            }
        });






    }

    // total number of cells
    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }


    public void waitTwoSec(View v){

        v.setClickable(false);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                v.setClickable(true);


            }
        }, 1500); // 2
    }


    // stores and recycles views as they are scrolled off screen
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_commuteList;
        TextView tv_attDate;
        TextView tv_attAgency;
        TextView tv_attService;
        TextView tv_attTime;
        TextView tv_attAddr;
        TextView tv_leaveTime;
        TextView tv_leaveAddr;
        ImageView iv_getToWorkInfo;
        ImageView iv_finishWorkInfo;
        Button btn_userRollList;



        ItemViewHolder(View itemView) {
            super(itemView);
            tv_commuteList = itemView.findViewById(R.id.tv_commuteList);
            tv_attDate = itemView.findViewById(R.id.tv_attDate);
            tv_attAgency = itemView.findViewById(R.id.tv_attAgency);
            tv_attService = itemView.findViewById(R.id.tv_attService);
            tv_attTime = itemView.findViewById(R.id.tv_attTime);
            tv_attAddr = itemView.findViewById(R.id.tv_attAddr);
            tv_leaveTime = itemView.findViewById(R.id.tv_leaveTime);
            tv_leaveAddr = itemView.findViewById(R.id.tv_leaveAddr);
            iv_getToWorkInfo = itemView.findViewById(R.id.iv_getToWorkInfo);
            iv_finishWorkInfo = itemView.findViewById(R.id.iv_finishWorkInfo);
            btn_userRollList = itemView.findViewById(R.id.btn_userRollList);


        }

        public View getView(){
            return itemView;
        }


    }



    // convenience method for getting data at click position
    public CommuteInfoListModel getItem(int id) {

        return mData.get(id);
    }
    public void setOnDatePickerButtonClickListener(CommuteInfoAdapter.OnDatePickerButtonClickListener onDatePickerButtonClickListener){
        this.onDatePickerButtonClickListener = onDatePickerButtonClickListener;
    }

    public void setOnGetToWorkButtonClickListener(CommuteInfoAdapter.OnGetToWorkButtonClickListener onGetToWorkButtonClickListener){
        this.onGetToWorkButtonClickListener = onGetToWorkButtonClickListener;
    }
    public void setOnFinishWorkButtonClickListener(CommuteInfoAdapter.OnFinishWorkButtonClickListener onFinishWorkButtonClickListener){
        this.onFinishWorkButtonClickListener = onFinishWorkButtonClickListener;
    }
    public void setOnUserRollListButtonClickListener(CommuteInfoAdapter.OnUserRollListButtonClickListener onUserRollListButtonClickListener){
        this.onUserRollListButtonClickListener = onUserRollListButtonClickListener;
    }







}
