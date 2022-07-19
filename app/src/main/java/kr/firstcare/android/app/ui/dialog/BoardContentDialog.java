package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.model.BoardListModel;


/**
 * ClassName            BoardContentDialog
 * Created by JSky on   2020-06-26
 * 
 * Description          게시물 내용 다이얼로그
 */

public class BoardContentDialog extends BaseDialog {


    private View.OnClickListener confirmButtonListener;

    Context mContext;
    BoardListModel mData;

    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    @BindView(R.id.tv_subject)
    TextView tv_subject;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_content)
    TextView tv_content;

    public BoardContentDialog(Context context, BoardListModel data, View.OnClickListener confirmButtonListener) {
        super(context, R.style.FullScreenDialogStyle);
        this.confirmButtonListener = confirmButtonListener;
        mData = data;
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_board_content);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        btn_confirm.setOnClickListener(confirmButtonListener);

        String subject = mData.getSubject() + "";
        String date = Utils.formateDateFromstring("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd",  mData.getAddDate()+"");
        String name = mData.getName() + "";
        String content = mData.getContent() + "";

        if (!subject.equals("")) {
            tv_subject.setText(subject);
        } else {
            tv_subject.setText("제목 없음");
        }

        if (!date.equals("")) {
            tv_date.setText(date);
        } else {
            tv_date.setText("-");
        }

        if (!name.equals("")) {
            tv_name.setText(name);
        } else {
            tv_name.setText("이름 없음");
        }

        if (!content.equals("")) {
            tv_content.setText(content);
        } else {
            tv_content.setText("글 내용이 없습니다.");
        }

    }

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        onBackPressed();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }
}
