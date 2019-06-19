package com.yoeki.kalpnay.frdinventry.Login;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.Dashboard.InventoryCounting.dashboardInventoryCount;
import com.yoeki.kalpnay.frdinventry.Model.ChangePaswd;
import com.yoeki.kalpnay.frdinventry.Model.LoginUser;
import com.yoeki.kalpnay.frdinventry.Model.changePswdResponse;
import com.yoeki.kalpnay.frdinventry.Model.login.LoginResponse;
import com.yoeki.kalpnay.frdinventry.Model.login.UserLoginResponse;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.dashboardNew;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    LinearLayout linearLayoutSignIn, linearLayoutForgotPassword;
    TextView textViewSignIn, textViewForgotPassword;
    String signInCLicked = "1";
    private boolean mIsBackVisible = false;
    Button buttonLogin;
    EditText edt_userId, edt_password;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        hideKeyboard(findViewById(R.id.login_container));
        activity = new Activity();
        initialize();

        overridePendingTransition(0, 0);

        textViewSignIn.setText(Html.fromHtml("<u>" + getString(R.string.sign_in) + "</u>"));
        textViewSignIn.setPaintFlags(textViewSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //getSupportActionBar().hide();
        loadAnimations();
        changeCameraDistance(linearLayoutSignIn, linearLayoutForgotPassword);

        linearLayoutForgotPassword.setAlpha(0);
        linearLayoutForgotPassword.setVisibility(View.GONE);

        textViewSignIn.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    private void changeCameraDistance(View linearLayoutSignIn, View linearLayoutForgotPassword) {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        linearLayoutSignIn.setCameraDistance(scale);
        linearLayoutForgotPassword.setCameraDistance(scale);
    }

    private void loadAnimations() {

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);

    }

    public void initialize() {
        linearLayoutSignIn = findViewById(R.id.linearLayoutSignIn);
        linearLayoutForgotPassword = findViewById(R.id.linearLayoutForgotPassword);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        edt_userId = findViewById(R.id.editTextUserPin);
        edt_password = findViewById(R.id.editTextUserPassword);

        buttonLogin.requestFocus();
    }

    public void flipCard() {

        if (!mIsBackVisible) {
            linearLayoutForgotPassword.setVisibility(View.VISIBLE);
            mSetRightOut.setTarget(linearLayoutSignIn);
            mSetLeftIn.setTarget(linearLayoutForgotPassword);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            linearLayoutSignIn.setVisibility(View.VISIBLE);
            mSetRightOut.setTarget(linearLayoutForgotPassword);
            mSetLeftIn.setTarget(linearLayoutSignIn);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewSignIn:
                onSignInClick();
                break;
            case R.id.textViewForgotPassword:
                onForgotPasswordInClick();
                break;
            case R.id.buttonLogin:
//                final int selection = 0;
                String userPin = edt_userId.getText().toString();
                String pwd = edt_password.getText().toString();
                if (userPin.equals("")) {
                    edt_userId.setError("Fill required field");
                } else if (pwd.equals("")) {
                    edt_password.setError("Fill required field");
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Please Wait"); // set message
                    progressDialog.show(); // show progress dialog
                    progressDialog.setCancelable(false); // set cancelable to false
                    ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

                    LoginUser userDetails = new LoginUser(userPin, pwd);

                    Call<LoginResponse> call = apiInterface.login(userDetails);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            progressDialog.dismiss();
                            LoginResponse responseLogin = response.body();

                            List<UserLoginResponse> responseUserId = responseLogin.getUserLoginResponse();
                            String userID = responseUserId.get(0).getUserId();
                            String message = responseLogin.getMessage();
                            String status = responseLogin.getStatus();

                            if (status.equals("success")) {
                                progressDialog.dismiss();
                                Preference.getInstance(getApplicationContext()).saveuserId(userID);
//                                String pswdChangeValue = Preference.getInstance(getApplicationContext()).getPswrdChange();
//                                if(pswdChangeValue.equals("1")){
////                                    Preference.getInstance(getApplicationContext()).setPswrdChange("2");
//                                    try {
//                                        dialogPasswordChange();
//                                    }catch(Exception e){
//                                        e.printStackTrace();
//                                    }
//                                }else{
                                Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
                                startActivity(intent);
                                finish();
//                                }
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

   /* private void openLoginDialog(int dialogAnimation, String s) {
        final Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_successfully_dialog);
        dialog.getWindow().getAttributes().windowAnimations=dialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button ok=dialog.findViewById(R.id.button_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent openRef=new Intent(LoginActivity.this, *//*dashboardInventoryCount*//*dashboardInventoryRequisition.class);
                startActivity(openRef);
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    public void onSignInClick() {

        if (!signInCLicked.equals("1")) {
            textViewSignIn.setText(Html.fromHtml("<u>" + getString(R.string.sign_in) + "</u>"));
            textViewSignIn.setPaintFlags(textViewSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textViewForgotPassword.setText(getString(R.string.forgotPassword));
            textViewForgotPassword.setPaintFlags(0);
            textViewSignIn.setTextColor(getResources().getColor(R.color.white));
            textViewForgotPassword.setTextColor(getResources().getColor(R.color.greyblack));
            flipCard();
            signInCLicked = "1";
        }
    }

    public void onForgotPasswordInClick() {
        if (signInCLicked.equals("1")) {
            textViewSignIn.setText(getString(R.string.sign_in));
            textViewSignIn.setPaintFlags(0);
            textViewForgotPassword.setText(Html.fromHtml("<u>" + getString(R.string.forgotPassword) + "</u>"));
            textViewForgotPassword.setPaintFlags(textViewForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textViewSignIn.setTextColor(getResources().getColor(R.color.greyblack));
            textViewForgotPassword.setTextColor(getResources().getColor(R.color.white));
            flipCard();
            signInCLicked = "";
        }
    }

    public void moduleSelection() {
        TextView txtclose;
        Button btnFollow;
        Dialog myDialog = new Dialog(LoginActivity.this);
        myDialog.setContentView(R.layout.custompopup_select_module);

        LinearLayoutCompat inventoryCountBtn = (LinearLayoutCompat) myDialog.findViewById(R.id.inventoryCountBtn);
        LinearLayoutCompat itemRequisitionBtn = (LinearLayoutCompat) myDialog.findViewById(R.id.itemRequisitionBtn);
        LinearLayoutCompat itemReceivingBtn = (LinearLayoutCompat) myDialog.findViewById(R.id.itemReceivingBtn);

        inventoryCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dashboardInventoryCount.class);
                startActivity(intent);
                finish();
            }
        });

        itemRequisitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), com.yoeki.kalpnay.frdinventry.dashboardInventoryRequisition.class);
                startActivity(intent1);
                finish();
            }
        });

        itemReceivingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void dialogPasswordChange() {
        final EditText confpass;
        AppCompatButton submit;
        Dialog myDialog = new Dialog(LoginActivity.this);
        myDialog.setContentView(R.layout.change_password);


        confpass = (EditText) myDialog.findViewById(R.id.confpass);
        submit = (AppCompatButton) myDialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = confpass.getText().toString();
                if (newPass.equals("")) {
                    Toast.makeText(activity, "Please fill Confirm Password.", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Please Wait"); // set message
                    progressDialog.show(); // show progress dialog
                    progressDialog.setCancelable(false); // set cancelable to false
                    ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
                    String userID = Preference.getInstance(getApplicationContext()).getUserId();
                    ChangePaswd chngepswd = new ChangePaswd(newPass, userID);
                    Call<changePswdResponse> call = apiInterface.changePassword(chngepswd);
                    call.enqueue(new Callback<changePswdResponse>() {
                        @Override
                        public void onResponse(Call<changePswdResponse> call, Response<changePswdResponse> response) {
                            progressDialog.dismiss();
                            changePswdResponse changePaswordResponse = response.body();

                            try {
                                String userId = String.valueOf(changePaswordResponse.getUserId());
                                String pin = String.valueOf(changePaswordResponse.getUserPin());
                                String description = changePaswordResponse.getResDescription();
                                String status = changePaswordResponse.getStatus();
                                if (status.equals("success")) {
                                    progressDialog.dismiss();
                                    moduleSelection();
                                } else {
                                    Toast.makeText(LoginActivity.this, description, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<changePswdResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void hideKeyboard(View parentView) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(parentView instanceof EditText)) {
            parentView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) LoginActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (parentView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) parentView).getChildCount(); i++) {
                View innerView = ((ViewGroup) parentView).getChildAt(i);
                hideKeyboard(innerView);
            }
        }
    }

}
