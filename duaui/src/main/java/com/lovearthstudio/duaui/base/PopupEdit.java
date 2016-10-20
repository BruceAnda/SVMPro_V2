package com.lovearthstudio.duaui.base;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.lovearthstudio.duaui.R;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.InputMethodUtils;

/**
 * Created by lr on 2016/8/3.
 */
public class PopupEdit extends BasePopupWindow implements View.OnClickListener{

    private Button btn_commit;
    private View popupView;
    private EditText mEditText;
    private OnCommitListener listener;
    public PopupEdit(Activity context) {
        super(context);
        init();
        setAdjustInputMethod(true);
        InputMethodUtils.showInputMethod(mEditText, 10);
    }

    private void init() {
        mEditText = (EditText) popupView.findViewById(R.id.comment_editText);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        btn_commit=(Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        findViewById(R.id.cancel_layout).setOnClickListener(this);
    }

    @Override
    protected Animation getShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_edit, null);
        return popupView;
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        popupView.findViewById(R.id.comment_editText).requestFocus();
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    public void setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id == R.id.btn_cancel||id==R.id.cancel_layout) {
            dismiss();
        } else if(id==R.id.btn_commit){
            String content = mEditText.getText().toString();
            if (listener != null && !TextUtils.isEmpty(content)) {
                listener.onClick(view, content);
                dismiss();
            }
        }
    }

    public interface OnCommitListener {
        void onClick(View view, String content);
    }
}
