package com.lovearthstudio.duaui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.base.PasswordEdit;
import com.lovearthstudio.duaui.util.AlertUtil;
import com.lovearthstudio.duaui.util.PasswordChecker;

public class DuaFragmentResetPwd extends BackHandledFragment implements View.OnClickListener{
    private PasswordEdit editText_pwd;
    private PasswordEdit editText_repwd;
    private Button button_next_step;

    private String mode;
    private DuaActivityLogin activity;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_resetpwd, container, false);
        button_next_step=(Button)view.findViewById(R.id.dua_register_button_next_step);
        button_next_step.setOnClickListener(this);
        editText_pwd=(PasswordEdit) view.findViewById(R.id.dua_et_password);
        editText_pwd.requestFocus();
        editText_repwd=(PasswordEdit) view.findViewById(R.id.dua_et_repwd);
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
        if(mode!=null&&mode.equals("ResetPwd")){
            button_next_step.setText("确定");
            activity.centerTitle.setText("重置密码");
        }else {
            activity.centerTitle.setText("注册");
        }
        activity.isRootFragment=false;
        activity.leftIcon.setVisibility(View.VISIBLE);
        activity.leftTitle.setVisibility(View.VISIBLE);
//        button_next_step.setEnabled(!inputIsEmpty(editText_pwd)&&!inputIsEmpty(editText_repwd));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dua_register_button_next_step) {
            String pwd=editText_pwd.getText().toString().trim();
//            String prompt=PasswordChecker.checkPwd(pwd,6,10,1,1,1,false);
//            if(!prompt.equals(PasswordChecker.OK_STR)){
//                editText_pwd.setError(prompt);
//                editText_pwd.requestFocus();
//                return;
//            }
            if(TextUtils.isEmpty(pwd)||pwd.length()<6){
                editText_pwd.setError("请输入至少6位密码");
                editText_pwd.requestFocus();
                return;
            }
            String repwd=editText_repwd.getText().toString().trim();
            if(!pwd.equals(repwd)){
                editText_repwd.setError("两次密码必须相同");
                editText_repwd.requestFocus();
                return;
            }
            activity.pwd=pwd;
            activity.repwd=repwd;
            if(mode!=null&&mode.equals("ResetPwd")){
                startResetPwd();
            }else {
                activity.setCurrentFragment(new DuaFragmentProfileSex(),null);
            }
        }
    }
    public void startResetPwd(){
        AlertUtil.showToast(activity,"修改/找回密功能暂未实现");
        activity.finish();
    }
}
