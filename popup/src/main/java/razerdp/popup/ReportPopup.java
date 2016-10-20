package razerdp.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.library.R;




/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup,有三个按钮
 * 1： 收藏
 * 2： 举报
 * 3： 取消
 */
public class ReportPopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    private Boolean mFlag = false;

    public ReportPopup(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation getShowAnimation() {
        return getTranslateAnimation(250*2,0,300);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView= LayoutInflater.from(mContext).inflate(R.layout.popup_report,null);
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
            popupView.findViewById(R.id.tx_4).setOnClickListener(this);
            popupView.findViewById(R.id.tx_5).setOnClickListener(this);
            popupView.findViewById(R.id.tx_6).setOnClickListener(this);
            popupView.findViewById(R.id.tx_7).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tx_1) {
            mClickListener.onItemClick("report");
        }else if (i == R.id.tx_2) {
            mClickListener.onItemClick("eroticism");
        }else if (i == R.id.tx_3) {
            mClickListener.onItemClick("advertisement");
        }else if (i == R.id.tx_4) {
            mClickListener.onItemClick("politics");
        }else if (i == R.id.tx_5) {
            mClickListener.onItemClick("copyright");
        }else if (i == R.id.tx_6) {
            mClickListener.onItemClick("other");
        }else if (i == R.id.tx_7) {
            mClickListener.onItemClick("cancel");
        }
        if(i != R.id.tx_1)
        {
            dismiss();
        }
    }



    //=============================================================Getter/Setter
    private OnClickListener mClickListener;
    public OnClickListener getClickListener() {
        return mClickListener;
    }

    public void setClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }
    //=============================================================InterFace
    public interface OnClickListener {
        void onItemClick(String which);
    }
}
