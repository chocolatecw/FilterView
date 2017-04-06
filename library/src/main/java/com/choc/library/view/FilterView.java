package com.choc.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choc.library.R;
import com.choc.library.util.FindView;
import com.choc.library.util.UnitConvertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */
public class FilterView extends FrameLayout {

    private static final int elementDefaultPadding = 5;
    private String[] entries;
    private int entriesID;
    private LinearLayout filterCon;
    private CheckBox[] childElements;
    private String title;
    private TextView titleTxt;
    private int elementPadding;
    private boolean isSingle;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FilterView, 0, 0);
        try {
            entriesID = a.getResourceId(R.styleable.FilterView_entries, -1);
            title = a.getString(R.styleable.FilterView_title);
            elementPadding = a.getDimensionPixelOffset(R.styleable.FilterView_elementPadding,
                    UnitConvertUtil.Dp2Px(context, elementDefaultPadding));
            isSingle = a.getBoolean(R.styleable.FilterView_single, true);
        }finally {
            a.recycle();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.filter_view, this, true);
        init(view);

    }

    private void init(View view) {
        filterCon = FindView.findById(view, R.id.filter_con);
        titleTxt = FindView.findById(view, R.id.title_txt);
        if(entriesID != -1) {
            entries = getContext().getResources().getStringArray(entriesID);
            addFilterElement(entries);
        }
        if(title != null && !title.isEmpty()) {
            titleTxt.setText(title);
        }
    }

    private void addFilterElement(String[] elementStr) {
        childElements = new CheckBox[elementStr.length];
        for (int i = 0; i < elementStr.length; i++) {
            childElements[i] = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.filter_element, null);
            childElements[i].setOnCheckedChangeListener(new CompoundButtonListener(i));
            childElements[i].setText(elementStr[i]);
            childElements[i].setPadding(elementPadding*6, elementPadding, elementPadding*6, elementPadding);
            filterCon.addView(childElements[i]);
        }
    }

    public int getCheckedItem() {
        if(!isSingle) {
            return -1;
        }
        for (int i = 0; i < childElements.length; i++) {
            if(childElements[i].isChecked()) {
                return i;
            }
        }
        return -1;
    }

    public List<Integer> getCheckedItems() {
        List<Integer> checkedItemOrders = new ArrayList<Integer>();
        if(isSingle) {
            return null;
        }
        for (int i = 0; i < childElements.length; i++) {
            if(childElements[i].isChecked()) {
                checkedItemOrders.add(i);
            }
        }
        return checkedItemOrders;
    }

    class CompoundButtonListener implements CompoundButton.OnCheckedChangeListener {

        private int position;

        public CompoundButtonListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked && isSingle) {
                for (int i = 0; i < childElements.length; i++) {
                    if(i != position && childElements[i].isChecked()) {
                        childElements[i].setChecked(false);
                    }
                }
            }
        }
    }


}
