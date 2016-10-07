package com.febapp.febapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;
import com.febapp.febapp.Template.Template;
import com.febapp.febapp.Utils.FileManager;
import com.febapp.febapp.Utils.MultiPartRequest;
import com.febapp.febapp.Utils.StringParser;
import com.febapp.febapp.Utils.VolleySingleton;
import com.febapp.febapp.activity.LoginActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button mAdd, mUpload;
    private ImageView mImage, mImagePhoto ,mController;
    private VideoView mVideo;
    private TextView mInfo, mResponse;
    private ProgressBar mProgress,progress;

    private Uri mOutputUri;
    private File mFile;
    private RequestQueue mRequest;
    private MultiPartRequest mMultiPartRequest;
    private MediaPlayer mMediaPlayer;
    private boolean mIsLoad = false;
    private AlertDialog dialog;


    private TextView txtName,txtEmail,tv_message,username,useremail;
    private EditText et_old_password,et_new_password;



    private Button btnLogout;
    private Context context;
//    private SQLiteHandler db;
    private SessionManager session;
    private View view;
    public ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profil_fragment, container, false);

        context = getActivity().getApplicationContext();
        session = new SessionManager(context);


        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        mRequest = VolleySingleton.getInstance().getRequestQueue();
        mAdd = (Button) view.findViewById(R.id.add);

        username =(TextView) view.findViewById(R.id.username);
        useremail = (TextView) view.findViewById(R.id.useremail);
        RelativeLayout usernamelayout = (RelativeLayout) view.findViewById(R.id.username_layout);
        RelativeLayout useremaillayout = (RelativeLayout) view.findViewById(R.id.useremail_layout);
        RelativeLayout userchangepass = (RelativeLayout) view.findViewById(R.id.changepass_layout);


        if (session.isLoggedIn()) {
            userchangepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogs();
                }
            });
            useremaillayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showemail();
                }
            });
            usernamelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showname();
                }
            });
        }else {
            userchangepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,
                            "Please Login to access this", Toast.LENGTH_LONG).show();
                }
            });
            useremaillayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,
                            "Please Login to access this", Toast.LENGTH_LONG).show();
                }
            });
            usernamelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,
                            "Please Login to access this", Toast.LENGTH_LONG).show();
                }
            });
        }
        mUpload = (Button) view.findViewById(R.id.upload);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        resetView();
        if (session.isLoggedIn()) {
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((TextView) view).getText().equals("Delete")) {
                        resetView();
                        if (mIsLoad) {
                            mRequest.cancelAll("MultiRequest");
                            mRequest.stop();
                            mIsLoad = false;
                        }
                    } else {
                        showDialog();
                    }
                }
            });
            mUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadFile();
                    mUpload.setVisibility(Button.INVISIBLE);
                    mProgress.setVisibility(ProgressBar.VISIBLE);
                    mIsLoad = true;
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,
                            "Please Login to access this", Toast.LENGTH_LONG).show();
                }
            });
            mUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,
                            "Please Login to access this", Toast.LENGTH_LONG).show();
                }
            });
        }

        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        final String names = pref.getString("name", "");
        final String emails = pref.getString("email", "");

        //Displaying the user details on the screen
        if (session.isLoggedIn()) {
        username.setText(names);
        useremail.setText(emails);
        }
        else{
            btnLogout.setText("Login");
            username.setText("Guest");
            useremail.setText("Guest Email");
        }


        //Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).logoutUser();
            }
        });
        return view;
    }

    private void showemail(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profil_email, null);
        final EditText uemail = (EditText) view.findViewById(R.id.email);

