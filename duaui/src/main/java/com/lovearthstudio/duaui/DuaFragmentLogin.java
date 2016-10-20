package com.lovearthstudio.duaui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duasdk.DuaCallback;
import com.lovearthstudio.duasdk.DuaConfig;
import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.base.ProgressButton;
import com.lovearthstudio.duaui.util.AlertUtil;
import com.lovearthstudio.intlphoneinput.IntlPhoneInput;

public class DuaFragmentLogin extends BackHandledFragment implements OnClickListener{
    private static final int STOP_ANIM = 1;
    private ProgressButton mSignInButton;

    private IntlPhoneInput phoneInputView;
    private EditText mPasswordView;

    public DuaActivityLogin activity;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_ANIM:
                    mSignInButton.stopAnim();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_login,container,false);
        activity=(DuaActivityLogin)getActivity();
        mSignInButton = (ProgressButton) view.findViewById(R.id.dua_login_button_sign_in);
        mSignInButton.setOnClickListener(this);
        mSignInButton.setButtonText("登录");
        mSignInButton.setBgColor(Color.parseColor("#A2E08D"));


        mPasswordView = (EditText) view.findViewById(R.id.dua_login_input_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.dua_action_ime_login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        phoneInputView = (IntlPhoneInput) view.findViewById(R.id.dua_login_input_phone);
        Dua.DuaUser user=Dua.getInstance().getCurrentDuaUser();
        phoneInputView.setNumber(user.tel);
        phoneInputView.setOnValidityChange(new IntlPhoneInput.IntlPhoneInputListener() {
            @Override
            public void done(View view, boolean isValid) {
                if(isValid){
                    mSignInButton.setEnabled(true);
                    mSignInButton.setBgColor(Color.parseColor("#A2E08D"));
                    mPasswordView.requestFocus();
                }else{
                    mSignInButton.setEnabled(false);
                    mSignInButton.setBgColor(Color.parseColor("#DDE08D"));
                    AlertUtil.showToast(activity,"手机号格式不正确");
                }
            }
        });
        TextView textView_register=(TextView)view.findViewById(R.id.dua_login_button_sign_up);
        textView_register.setOnClickListener(this);
        TextView textView_reset_pwd=(TextView)view.findViewById(R.id.dua_login_button_forget_pwd);
        textView_reset_pwd.setOnClickListener(this);
        return view;
    }

    private void attemptLogin(){
        String phone="";
        if(phoneInputView.isValid()){
            phone=phoneInputView.getUstr();
        }
        if (TextUtils.isEmpty(phone)) {
            AlertUtil.showToast(getActivity(),"请输入正确的手机号码");
            phoneInputView.requestFocus();
            return;
        }
        String password = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("请输入密码");
            mPasswordView.requestFocus();
            return;
        }
        mSignInButton.startAnim();
        Dua.getInstance().login(phone,password,"all", new DuaCallback() {
            @Override
            public void onSuccess(Object s) {
                handler.sendEmptyMessage(STOP_ANIM);
                activity.onLoginOk();
            }
            @Override
            public void onError(int status,String s) {
                handler.sendEmptyMessage(STOP_ANIM);
                String reason=DuaConfig.errCode.get(status);
                if(reason==null){
                    reason=s;
                }
                AlertUtil.showToast(activity,reason);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        activity.isRootFragment=true;
        activity.leftIcon.setVisibility(View.INVISIBLE);
        activity.leftTitle.setVisibility(View.INVISIBLE);
        activity.centerTitle.setText("登录");

        if(activity.ustr!=null){
            setPhoneNumber(activity.ustr);
        }
        if(phoneInputView.isValid()){
            mPasswordView.requestFocus();
        }else {
            phoneInputView.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dua_login_button_sign_in) {
            attemptLogin();
        }else if(i == R.id.dua_login_button_sign_up){
            DuaFragmentGetVfCode duaFragmentGetVfCode = new DuaFragmentGetVfCode();
            Bundle args1 = new Bundle();
            args1.putString("mode","register");
            duaFragmentGetVfCode.setArguments(args1);
            activity.setCurrentFragment(duaFragmentGetVfCode,null);
        }else if(i == R.id.dua_login_button_forget_pwd){
            activity.setCurrentFragment(new DuaFragmentGetVfCode(),null);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    public void setPhoneNumber(String ustr){
        int index=ustr.indexOf("-");
        String number=ustr.substring(index+1);
        phoneInputView.setNumber(number);
        String prefix=ustr.substring(1,index);
    }
}

