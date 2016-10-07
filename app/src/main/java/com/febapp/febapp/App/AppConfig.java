package com.febapp.febapp.App;

/**
 * Created by Philipus on 20/02/2016.
 */
public class AppConfig {
    // Server user login url
//    public static String URL_LOGIN = "http://192.168.0.13:80/task_manager/v1/login";
//
//    // Server user register url
//    public static String URL_REGISTER = "http://192.168.0.13/task_manager/v1/register";
//
//    //URL of my even
//    public static final String DATA_URL = "http://192.168.0.13/task_manager/v1/even/";
//
//    public static final String DATA_PROFIL = "http://192.168.0.13/task_manager/v1/profil";
//    public static final String DATA_NOTIF = "http://192.168.0.13/task_manager/v1/tasks";
//    public static final String NOTIF_HAPUS = "http://192.168.0.13/task_manager/v1/hapus";
//
//    public static final String DATA_URL_DETAIL = "http://192.168.0.13/task_manager/v1/detaileven/";
//    public static final String DATA_MAPS = "http://192.168.0.13/task_manager/v1/map";

    public static String url= "http://ktmonlinesystem.com/febapp/v1";
//    static String url= "http://192.168.0.12/task_manager/v1";
    public static String URL_LOGIN = url+"/login";

    public static String ANDEVINDO =  url+"/gambar";

    // Server user register url
    public static String URL_REGISTER = url+"/register";
    public static String DATA_LIST = url+"/eveng/";
    public static String CART = url+"/getCart/";


    public static String RESET_PASS_REQ= url+"/resetPasswordRequest";
    public static String RESET_PASS = url+"/resetPassword";

    public static String CHG_PASS = url+"/chgpass";
    public static String CHG_NAME = url+"/changeName";
    public static String CHG_EMAIL = url+"/changeEmail";

    public static String DETAIL_GAMBAR = url+"/detailGambar/";

    //URL of my even

    public static final String DATA_URL = url+"/even/";

    public static final String DATA_URL_NEW = url+"/evenNew/";
    public static final String DATA_URL_POPULAR = url+"/evenPopular/";

    public static final String DATA_GET_NOTIF = url+"/getbook/";

//    public static final String DATA_PROFIL = url+"/profil";
    public static final String PROFIL = url+"/profil";
    public static final String DATA_NOTIF = url+"/postbook";
    public static final String incQty = url+"/incQty";
    public static final String decQty = url+"/decQty";
    public static final String clearStat = url+"/clearStat";
    public static final String insertOrder = url+"/insertOrder";
    public static final String delQty = url+"/delJual";
    public static final String INS_JUAL = url+"/insertJual";
    public static final String NOTIF_HAPUS = url+"/hapusbook";
    public static final String DATA_CLICK = url+"/changeClick";

    public static final String DATA_URL_DETAIL = url+"/detaileven/";
    public static final String DATA_MAPS = url+"/map";

    //Tags for my JSON
    public static final String TAG_NO      = "no";
    public static final String TAG_PROFIL  = "gambar";
    public static final String TAG_ID      = "id";
    public static final String TAG_IMAGE_URL = "gambar";
    public static final String TAG_IMAGE_URLS = "gambars";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_TASK = "task";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_DUIT = "duit";
    public static final String TAG_PERSEN = "persen";
    public static final String TAG_SISA_HARI = "sisahari";
    public static final String TAG_TOTAL = "total";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG = "lng";
    public static final String TAG_DATE = "date";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_WEB = "web";
    public static final String TAG_YOUTUBE = "youtube";
    public static final String TAG_INSTAGRAM = "instagram";
    public static final String TAG_FB = "fb";
    public static final String TAG_FAQ = "faq";
    public static final String TAG_KATEGORI_ACARA = "kategori_acara";
    public static final String TAG_TWITTER = "twitter";
    public static final String TAG_LOKASI = "lokasi";
    public static final String TAG_START_DATE = "start_date";
    public static final String TAG_END_DATE = "end_date";
    public static final String TAG_SLUG = "slug";
    public static final String TAG_BOOKMARK = "bookmark";
}
