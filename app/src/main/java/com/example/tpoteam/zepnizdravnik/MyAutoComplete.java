package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Luka Loboda on 09-Jan-17.
 */

public class MyAutoComplete extends AutoCompleteTextView {

    public MyAutoComplete(Context context) {
        super(context);
    }

    public MyAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MyAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
        }
    }

}
