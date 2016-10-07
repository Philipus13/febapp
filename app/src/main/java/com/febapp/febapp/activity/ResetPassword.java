package com.febapp.febapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipus on 06/05/2016.
 */
public class ResetPassword extends AppCompatActivity {

    private AppCompatButton btn_reset;
    private EditText et_email,et_code,et_password;
    private TextView tv_timer;
    private ProgressBar progress;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;
    private LinearLayout root;
    String message;
    private static final String TAG = ResetPassword.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        root = (LinearLayout) findViewById(R.id.resetRoot);
        btn_reset = (AppCompatButton) findViewById(R.id.btn_reset);
        tv_timer = (TextView) findViewById(R.id.timer);
        et_code = (EditText)findViewById(R.id.et_code);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        tv_timer.setVisibility(View.GONE);
        progress = (ProgressBar) findViewById(R.id.progress);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResetInitiated) {

                    email = et_email.getText().toString();
                    if (!email.isEmpty()) {
                        progress.setVisibility(View.VISIBLE);
                        initiateResetPasswordProcess(email);
                    } else {

                        Snackbar.make(root, "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    String code = et_code.getText().toString();
                    String password = et_password.getText().toString();

                    if (!code.isEmpty() && !password.isEmpty()) {

                        finishResetPasswordProcess(email, code, password);
                    } else {

                        Snackbar.make(root, "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    private void initiateResetPasswordProcess(final String email){
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESET_PASS_REQ, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean result = jObj.getBoolean("result");
                    message = jObj.getString("message");

                    if (result) {
                    Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                    et_email.setVisibility(View.GONE);
                    et_code.setVisibility(View.VISIBLE);
                    et_password.setVisibility(View.VISIBLE);
                    tv_timer.setVisibility(View.VISIBLE);
                    btn_reset.setText("Change Password");
                    isResetInitiated = true;
                    startCountdownTimer();

                    }
                    else {
                        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    // JSON error

                    Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.INVISIBLE);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "failed");
                Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };
        // ngebuat request default timeoutnya jadi 2x lipat. nah kalo udah gini requestnya jadi gak double. email yg ke kirim pun jadinya gak double
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void finishResetPasswordProcess(final String email,final String code, final String password){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESET_PASS, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);


                    boolean result = jObj.getBoolean("result");
                    message = jObj.getString("message");

                    if (result) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    countDownTimer.cancel();
                    isResetInitiated = false;
                    goToLogin();

                    }else{
                        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    // JSON error

                    Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.INVISIBLE);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "failed");
                Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("code", code);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                Snackbar.make(root, "Time Out ! Request again to reset password.", Snackbar.LENGTH_LONG).show();
                goToLogin();
            }
        }.start();
    }

    private void goToLogin(){

        Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
