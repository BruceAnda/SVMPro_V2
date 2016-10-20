package com.lovearthstudio.duaui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.util.AlertUtil;


public class DuaFragmentProfileSex extends BackHandledFragment implements View.OnClickListener{
    private Button button_last_step;
    private Button button_next_step;
    private ImageView radioMale;
    private ImageView radioFemale;
    private DuaActivityLogin activity;
    public DuaFragmentProfileSex() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dua_fragment_profile_sex, container, false);
        button_last_step=(Button)view.findViewById(R.id.dua_register_button_last_step);
        button_last_step.setOnClickListener(this);
        button_next_step=(Button)view.findViewById(R.id.dua_register_button_next_step);
        button_next_step.setOnClickListener(this);
        radioMale=(ImageView)view.findViewById(R.id.radioMale);
        radioMale.setOnClickListener(this);
        radioFemale=(ImageView)view.findViewById(R.id.radioFemale);
        radioFemale.setOnClickListener(this);
        activity=(DuaActivityLogin) getActivity();
        activity.leftTitle.setVisibility(View.VISIBLE);

        if(activity.sex!=null){
            if(activity.sex.equals("M")){
                radioFemale.setBackgroundResource(R.drawable.gender_female);
                radioMale.setBackgroundResource(R.drawable.male_selected);
            }else if(activity.sex.equals("F")){
                radioMale.setBackgroundResource(R.drawable.gender_male);
                radioFemale.setBackgroundResource(R.drawable.female_selected);
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.radioMale) {
            activity.sex="M";
            radioFemale.setBackgroundResource(R.drawable.gender_female);
            radioMale.setBackgroundResource(R.drawable.male_selected);
        }else if(i == R.id.radioFemale){
            activity.sex="F";
            radioMale.setBackgroundResource(R.drawable.gender_male);
            radioFemale.setBackgroundResource(R.drawable.female_selected);
        } else if (i == R.id.dua_register_button_last_step) {
            activity.onBackPressed();
        } else if (i == R.id.dua_register_button_next_step) {
            if (TextUtils.isEmpty(activity.sex)) {
                AlertUtil.showToast(getContext(),"请选择性别");
                return;
            }
            activity.setCurrentFragment(new DuaFragmentProfileBirthday(),null);
        }
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
