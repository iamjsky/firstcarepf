package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.model.FaqListModel;

/**
 * ClassName            BoardFaqAdapter
 * Created by JSky on   2020-06-26
 * <p>
 * Description          faq (백업)
 */
public class BoardFaqAdapter extends BaseExpandableListAdapter {
    List<FaqListModel> data;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    Context context;

    public BoardFaqAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return data == null?0:data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return data.get(i).getContent();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_board_faq, viewGroup, false);
            viewHolder.tv_subject = (TextView) v.findViewById(R.id.tv_subject);
            viewHolder.iv_itemArrow = (ImageView) v.findViewById(R.id.iv_itemArrow);

            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }
        if (b) {
           //선태ㅔㄱ했을떄


            Glide.with(context).load(R.drawable.icn_uparrow_01)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into( viewHolder.iv_itemArrow);

        } else {
            Glide.with(context).load(R.drawable.icn_downarrow_01)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into( viewHolder.iv_itemArrow);
        }

        FaqListModel result = (FaqListModel) getGroup(i);
        viewHolder.tv_subject.setText(result.getSubject());




        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = view;

        String des,finalImgUrl;


        if(v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_board_faq_child, null);
            viewHolder.tv_content = (TextView) v.findViewById(R.id.tv_content);


            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }


            viewHolder.tv_content.setText(String.valueOf(getChild(i, i1)));


        return v;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void setData(List<FaqListModel> noticeDataList) {
        this.data = noticeDataList;
    }

    public int getGroupPosition(int selectedNoticeId) {
        int i = 0;
        for (FaqListModel item: data) {
            if (item.getIdx() == selectedNoticeId) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public void addData(List<FaqListModel> boardItemModels) {
        this.data = boardItemModels;


        notifyDataSetChanged();
    }

    class ViewHolder{
        public TextView tv_subject;
        public TextView tv_content;
        public ImageView iv_itemArrow;
    }
}
