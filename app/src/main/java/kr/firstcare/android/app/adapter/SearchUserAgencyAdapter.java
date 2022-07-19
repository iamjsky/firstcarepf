package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.model.UserAgencyListModel;

/**
 * ClassName            SearchAgencyAdapter
 * Created by JSky on   2020-06-16
 * <p>
 * Description          소속기관 검색 어댑터 (삭제)미사용
 */
public class SearchUserAgencyAdapter extends RecyclerView.Adapter<SearchUserAgencyAdapter.ViewHolder> {
    private Context mContext;
    private List<UserAgencyListModel> mData, backUpData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    public SearchUserAgencyAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);



    }

    public void addItem(List<UserAgencyListModel> data) {
            this.mData = data;
            this.backUpData = data;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_agency, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAgencyListModel list = mData.get(position);

//        if(list.getCoverImg() != null){
//            Glide.with(mContext).load(list.getCoverImg())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(holder.songCover);
//
//

        if(list.getSelected() == 0){
            Constant.LOG("dingding", "list.getSelected()0000 : " + list.getSelected());
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else{
            Constant.LOG("dingding", "list.getSelected()1111 : " + list.getSelected());
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.fc_agencyItemClickedBackgroundColor));
        }
        if(list.getSubAgencyName() != null){
            holder.tv_agencyName.setText(list.getSubAgencyName());
        }else{
            holder.tv_agencyName.setText("-");
        }

        if(list.getSubAgencyNumber() != null){
            holder.tv_agencyPhoneNum.setText(list.getSubAgencyNumber());
        }else{
            holder.tv_agencyPhoneNum.setText("-");
        }
        if(list.getPosition() == -1){
            list.setPosition(position);
        }


    }

    // total number of cells
    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }

    public int getBackUpItemCount() {
        if(backUpData != null){
            return backUpData.size();
        }
        return 0;
    }




    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        TextView tv_agencyName;
        TextView tv_agencyPhoneNum;
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_item);
            tv_agencyName = itemView.findViewById(R.id.tv_agencyName);
            tv_agencyPhoneNum = itemView.findViewById(R.id.tv_agencyPhoneNum);
            itemView.setOnClickListener(this);
            this.itemView = itemView;
        }

        public View getView(){
            return itemView;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



    // convenience method for getting data at click position
    public UserAgencyListModel getItem(int id) {

        return mData.get(id);
    }
    public UserAgencyListModel getBackUpItem(int id) {

        return backUpData.get(id);
    }
//    public void setSelected(int position){
//        backUpData.get(position).setSelected(1);
//
//    }
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



    public void filter(String searchText) {

        if (searchText.length() == 0) {//jika string kosong
            List<UserAgencyListModel> filteredList = new ArrayList<UserAgencyListModel>();
            for (UserAgencyListModel data : backUpData) {
             //   if (Utils.SoundSearcher.matchString(data.getAgencyName(),searchText)) {
                    filteredList.add(data);
              //  }
            }

        mData = filteredList;
        } else {//filter dan buat arraybaru dengan filter query model
            List<UserAgencyListModel> filteredList = new ArrayList<UserAgencyListModel>();

            for (UserAgencyListModel data : backUpData) {
                if (Utils.SoundSearcher.matchString(data.getSubAgencyName(),searchText)) {
                    filteredList.add(data);
                }
            }




            mData = filteredList;

        }
        //memperbarui list adapter
        notifyDataSetChanged();
    }
}
