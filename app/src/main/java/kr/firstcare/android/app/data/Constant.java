package kr.firstcare.android.app.data;


import android.util.Log;

import lombok.Data;


/**
 * ClassName            Constant
 * Created by JSky on   2020-06-03
 *
 * Description          앱 기본 정의
 */

public class Constant {

    /*FirstCare Server URL*/
    public static final String SERVER_URL = "http://raon-soft.com/firstcare_mobile/";

    /*FirstCare Teacher Profile URL*/
    public static final String TEACHER_PROFILE_IMG_URL ="https://firstcare.kr/_public/uploadFiles/vteacher_data/";

    /*FirstCare Child Profile URL*/
    public static final String CHILD_PROFILE_IMG_URL ="https://firstcare.kr/_public/uploadFiles/child_data/";

    /*도로명 주소 검색 파일 URL - 수정 필요*/
    public static final String SEARCH_ADDRESS_URL = "http://raon-soft.com/firstcare_mobile/search_addr_android/";

    /*퍼미션*/
    public static  int ACCESS_FINE_LOCATION_STAT = 1;
    public static  int ACCESS_BACKGROUND_LOCATION_STAT = 1;

    public static final int PROGRESS_COUNT = 3;

    /*위치 정보 받아오기 실패 했을 때, 기본 위경도 값 (사용안함)*/
//    public static final double DefaultLatitude = 35.1750569;
//    public static final double DefaultLongitude = 129.1250885;

    /*이용약관 파일*/
    public static final String FILE_TERM_OF_USE = "fc_termofuse.txt";
    public static final String FILE_PRIVACY = "fc_privacy.txt";


    /*로그 찍기, DEBUG_MODE = true 일때만 로그 찍힘*/
    public static void LOG(String tag, String text){
        if(UserInfo.getInstance().DEBUG_MODE){
            Log.e(tag, text);
        }

    }



}