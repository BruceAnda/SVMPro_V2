package smv.lovearthstudio.com.svmpro_v2.app;

import android.app.Application;

import com.lovearthstudio.duasdk.Dua;

/**
 * Application
 *
 * @author zhaoliang
 *         create at 16/11/10 上午10:07
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDua();
    }

    /**
     * 初始化Dua，登录模块
     *
     * @author zhaoliang
     * create at 16/11/10 上午10:07
     */
    private void initDua() {
        Dua.init(this);
    }
}
