package com.hytera.fcls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cctv on 2017/3/10.
 */

public class MaqueTextView extends TextView {

    public MaqueTextView(Context context) {
        super(context);
    }

    public MaqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaqueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MaqueTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
@Override
public boolean isFocused() {
    return true;
}
}
