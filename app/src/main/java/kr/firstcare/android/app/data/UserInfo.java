package kr.firstcare.android.app.data;


/**
 * ClassName            UserInfo
 * Created by JSky on   2020-06-12
 *
 * Description          유저 정보 데이터
 */

public class UserInfo {

    private static UserInfo singleton;

    //로그 설정
    public boolean DEBUG_MODE = false;
    //테스트 설정(회원가입 무시, 출퇴근 상관없이 스피너 선택 가능, 이용자 출석 관리 오늘날짜 무시...) 0709 deleted
    public boolean TEST_MODE = false;

    /*
     selectLoginType    로그인한 유저 타입(권한)
     -1 : err
     0 : none
     1 : G  대상자보호자
     2 : N  케어선생님
     3 : V  제공인력
      */
    public int selectLoginType;

    public Integer idx;
    public String userId;
    public String vteacherId;
    public String userpw;
    public String hp;
    public String accessToken;
    public String vteacherName;
    public String googleToken;
    public String email;
    public String emailFlag;
    public String smsFlag;
    public String sexFlag;
    public String zipcode;
    public String addr;
    public String addrDetail;
    public String photo;
    public String userCode;
    public String canILogin;
    public String withdrawFlag;
    public String withdrawId;
    public String withdrawDate;
    public String withdrawMemo;
    public String delFlag;

    public String phoneAuthToken;
    public String phoneAuthNumber;

    public String selectCommuteInfoDate;

    public String nowAttTime;
    public String nowLeaveTime;

    /*userCommuteState  0   none
    *                   1   오늘 출근을 누른 상태
    *                   2   오늘 퇴근을 누른 상태
    *                   -1  출퇴근정보 서버 통신 실패
    * */
    public int userCommuteState = 0;

    public String userIp;
    public int selectedSubagency_idx;
    public int selectedService_idx;
    public String selectedSubagency_name;
    public String selectedSubagency_addr;
    public String selectedService_name;
    public int attendance_att_idx;
    public int att_idx;
    public boolean isFinRollList = false;



    public static UserInfo getInstance() {
        if (singleton == null) {
            singleton = new UserInfo();
        }

        return singleton;
    }


}
