package com.fleetlabs.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fleetlabs.library.R;
import com.fleetlabs.library.utils.StringUtil;

/**
 * Created by alvinzeng on 2/23/16.
 */
public class CountDownTextView extends TextView {

    private int millisInFuture = 60*1000; //default 60

    private String waitingTitle;
    private String restartTitle;
    private String defaultTitle;
    private boolean autoStart;

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, android.R.attr.textViewStyle);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownTextView, defStyle, 0);
        int n = ta.getIndexCount();
        for(int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);

            if (attr == R.styleable.CountDownTextView_restartTitle) {
                restartTitle = ta.getString(attr);
            } else if(attr == R.styleable.CountDownTextView_waitingTitle) {
                waitingTitle = ta.getString(attr);
            } else if(attr == R.styleable.CountDownTextView_autoStart) {
                autoStart = ta.getBoolean(attr, false);
            } else if(attr == R.styleable.CountDownTextView_millisInFuture) {
                millisInFuture = ta.getInteger(attr, 60 * 1000);
            }
        }
        ta.recycle();

        defaultTitle = getText().toString();
        if(StringUtil.isEmpty(restartTitle)) {
            restartTitle = defaultTitle;
        }

        if(autoStart) {
            this.start();
        }
    }

    public void setMillisInFuture(int millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public void start() {
        this.setEnabled(false);
        timer.start();
    }

    public void restart() {
        timer.cancel();
        this.start();
    }

    public void stop() {
        timer.cancel();
        setEnabled(true);
        setText(defaultTitle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(timer != null) {
            timer.cancel();
        }
    }

    private CountDownTimer timer = new CountDownTimer(millisInFuture, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            String text = String.format(waitingTitle, (millisUntilFinished / 1000));
            CountDownTextView.this.setText(text);
        }

        @Override
        public void onFinish() {
            CountDownTextView.this.setEnabled(true);
            CountDownTextView.this.setText(restartTitle);
        }
    };
}
