package com.lovearthstudio.duaui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duasdk.DuaCallback;
import com.lovearthstudio.duasdk.DuaConfig;
import com.lovearthstudio.duasdk.util.DuaPermissionUtil;
import com.lovearthstudio.duaui.DuaActivityLogin;
import com.lovearthstudio.duaui.R;
import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.util.AlertUtil;


public class DuaFragmentProfileName extends BackHandledFragment implements View.OnClickListener{
    private EditText editText_nickName;
    private Button button_last_step;
    private Button button_next_step;
    private DuaActivityLogin activity;
    public DuaFragmentProfileName() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_profile_nickname, container, false);
        editText_nickName=(EditText)view.findViewById(R.id.dua_et_nickname);
        button_last_step=(Button)view.findViewById(R.id.dua_register_button_last_step);
        button_last_step.setOnClickListener(this);
        button_next_step=(Button)view.findViewById(R.id.dua_register_button_next_step);
        button_next_step.setOnClickListener(this);
        activity=(DuaActivityLogin) getActivity();
        activity.leftTitle.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dua_register_button_last_step) {
            activity.onBackPressed();
        } else if (i == R.id.dua_register_button_next_step) {
            String nickName=editText_nickName.getText().toString().trim();
            if (TextUtils.isEmpty(nickName)) {
                AlertUtil.showToast(getContext(),"请填写昵称");
                return;
            }
            activity.name=nickName;
            startRegister();
        }
    }

    public void startRegister(){
        String role="member";
        String in_vode="";
        String type="T";
        DuaCallback duaCallback=new DuaCallback() {
            @Override
            public void onSuccess(Object str) {
                activity.onDismissPressed();
            }

            @Override
            public void onError(int status,String str) {
                String reason= DuaConfig.errCode.get(status);
                if(reason==null){
                    reason=str;
                }
                AlertUtil.showToast(activity,reason);
            }
        };
        Dua.getInstance().register(activity.ustr,activity.pwd,role,activity.vf_code,in_vode,
                type,activity.name,activity.sex,activity.birthday,activity.avatar_url,duaCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.isRootFragment=false;
        activity.leftIcon.setVisibility(View.VISIBLE);
        activity.leftTitle.setVisibility(View.VISIBLE);
        activity.centerTitle.setText("注册");
    }
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
