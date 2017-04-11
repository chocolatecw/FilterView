package com.choc.library.filterviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choc.library.R;
import com.choc.library.filterviewutil.FindView;
import com.choc.library.filterviewutil.UnitConvertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */
public class FilterView extends FrameLayout {

    private static final int elementDefaultPadding = 5;
    private static final int fixedMode = 0;
    private static final int wrapMode = 1;
    private static final int fixedModeDefaultWidth = 100;
    private static final int fixedModeDefaultHeight = 30;

    private String[] entries;
    private int entriesID;
    private LinearLayout filterCon;
    private CheckBox[] childElements;
    private String title;
    private TextView titleTxt;
    private int elementPadding;
    private boolean isSingle;
    private int mode;
    private int eleWidth;
    private int eleHeight;
    private boolean checkFirst;

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
            mode = a.getInteger(R.styleable.FilterView_mode, fixedMode);
            eleWidth = a.getDimensionPixelOffset(R.styleable.FilterView_eleWidth,
                    UnitConvertUtil.Dp2Px(getContext(), fixedModeDefaultWidth));
            eleHeight = a.getDimensionPixelOffset(R.styleable.FilterView_eleHeight,
                    UnitConvertUtil.Dp2Px(getContext(), fixedModeDefaultHeight));
            checkFirst = a.getBoolean(R.styleable.FilterView_checkFirst, true);
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

            if(mode == fixedMode) { //固定模式
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(eleWidth, eleHeight);
                childElements[i].setLayoutParams(layoutParams);
                childElements[i].setPadding(0, 0, 0, 0);
            }else if(mode == wrapMode) { //wrap模式
                //计算长宽比例
                int ratio = 6 - elementStr[i].length()/2;
                ratio = Math.max(ratio, 1);
                childElements[i].setPadding(elementPadding*ratio, elementPadding, elementPadding*ratio, elementPadding);
            }

            filterCon.addView(childElements[i]);
        }

        if(checkFirst && childElements.length > 0) {
            setCheckedItem(0);
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

    public void setCheckedItem(int position) {
        if(position >= 0 && position < childElements.length) {
            childElements[position].setChecked(true);
        }
    }

    public void setCheckedItems(int[] positions) {
        if(!isSingle) {
            for (int pt:positions) {
                setCheckedItem(pt);
            }
        }
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
