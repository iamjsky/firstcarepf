package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.model.UserAgencyListModel;


public class UserAgencySpinnerAdapter extends ArrayAdapter<UserAgencyListModel> implements SpinnerAdapter {

    Context context;
    List<UserAgencyListModel> data;
    LayoutInflater inflater;
    int dropResource;
    int topResource;



    public UserAgencySpinnerAdapter(Context context, int resource, List<UserAgencyListModel> data){
        super(context, resource, data);
        clearItem();
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.topResource = resource;
    }


    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }
    @Override
    public UserAgencyListModel getItem(int position) {
        return data.get(position);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if(convertView==null) {
//            convertView = inflater.inflate(R.layout.spinner_service_normal, parent, false);
//
//            if(data!=null){
//                //데이터세팅
//                UserAgencyListModel list = data.get(position);
//
//                TextView spinnerNormalText = (TextView) convertView.findViewById(R.id.spinnerText);
//                if(list.getSubAgencyName() != null){
//                    spinnerNormalText.setText(list.getSubAgencyName());
//                }
//
//            }
//        }



        return createTopView(position, convertView, parent);
    }
    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        this.dropResource = resource;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        if(convertView==null){
//            convertView = inflater.inflate(R.layout.spinner_service_dropdown, parent, false);
//
//        }
//
//        //데이터세팅
//        ProgramListModel list = data.get(position);
//        if(list.getTitle() != null){
//            ((TextView)convertView.findViewById(R.id.spinnerText)).setText(list.getTitle());
//        }


        return createDropView(position, convertView, parent);
    }
    private View createTopView(int position, View convertView, ViewGroup parent){
        Constant.LOG("DINGDING", "POSITION : " + position);
        final View view = inflater.inflate(topResource, parent, false);
        if(data!=null){
//                //데이터세팅
            UserAgencyListModel list = data.get(position);

            TextView spinnerNormalText = (TextView) view.findViewById(R.id.spinnerText);
            if(list.getSubAgencyName() != null){
                spinnerNormalText.setText(list.getSubAgencyName());
            }

        }
        return view;
    }

    private View createDropView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(dropResource, parent, false);
        if(data!=null){
//                //데이터세팅
            UserAgencyListModel list = data.get(position);

            TextView spinnerNormalText = (TextView) view.findViewById(R.id.spinnerText);
            if(list.getSubAgencyName() != null){
                spinnerNormalText.setText(list.getSubAgencyName());
            }


        }
        return view;
    }

    public void clearItem(){
        if(data != null){
            data.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


















}
