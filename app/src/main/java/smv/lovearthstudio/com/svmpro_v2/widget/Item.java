package smv.lovearthstudio.com.svmpro_v2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import smv.lovearthstudio.com.svmpro_v2.R;

/**
 * 自定义Item
 * Created by zhaoliang on 2016/11/11.
 */

public class Item extends LinearLayout {

    private ImageView mIvIcon, mIvArray;
    private TextView mTvTitle;

    private int mResourceId;
    private String mText;
    private boolean mShowArray;

    /**
     * 是否显示右边箭头
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:19
     */
    public void showArray(boolean isShowArray) {
        if (isShowArray) {
            mIvArray.setVisibility(VISIBLE);
        } else {
            mIvArray.setVisibility(GONE);
        }
    }

    /**
     * 设置显示的内容
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:21
     */
    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvTitle.setText(text);
        }
    }

    /**
     * 设置显示图标
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:22
     */
    public void setIcon(int resourceId) {
        if (resourceId != 0) {
            mIvIcon.setImageResource(resourceId);
        }
    }

    public Item(Context context) {
        super(context);
    }

    public Item(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:18
     */
    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.item_layout, this);

        findView();

        readAttribute(context, attrs);

        refreshItemState();
    }

    /**
     * 查找控件
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:17
     */
    private void findView() {
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvArray = (ImageView) findViewById(R.id.iv_array);
    }

    /**
     * 读取属性
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:17
     */
    private void readAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Item);
        mResourceId = typedArray.getResourceId(R.styleable.Item_icon, 0);
        mText = typedArray.getString(R.styleable.Item_text);
        mShowArray = typedArray.getBoolean(R.styleable.Item_show_array, true);
    }

    /**
     * 更新item显示内容
     *
     * @author zhaoliang
     * create at 2016/11/22 下午3:17
     */
    private void refreshItemState() {
        setIcon(mResourceId);
        setText(mText);
        showArray(mShowArray);
    }

    public Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
