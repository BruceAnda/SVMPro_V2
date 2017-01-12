package smv.lovearthstudio.com.svmpro_v2.app;

import android.app.Application;

import com.lovearthstudio.duasdk.Dua;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
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
