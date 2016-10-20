package smv.lovearthstudio.com.svmpro_v2.activity.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duaui.DuaActivityLogin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.main.MainActivity;

public class SplashActivity extends Activity {

    FrameLayout mFlSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        findView();
        initAndPlayAnimator();
    }

    private void findView() {
        mFlSplash = (FrameLayout) findViewById(R.id.fl_splash);
    }

    /**
     * 初始化动画,并播放动画
     */
    private void initAndPlayAnimator() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimation.setDuration(2000);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /**
                 * 动画播放结束后处理逻辑
                 * 进入Guide或main
                 */
                // selectPager();
                enterMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFlSplash.startAnimation(alphaAnimation);
    }

    private void selectPager() {
        Dua.DuaUser duaUser = Dua.getInstance().getCurrentDuaUser();
        if (duaUser.logon) {
            enterMain();
        } else {
            startActivityForResult(new Intent(this, DuaActivityLogin.class), 10086);
        }
        finish();
    }

    private void enterMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String operator) {
        if (operator.equals(DuaActivityLogin.LOGIN_SUCCESS)) {
            enterMain();
        }
    }
}
