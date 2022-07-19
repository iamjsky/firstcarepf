package kr.firstcare.android.app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName            SignUp
 * Created by JSky on   2020-07-01
 * <p>
 * Description          회원가입 데이터
 */
public class SignUp {

    private static SignUp singleton;

    public String vteacherId;
    public String userPw;
    public String vteacherName;
    public String hp;
    public String email;
    public String emailFlag;
    public String smsFlag;
    public String sexFlag;
    public String zipcode;
    public String addr;
    public String addrDetail;
    public String photo;
    public String userCode;
    public String withdrawId;
    public String withdrawIp;
    public String withdrawDate;
    public String withdrawMemo;
    public String addDate;
    public String addId;
    public String addIp;
    public ArrayList<String> selectAgencyIdx = new ArrayList<>();












    public static SignUp getInstance() {
        if (singleton == null) {
            singleton = new SignUp();
        }

        return singleton;
    }
}