//        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
//        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
//        tv_message = (TextView)view.findViewById(R.id.tv_message);
//        progress = (ProgressBar)view.findViewById(R.id.progress);

        builder.setView(view);
        builder.setTitle("Change Email");
        builder.setPositiveButton("Change Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        dialog.show();
//        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//        dialog.getWindow().setLayout(width, height);


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String emails = uemail.getText().toString().trim();


                String tag_string_req = "req_login";
                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                final String apikey = pref.getString("apikey", "");

                StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.CHG_EMAIL , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            // Check for error node in json
                            if (!error) {
                                String errorMsg = jObj.getString("message");
                                Toast.makeText(context,
                                        errorMsg, Toast.LENGTH_LONG).show();
                                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email",emails);
                                editor.commit();
                                useremail.setText(emails);
                                dialog.dismiss();

                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("message");
                                Toast.makeText(context,
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", emails);


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new HashMap<String, String>();
                        headers.put("authorization",apikey);
                        return headers;
                    }
                };

                // ngebuat request default timeoutnya jadi 2x lipat. nah kalo udah gini requestnya jadi gak double. email yg ke kirim pun jadinya gak double
                sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(sr, tag_string_req);
                }
        });
    }

    private void showname(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profil_name, null);
        final EditText uname = (EditText) view.findViewById(R.id.name);

//        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
//        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
//        tv_message = (TextView)view.findViewById(R.id.tv_message);
//        progress = (ProgressBar)view.findViewById(R.id.progress);

        builder.setView(view);
        builder.setTitle("Change Name");
        builder.setPositiveButton("Change Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        dialog.show();
//        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//        dialog.getWindow().setLayout(width, height);


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String unames = uname.getText().toString().trim();

                changeName(unames);

                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("name",unames);
                editor.commit();
//                SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//                final String names = prefs.getString("name", "");
                username.setText(unames);

                dialog.dismiss();
//                getActivity().finish();
                Intent intents = new Intent(getActivity(), LoginActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
//
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);



            }
        });
    }

    private void changeEmail(final String emails){


    }



    private void changeName(final String uname){
        String tag_string_req = "req_login";
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final String apikey = pref.getString("apikey", "");

        StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.CHG_NAME , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,
                        "Succesfully Change Name", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", uname);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("authorization",apikey);
                return headers;
            }
        };

        // ngebuat request default timeoutnya jadi 2x lipat. nah kalo udah gini requestnya jadi gak double. email yg ke kirim pun jadinya gak double
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr, tag_string_req);

    }

        private void showPhoto(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profil_photo, null);
        mImagePhoto = (ImageView) view.findViewById(R.id.imagePhoto);
