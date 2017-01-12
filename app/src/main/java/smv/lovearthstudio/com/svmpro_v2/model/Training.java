package smv.lovearthstudio.com.svmpro_v2.model;

import io.realm.RealmObject;

/**
 * Created by zhaoliang on 2017/1/10.
 */

public class Training extends RealmObject {

    private String values;

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
