package com.lovearthstudio.duaui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duasdk.DuaCallback;
import com.lovearthstudio.duasdk.DuaConfig;
import com.lovearthstudio.duasdk.util.LogUtil;
import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.util.AlertUtil;

import com.lovearthstudio.duaui.util.ThreadUtil;
import com.lovearthstudio.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

public class DuaFragmentGetVfCode extends BackHandledFragment implements View.OnClickListener{
    private IntlPhoneInput intlPhoneInput;
    private EditText editText_vf_code;
    private Button button_get_vf_code;
    private Button button_next_step;
    private DuaActivityLogin activity;
    private static final int UPDATE_TIMER_TICK =10010;
    private static final int UPDATE_TIMER_FINISH=10086;
    private String mode;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIMER_TICK:
                    button_get_vf_code.setEnabled(false);
                    button_get_vf_code.setText((String)msg.obj);
                    break;
                case UPDATE_TIMER_FINISH:
                    button_get_vf_code.setEnabled(true);
                    button_get_vf_code.setText(R.string.dua_button_get_vf_code);
                    intlPhoneInput.setEnabled(true);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_getvfcode, container, false);
        intlPhoneInput=(IntlPhoneInput) view.findViewById(R.id.dua_register_input_phone);
        editText_vf_code=(EditText)view.findViewById(R.id.dua_register_input_vf_code);
        button_get_vf_code=(Button)view.findViewById(R.id.dua_register_button_get_vf_code);
        button_next_step=(Button)view.findViewById(R.id.dua_register_button_next_step);
        try {
            mode=getArguments().getString("mode");
        }catch (Exception e){

        }
        activity=(DuaActivityLogin) getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean bl=intlPhoneInput.isValid();
        button_get_vf_code.setEnabled(bl);
        button_get_vf_code.setOnClickListener(this);
        button_next_step.setOnClickListener(this);
        intlPhoneInput.setOnValidityChange(new IntlPhoneInput.IntlPhoneInputListener() {
            @Override
            public void done(View view, boolean isValid) {
                if(isValid){
                    button_get_vf_code.setEnabled(true);
                    editText_vf_code.requestFocus();
                }else{
                    button_get_vf_code.setEnabled(false);
                    intlPhoneInput.requestFocus();
                }
            }
        });

        activity.isRootFragment=false;
        activity.leftIcon.setVisibility(View.VISIBLE);
        activity.leftTitle.setVisibility(View.VISIBLE);
        if(mode!=null&&mode.equals("register")){
            activity.centerTitle.setText("注册");
        }else {
            activity.centerTitle.setText("重置密码");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dua_register_button_get_vf_code) {
            if(checkUstr()){
                intlPhoneInput.setEnabled(false);
				button_get_vf_code.setEnabled(false);
                Dua.getInstance().getVfCode(activity.ustr,new DuaCallback() {
                    @Override
                    public void onSuccess(Object str) {
                        ThreadUtil.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                startCountDownTimer();
                            }
                        });
                    }
                    @Override
                    public void onError(int status,String str) {
                        updateUI(UPDATE_TIMER_FINISH,null);
                        AlertUtil.showToast(activity,status+" "+str);
                    }
                });
            }
        } else if (i == R.id.dua_register_button_next_step) {
            if(checkUstr()) {
                String vf_code = editText_vf_code.getText().toString().trim();
                if (TextUtils.isEmpty(vf_code)) {
                    editText_vf_code.setError("请输入短信验证码");
                    editText_vf_code.requestFocus();
                    return;
                }
                activity.vf_code = vf_code;
                checkVfCode(activity.ustr, vf_code);
            }
        }
    }
    public void checkVfCode(String ustr,String vf_code){
        Dua.getInstance().checkVfCode(ustr, vf_code, new DuaCallback() {
            @Override
            public void onSuccess(Object result) {
                if(mode!=null&&mode.equals("register")){
                    try{
                        JSONObject jo=(JSONObject)result;
                        if(jo.getInt("ustr_exist")>0){
                            activity.showLoginDialog();
                        }else {
                            activity.setCurrentFragment(new DuaFragmentResetPwd(),null);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    DuaFragmentResetPwd duaFragmentResetPwd = new DuaFragmentResetPwd();
                    Bundle args2 = new Bundle();
                    args2.putString("mode","ResetPwd");
                    duaFragmentResetPwd.setArguments(args2);
                    activity.setCurrentFragment(duaFragmentResetPwd,null);
                }
            }

            @Override
            public void onError(int status, String s) {
                String reason= DuaConfig.errCode.get(status);
                if(reason==null){
                    reason=s;
                }
                AlertUtil.showToast(activity,reason);
            }
        });
    }

    PhoneNumberUtil phoneNumberUtil=PhoneNumberUtil.getInstance();
    public boolean checkUstr(){
        String phone="";
        try {
            LogUtil.e(intlPhoneInput.getUstr());
            LogUtil.e(intlPhoneInput.getPhoneNumber().toString());
            LogUtil.e("是否合格" + phoneNumberUtil.isValidNumber(intlPhoneInput.getPhoneNumber()));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(intlPhoneInput.isValid()){
            phone=intlPhoneInput.getUstr();
        }
        if (TextUtils.isEmpty(phone)) {
            AlertUtil.showToast(getActivity(),"请输入正确的手机号码");
            intlPhoneInput.requestFocus();
            return false;
        }
        activity.ustr=phone;
        return true;
    }
    private CountDownTimer timer;
    private void startCountDownTimer(){
        if(timer==null){
            timer=new CountDownTimer(60000,1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    updateUI(UPDATE_TIMER_TICK,millisUntilFinished/1000+"秒");
                }
                @Override
                public void onFinish() {
                    updateUI(UPDATE_TIMER_FINISH,null);
                }
            };
        }
        timer.start();
    }
    private void cancelCountDownTimer(){
        if(timer!=null){
            timer.cancel();
        }
    }
    private void updateUI(int what,Object obj){
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj=obj;
        msg.sendToTarget();
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
