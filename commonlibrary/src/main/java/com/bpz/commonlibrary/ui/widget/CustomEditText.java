package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpz.commonlibrary.R;

import org.jetbrains.annotations.NotNull;


/**
 * Created by Administrator on 2018/1/11.
 * 自定义文本输入框
 */

public class CustomEditText extends LinearLayout {
    public static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
    public static final int DEFAULT_HINT_COLOR = 0x99FFFFFF;
    public static final int DEFAULT_TEXT_SIZE = 14;
    public static final int DEFAULT_PADDING = 0;
    View mRootView;
    ImageView customEtIvLeft;
    EditText customEt;
    ImageView customEtIvRightF;
    ImageView customEtIvRightS;
    LinearLayout customEtLl;
    View customEtVBottom;
    LinearLayout.LayoutParams customEtLayoutParams;
    LinearLayout.LayoutParams customLlLayoutParams;
    //左边的图标
    @DrawableRes
    private int mLeftImgRes;
    //右边第一个图标
    @DrawableRes
    private int mRightImgResFirst;
    //右边第二个图标
    @DrawableRes
    private int mRightImgResSecond;
    //文字颜色
    private int mTextColor;
    //提示文字颜色
    private int mHintColor;
    //文字大小
    private float mTextSize;
    //背景颜色
    private int mBackgroundColor;
    //输入框距左图的边距
    private float mPaddingStart;
    //输入框距右图的边距
    private float mPaddingEnd;
    //输入框距底部分割线的边距
    private float mPaddingBottom;
    //是否显示分割线，true 显示
    private boolean mShowDivideLine = true;
    //分割线的颜色
    private int mDivideLineColor;
    //是否显示左边的图标 true 显示
    private boolean mShowImgLeft = true;
    //是否显示右边的第一个图标 true 显示
    private boolean mShowImgRightF = false;
    //是否显示右边的第二个图标 true 显示
    private boolean mShowImgRightS = false;
    /**
     * 控制密码是否可见，默认不可见
     */
    private boolean isSee = false;
    public CustomEditText(Context context) {
        this(context, null);
    }
    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomEditText, 0, 0);
        mLeftImgRes = typeArray.getResourceId(R.styleable.CustomEditText_mLeftImgRes, R.drawable.fr_phone);
        mRightImgResFirst = typeArray.getResourceId(R.styleable.CustomEditText_mRightImgResFirst, R.drawable.fr_close);
        mRightImgResSecond = typeArray.getResourceId(R.styleable.CustomEditText_mRightImgResSecond, R.drawable.fr_eye_close);
        mTextColor = typeArray.getColor(R.styleable.CustomEditText_mTextColor, DEFAULT_TEXT_COLOR);
        mDivideLineColor = typeArray.getColor(R.styleable.CustomEditText_mDivideLineColor, DEFAULT_HINT_COLOR);
        mHintColor = typeArray.getColor(R.styleable.CustomEditText_mHintColor, DEFAULT_HINT_COLOR);
        mTextSize = typeArray.getDimension(R.styleable.CustomEditText_mTextSize, DEFAULT_TEXT_SIZE);
        mBackgroundColor = typeArray.getColor(R.styleable.CustomEditText_mBackgroundColor, DEFAULT_BACKGROUND_COLOR);
        mPaddingStart = typeArray.getDimension(R.styleable.CustomEditText_mPaddingStart, DEFAULT_PADDING);
        mPaddingEnd = typeArray.getDimension(R.styleable.CustomEditText_mPaddingEnd, DEFAULT_PADDING);
        mPaddingBottom = typeArray.getDimension(R.styleable.CustomEditText_mPaddingBottom, DEFAULT_PADDING);
        mShowDivideLine = typeArray.getBoolean(R.styleable.CustomEditText_mShowDivideLine, mShowDivideLine);
        mShowImgLeft = typeArray.getBoolean(R.styleable.CustomEditText_mShowImgLeft, mShowImgLeft);
        mShowImgRightF = typeArray.getBoolean(R.styleable.CustomEditText_mShowImgRightF, mShowImgRightF);
        mShowImgRightS = typeArray.getBoolean(R.styleable.CustomEditText_mShowImgRightS, mShowImgRightS);
        initView(context);
    }

    private void initView(Context context) {
        //初始化视图
        mRootView = View.inflate(context, R.layout.fr_custom_edit_text, this);
        findView(mRootView);
        //设置背景颜色
        setBackgroundColor(mBackgroundColor);
        //设置控件的显示、隐藏
        customEtIvLeft.setVisibility(mShowImgLeft ? VISIBLE : GONE);
        customEtIvRightF.setVisibility(mShowImgRightF ? VISIBLE : GONE);
        customEtIvRightS.setVisibility(mShowImgRightS ? VISIBLE : GONE);
        customEtVBottom.setVisibility(mShowDivideLine ? VISIBLE : GONE);
        //设置相对边距
        customEtLayoutParams = (LayoutParams) customEt.getLayoutParams();
        customEtLayoutParams.leftMargin = (int) mPaddingStart;
        customEtLayoutParams.rightMargin = (int) mPaddingEnd;
        customLlLayoutParams = (LayoutParams) customEtLl.getLayoutParams();
        customLlLayoutParams.bottomMargin = (int) mPaddingBottom;
        //设置输入框的文字颜色，字体大小等属性
        customEt.setTextColor(mTextColor);
        customEt.setHintTextColor(mHintColor);
        customEt.setTextSize(mTextSize);
        customEtVBottom.setBackgroundColor(mDivideLineColor);
        //设置左右图片
        customEtIvLeft.setImageResource(mLeftImgRes);
        customEtIvRightF.setImageResource(mRightImgResFirst);
        customEtIvRightS.setImageResource(mRightImgResSecond);
    }

    private void findView(@NonNull View mRootView) {
        customEtIvLeft = mRootView.findViewById(R.id.fr_custom_et_iv_left);
        customEt = mRootView.findViewById(R.id.fr_custom_et);
        customEtIvRightF = mRootView.findViewById(R.id.fr_custom_et_iv_right_f);
        customEtIvRightS = mRootView.findViewById(R.id.fr_custom_et_iv_right_s);
        customEtLl = mRootView.findViewById(R.id.fr_custom_et_ll);
        customEtVBottom = mRootView.findViewById(R.id.fr_custom_et_v_bottom);

    }

    public void setMDivideLineColor(int mDivideLineColor) {
        this.mDivideLineColor = mDivideLineColor;
        customEtVBottom.setBackgroundColor(mDivideLineColor);
    }

    public void setMLeftImgRes(@DrawableRes int mLeftImgRes) {
        this.mLeftImgRes = mLeftImgRes;
        customEtIvLeft.setImageResource(mLeftImgRes);
    }

    public void setMRightImgResFirst(@DrawableRes int mRightImgResFirst) {
        this.mRightImgResFirst = mRightImgResFirst;
        customEtIvRightF.setImageResource(mRightImgResFirst);
    }

    public void setMRightImgResSecond(@DrawableRes int mRightImgResSecond) {
        this.mRightImgResSecond = mRightImgResSecond;
        customEtIvRightS.setImageResource(mRightImgResSecond);
    }

    public void setMTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        customEt.setTextColor(mTextColor);
    }

    public void setMHintColor(int mHintColor) {
        this.mHintColor = mHintColor;
        customEt.setHintTextColor(mHintColor);
    }

    public void setMTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        customEt.setTextSize(mTextSize);
    }

    public void setMBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        setBackgroundColor(mBackgroundColor);
    }

    public void setMPaddingStart(float mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
        if (customEtLayoutParams != null) {
            customEtLayoutParams.leftMargin = (int) mPaddingStart;
        }
    }

    public void setMPaddingEnd(float mPaddingEnd) {
        this.mPaddingEnd = mPaddingEnd;
        if (customEtLayoutParams != null) {
            customEtLayoutParams.rightMargin = (int) mPaddingEnd;
        }
    }

    public void setMPaddingBottom(float mPaddingBottom) {
        this.mPaddingBottom = mPaddingBottom;
        if (customLlLayoutParams != null) {
            customLlLayoutParams.bottomMargin = (int) mPaddingBottom;
        }
    }

    public void setMShowDivideLine(boolean mShowDivideLine) {
        this.mShowDivideLine = mShowDivideLine;
        customEtVBottom.setVisibility(mShowDivideLine ? VISIBLE : GONE);
    }

    public void setMShowImgLeft(boolean mShowImgLeft) {
        this.mShowImgLeft = mShowImgLeft;
        customEtIvLeft.setVisibility(mShowImgLeft ? VISIBLE : GONE);
    }

    public void setMShowImgRightF(boolean mShowImgRightF) {
        this.mShowImgRightF = mShowImgRightF;
        customEtIvRightF.setVisibility(mShowImgRightF ? VISIBLE : GONE);
    }

    public void setMShowImgRightS(boolean mShowImgRightS) {
        this.mShowImgRightS = mShowImgRightS;
        customEtIvRightS.setVisibility(mShowImgRightS ? VISIBLE : GONE);
    }

    public View getMRootView() {
        return mRootView;
    }

    public EditText getCustomEt() {
        return customEt;
    }

    public String getContentText() {
        return customEt.getText().toString();
    }

    public ImageView getCustomEtIvLeft() {
        return customEtIvLeft;
    }

    public ImageView getCustomEtIvRightF() {
        return customEtIvRightF;
    }

    public ImageView getCustomEtIvRightS() {
        return customEtIvRightS;
    }

    /**
     * 清除文本框里的内容
     *
     * @param clearBtn 由外部指定的控制控件
     */
    public void clearContent(final View clearBtn) {
        String temp = customEt.getText().toString();
        clearBtn.setVisibility(!TextUtils.isEmpty(temp) ? VISIBLE : GONE);
        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                customEt.setText("");
            }
        });
        customEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearBtn.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void visualTransform(final ImageView transFormBtn) {
        transFormBtn.setVisibility(VISIBLE);
        transFormBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSee) {
                    //能看见,变成看不见
                    customEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //图标切换
                    transFormBtn.setImageResource(R.drawable.fr_eye_open);
                    customEt.setSelection(customEt.getText().length());
                } else {
                    //看不见变成能看见
                    customEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //图标切换
                    transFormBtn.setImageResource(R.drawable.fr_eye_close);
                    customEt.setSelection(customEt.getText().length());
                }
                isSee = !isSee;
            }
        });
    }

    /**
     * 监听键盘点击回车后，执行某些操作
     *
     * @param listener 监听器
     */
    public void doAfterActionDone(final ActionDoneListener listener) {
        customEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        customEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (listener != null) {
                        listener.doSthActionDone(v, event);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 传递回车点击事件
     */
    public interface ActionDoneListener {
        void doSthActionDone(TextView v, KeyEvent event);
    }
}