//        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
//        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
//        tv_message = (TextView)view.findViewById(R.id.tv_message);
//        progress = (ProgressBar)view.findViewById(R.id.progress);

        builder.setView(view);
        builder.setTitle("Change Photo");
        builder.setPositiveButton("Change Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        dialog.show();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadFile();
                Toast.makeText(context,
                        "Succesfully Change Photo", Toast.LENGTH_LONG).show();
                mIsLoad = true;
                //disini buat ngakalin error null set response volley multipart httpentity
//                dialog.dismiss();
                getActivity().finish();
                Intent intents = new Intent(getActivity(), LoginActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intents);
//
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


    private void showDialogs(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);

        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {
                    SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    final String emails = pref.getString("email", "");

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(emails, old_password, new_password);

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePasswordProcess(final String emails, final String old_password, final String new_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CHG_PASS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean result = jObj.getBoolean("result");

                        // Create login session
                    if (result) {
                        session.setLogin(true);

                        //ini besok lo masukin ke sharedpref
                        // Now store the user in SQLite
                        String uid = jObj.getString("api_key");
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String created_at = jObj.getString("createdAt");
                        String message = jObj.getString("message");

                        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("apikey", uid);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("created_at", created_at);
                        editor.commit();


                        // Inserting row in users table

                        progress.setVisibility(View.GONE);
                        tv_message.setVisibility(View.GONE);
                        dialog.dismiss();
                        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                    }else{
                        String message = jObj.getString("message");
                        progress.setVisibility(View.GONE);
                        tv_message.setVisibility(View.GONE);

                        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error

                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Succesfully Change Password", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.GONE);

//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", emails);
                params.put("old_password", old_password);
                params.put("new_password", new_password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

     //TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void showDialog() {
        String[] CHOOSE_FILE = {"Camera", "File manager"};
        new MaterialDialog.Builder(getActivity()).title("Choose file")
                .items(CHOOSE_FILE)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {


                        if (i == 0) {
                            //Mengambil foto dengan camera
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            mOutputUri = FileManager.getOutputMediaFileUri(Template.Code.CAMERA_IMAGE_CODE);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);


                            startActivityForResult(intent, Template.Code.CAMERA_IMAGE_CODE);
                        } //else if (i == 1) {
//                            //Mengambil video dengan camera
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//                            mOutputUri = FileManager.getOutputMediaFileUri(Template.Code.CAMERA_VIDEO_CODE);
//
//
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);
//                            startActivityForResult(intent, Template.Code.CAMERA_VIDEO_CODE);
//                        }
                        else {
                            //Mendapatkan file dari storage
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/* ");
                            startActivityForResult(intent, Template.Code.FILE_MANAGER_CODE);
                        }
                    }
                }).show();
    }

    //Respon dari upload button ketika diklik, untuk melakukan upload file ke server
    void uploadFile() {

        mRequest.start();
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final String apikeys = pref.getString("apikey", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", apikeys);

        mMultiPartRequest = new MultiPartRequest(new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
//                mUpload.setVisibility(Button.VISIBLE);
                mProgress.setVisibility(ProgressBar.GONE);
                mIsLoad = false;
                setResponse(null, error);
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
//                mUpload.setVisibility(Button.VISIBLE);
                mProgress.setVisibility(ProgressBar.GONE);
                mIsLoad = false;
                setResponse(response, null);

            }
        }, mFile,headers);
        //Set tag, diperlukan ketika akan menggagalkan request/cancenl request
        mMultiPartRequest.setTag("MultiRequest");
        //Set retry policy, untuk mengatur socket time out, retries. Bisa disetting lewat template
        mMultiPartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Menambahkan ke request queue untuk diproses
        mRequest.add(mMultiPartRequest);
    }

    //Mengisi variable File dari path yang didapat dari storage
    void setFile(int type, Uri uri) {
        mFile = new File(FileManager.getPath(getActivity().getApplicationContext(), type, uri));
    }

    //Respon ketika path file dari storage didapatkan, untuk menampilkan view untuk upload
    void setView(int type, Uri uri) {
//        mUpload.setVisibility(Button.VISIBLE);
//        mAdd.setText("Delete");
//        mInfo.setVisibility(TextView.VISIBLE);
//        mInfo.setText("File info\n" + "Name : " + mFile.getName() + "\nSize : " +
//                FileManager.getSize(mFile.length(), true));
        File file = new File(FileManager.getPath(getActivity().getApplicationContext(), type, uri));
        type = FileManager.fileType(file);
        if (type == Template.Code.CAMERA_IMAGE_CODE) {
            showPhoto();
            mImagePhoto.setImageBitmap(BitmapFactory.decodeFile(FileManager.getPath(getActivity().getApplicationContext(), type, uri)));
//            mImage.setVisibility(ImageView.VISIBLE);
//            mImage.setImageBitmap(BitmapFactory.decodeFile(FileManager.getPath(getActivity().getApplicationContext(), type, uri)));
        } else if (type == Template.Code.CAMERA_VIDEO_CODE) {
//            mVideo.setVisibility(VideoView.VISIBLE);
//            mVideo.setVideoPath(FileManager.getPath(getApplicationContext(), type, uri));
//            mController.setVisibility(ImageView.VISIBLE);
//            mController.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (mVideo.isPlaying()) {
//                        mController.setImageResource(R.drawable.ic_play);
//                        mVideo.pause();
//                   } else {
//
//                        mController.setImageResource(R.drawable.ic_pause);
//                        mVideo.start();
//                    }
//                }
//            });
//            mVideo.start();
        } else {

            file = new File(FileManager.getPath(getActivity().getApplicationContext(), type, uri));
            int fileType = FileManager.fileType(file);
            if (fileType == Template.Code.CAMERA_IMAGE_CODE) {
                showPhoto();
                mImagePhoto.setImageBitmap(BitmapFactory.decodeFile(FileManager.getPath(getActivity().getApplicationContext(), type, uri)));
//                mImage.setVisibility(ImageView.VISIBLE);
//                mImage.setImageBitmap(BitmapFactory.decodeFile(FileManager.getPath(getActivity().getApplicationContext(), type, uri)));
            } else if (fileType == Template.Code.CAMERA_VIDEO_CODE) {
//                mVideo.setVisibility(VideoView.VISIBLE);
//                mVideo.setVideoPath(FileManager.getPath(getApplicationContext(), type, uri));
//                mController.setVisibility(ImageView.VISIBLE);
//                mController.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (mVideo.isPlaying()) {
//                            mController.setImageResource(R.drawable.ic_play);
//                            mVideo.pause();
//                        } else {
//
//                            mController.setImageResource(R.drawable.ic_pause);
//                            mVideo.start();
//                        }
//                    }
//                });
//                mVideo.start();
            } else if (fileType == Template.Code.AUDIO_CODE) {
//                mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//                mMediaPlayer.setLooping(true);
//                mController.setVisibility(ImageView.VISIBLE);
//                mController.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (mMediaPlayer.isPlaying()) {
//                            mController.setImageResource(R.drawable.ic_play);
//                            mMediaPlayer.pause();
//                        } else {
//
//                            mController.setImageResource(R.drawable.ic_pause);
//                            mMediaPlayer.start();
//                        }
//                    }
//                });
//                mMediaPlayer.start();
            } else {
                mImagePhoto.setImageResource(R.mipmap.ic_nice);
//                mImage.setVisibility(ImageView.VISIBLE);
//                mImage.setImageResource(R.drawable.ic_android_green_501_48dp);
            }

        }
    }

    //Mereset tampilan ke semula
    void resetView() {
        mUpload.setVisibility(Button.GONE);
//        mImage.setVisibility(ImageView.GONE);
//        mVideo.setVisibility(VideoView.GONE);


        mAdd.setText("Change Profile Picture");
        mProgress.setVisibility(ProgressBar.GONE);
//        mController.setVisibility(ImageView.GONE);
//        mController.setImageResource(R.drawable.ic_pause);
//        if (mVideo.isPlaying())
//            mVideo.pause();
//        if (mMediaPlayer!=null&&mMediaPlayer.isPlaying())
//            mMediaPlayer.pause();
    }

    //Respon dari volley, untuk menampilkan keterengan upload, seperti error, message dari server
    void setResponse(Object response, VolleyError error) {
        if (response == null) {
            Toast.makeText(context,
                    "Error\n" + error, Toast.LENGTH_LONG).show();
//            mResponse.setText("Error\n" + error);
        } else {
            if (StringParser.getCode(response.toString()).equals(Template.Query.VALUE_CODE_SUCCESS))
            {
//                Toast.makeText(context,
//                        "Success\n" + StringParser.getMessage(response.toString()), Toast.LENGTH_LONG).show();


//                mResponse.setText("Success\n" + StringParser.getMessage(response.toString()));
            }
            else{
                Toast.makeText(context,
                        "Error\n" + StringParser.getMessage(response.toString()), Toast.LENGTH_LONG).show();
//                mResponse.setText("Error\n" + StringParser.getMessage(response.toString()));
            }
        }
    }

    //Respon dari pengambilan data dari storage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Template.Code.FILE_MANAGER_CODE) {
                setFile(requestCode, data.getData());
                setView(requestCode, data.getData());
            } else {
                setFile(requestCode, mOutputUri);
                setView(requestCode, mOutputUri);
            }

        } else {
            resetView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
