package com.terabyte.clock001;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class DelayDismissButton extends ConstraintLayout {
    private ImageView foreground;
    private TextView textDelay, textDismiss;
    private float initX, rightBorderX = -1, leftBorderX = -1;
    private float rightActiveBorder = -1, leftActiveBorder = -1;

    private DelayDismissListener listener;

    public DelayDismissButton(@NonNull Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public DelayDismissButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public DelayDismissButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    public DelayDismissButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ConstraintLayout background = new ConstraintLayout(context);
        LayoutParams backgroundLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        backgroundLayoutParams.topToTop = LayoutParams.PARENT_ID;
        backgroundLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
        background.setBackground(ContextCompat.getDrawable(context, R.drawable.delay_dismiss_button_background));
        background.setPadding(20,20,20,20);
        addView(background, backgroundLayoutParams);

        //add two textView fields "delay" and "dismiss"
        textDelay = new TextView(context);
        LayoutParams textDelayParams= new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textDelayParams.leftToLeft = LayoutParams.PARENT_ID;
        textDelayParams.topToTop = LayoutParams.PARENT_ID;
        textDelayParams.bottomToBottom = LayoutParams.PARENT_ID;
        textDelayParams.leftMargin = 20;
        textDelay.setText("delay");
        textDelay.setTextColor(Color.WHITE);
        textDelay.setTextSize(18);
        background.addView(textDelay, textDelayParams);

        textDismiss = new TextView(context);
        LayoutParams textDismissParams= new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textDismissParams.rightToRight = LayoutParams.PARENT_ID;
        textDismissParams.topToTop = LayoutParams.PARENT_ID;
        textDismissParams.bottomToBottom = LayoutParams.PARENT_ID;
        textDismissParams.rightMargin = 20;
        textDismiss.setText("dismiss");
        textDismiss.setTextColor(Color.WHITE);
        textDismiss.setTextSize(18);
        background.addView(textDismiss, textDismissParams);

        //here we add moving part of our button
        foreground = new ImageView(context);
        LayoutParams foregroundParams = new LayoutParams(200, 200);
        foregroundParams.leftToLeft = LayoutParams.PARENT_ID;
        foregroundParams.rightToRight = LayoutParams.PARENT_ID;
        foregroundParams.topToTop = LayoutParams.PARENT_ID;
        foregroundParams.bottomToBottom = LayoutParams.PARENT_ID;
        foreground.setBackground(ContextCompat.getDrawable(context, R.drawable.delay_dismiss_button_foreground));
        foreground.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delay_disiss_button_foreground));
        foreground.setPadding(50,50,50,50);
        addView(foreground, foregroundParams);



        //finally set onTouchListener
        setOnTouchListener(getMyTouchListener());
    }

    private OnTouchListener getMyTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(leftBorderX ==-1 || rightBorderX==-1) {
                            leftBorderX = foreground.getWidth()/2;
                            rightBorderX = getWidth()-foreground.getWidth()/2;
                            rightActiveBorder = getWidth()-foreground.getWidth()*2;
                            leftActiveBorder = foreground.getWidth()*2;
                        }
                        initX = foreground.getX();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(event.getX()>leftBorderX && event.getX()<rightBorderX) {
                            foreground.setX(event.getX()-foreground.getWidth()/2);
                        }
                        if(event.getX()<=leftBorderX) {
                            foreground.setX(leftBorderX-foreground.getWidth()/2);
                        }
                        if(event.getX()>=rightBorderX) {
                            foreground.setX(rightBorderX-foreground.getWidth()/2);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(foreground.getX()<rightActiveBorder && foreground.getX()>leftActiveBorder) {
                            foreground.setX(initX);
                        }
                        if(foreground.getX()<=leftActiveBorder) {
                            //onSwipeDelay
                            listener.onSwipeDelay();
                        }
                        if(foreground.getX()>=rightActiveBorder) {
                            //onClick dismiss
                            listener.onSwipeDismiss();
                        }
                        return true;
                }
                return false;
            }
        };
    }

    public void setDelayDismissListener(DelayDismissListener listener) {
        this.listener = listener;
    }
}
