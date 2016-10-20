package com.lovearthstudio.duaui;


import android.Manifest;
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
import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.util.AlertUtil;
import com.lovearthstudio.duasdk.util.DuaPermissionUtil;


public class DuaFragmentProfileAvata extends BackHandledFragment implements View.OnClickListener{
    private ImageView userAvatar;
    private Button button_last_step;
    private Button button_next_step;
    private DuaActivityLogin activity;
    public DuaFragmentProfileAvata() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_profile_avata, container, false);
        userAvatar=(ImageView)view.findViewById(R.id.dua_register_iv_avatar);
        userAvatar.setOnClickListener(this);
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
            activity.setCurrentFragment(new DuaFragmentProfileName(),"Name");
        }else if(i==R.id.dua_register_iv_avatar){
            if(DuaPermissionUtil.invalidSdAndCamera(getActivity())) {
                DuaPermissionUtil.requestSdAndCamera(getActivity(),10086);
            }else {
                activity.showPhotoDialog(userAvatar);
            }
        }
    }

    public void setNextStepEnable(boolean able){
        button_next_step.setEnabled(able);
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
