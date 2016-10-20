package com.lovearthstudio.duaui.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.lovearthstudio.duaui.R;


public class PasswordEdit extends EditText{

    private final static int VISIBILITY_ENABLED = (int) (255 * .54f);
    private final static int VISIBILITY_DISABLED = (int) (255 * .38f);

    private Drawable eye;
    private Drawable eyeWithStrike;

    private boolean visible = false;
    private boolean useStrikeThrough = true;
    private int eyeTint = Color.BLACK;
    private boolean eyeClicked;

    public PasswordEdit(Context context) {
        super(context);
        init(null);
    }

    public PasswordEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
//                    attrs,
//                    R.styleable.PasswordView,
//                    0, 0);
//            useStrikeThrough = typedArray.getBoolean(R.styleable.PasswordView_strikeThrough, false);
//            eyeTint = typedArray.getColor(R.styleable.PasswordView_eyeTint, Color.BLACK);
//            typedArray.recycle();
//        }
        eye = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye).mutate();
        eyeWithStrike =  ContextCompat.getDrawable(getContext(), R.drawable.ic_eye_strike).mutate();
        setUp();
    }
    private void setUp() {
        DrawableCompat.setTint(eye, eyeTint);
        DrawableCompat.setTint(eyeWithStrike, eyeTint);
        eyeWithStrike.setAlpha(VISIBILITY_DISABLED);
        apply();
    }
    protected void apply() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        setSelection(start, end);
        Drawable drawable = useStrikeThrough && !visible ? eyeWithStrike : eye;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null, drawables[3]);//解决setError后eye无法切换
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        eye.setAlpha(visible || useStrikeThrough ? VISIBILITY_ENABLED : VISIBILITY_DISABLED);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int drawableRightX = getWidth() - getPaddingRight();
        int drawableLeftX = drawableRightX - getCompoundDrawables()[2].getBounds().width();
        boolean rightDrawableClicked = event.getX() >= drawableLeftX && event.getX() <= drawableRightX;
//        boolean notErrorDrawable=eye.isVisible()||eyeWithStrike.isVisible();  //经测实没作用
        boolean notErrorDrawable= getError()==null;
        if (event.getAction() == MotionEvent.ACTION_DOWN && rightDrawableClicked && notErrorDrawable) {
            eyeClicked = true;
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (rightDrawableClicked && eyeClicked) {
                eyeClicked = false;
                visible = !visible;
                apply();
                invalidate();
                return true;
            }else {
                eyeClicked=false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override public void setInputType(int type) {
        super.setInputType(type);
        setTypeface(getTypeface());
    }

    public void useStrikeThrough(boolean useStrikeThrough) {
        this.useStrikeThrough = useStrikeThrough;
    }

    public void setEyeTint(@ColorInt int eyeTint) {
        this.eyeTint = eyeTint;
        setUp();
    }
}
