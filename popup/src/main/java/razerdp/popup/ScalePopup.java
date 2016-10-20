package razerdp.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.library.R;
import razerdp.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class ScalePopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    public ScalePopup(Activity context) {
        super(context);
        bindEvent();
    }



    @Override
    protected Animation getShowAnimation() {
        return getDefaultScaleAnimation();
    }


    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView= LayoutInflater.from(mContext).inflate(R.layout.popup_normal,null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tx_1) {
            ToastUtils.ToastMessage(mContext, "click tx_1");

        } else if (i == R.id.tx_2) {
            ToastUtils.ToastMessage(mContext, "click tx_2");

        } else if (i == R.id.tx_3) {
            ToastUtils.ToastMessage(mContext, "click tx_3");

        } else {
        }

    }
}
