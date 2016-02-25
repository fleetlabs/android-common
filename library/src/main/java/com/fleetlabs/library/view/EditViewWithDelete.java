package com.fleetlabs.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.fleetlabs.library.R;

/**
 * Created by Richard.Lai on 2016/1/26.
 */
public class EditViewWithDelete extends EditText implements View.OnFocusChangeListener,TextWatcher {
    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private Drawable deleteImage;
    private OnFocusListener onFocusListener;

    public EditViewWithDelete(Context context) {
        this(context, null);
    }

    public EditViewWithDelete(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditViewWithDelete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditViewWithDelete, defStyle, 0);
        int n = ta.getIndexCount();
        for(int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);

            if (attr == R.styleable.EditViewWithDelete_deleteImage) {
                deleteImage = ta.getDrawable(attr);
            }
        }
        ta.recycle();

        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = deleteImage == null ? getResources().getDrawable(R.mipmap.delete) : deleteImage;
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight())
                        && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
            if(onFocusListener != null) {
                onFocusListener.onFocus();
            }
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
    public void setOnFocusListtener(OnFocusListener onFocusListener){
        this.onFocusListener = onFocusListener;
    }

    public interface OnFocusListener{
        void onFocus();
    }
}
