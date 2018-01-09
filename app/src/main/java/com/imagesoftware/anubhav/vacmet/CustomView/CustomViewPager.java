package com.imagesoftware.anubhav.vacmet.CustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.imagesoftware.anubhav.vacmet.adapters.ItemDetailsViewPagerAdapter;

/**
 * Created by anubhav on 1/10/17.
 */

public class CustomViewPager extends ViewPager {

    private ItemDetailsViewPagerAdapter itemDetailsViewPagerAdapter;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setItemDetailsViewPagerAdapter(ItemDetailsViewPagerAdapter itemDetailsViewPagerAdapter) {
        this.itemDetailsViewPagerAdapter = itemDetailsViewPagerAdapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if(itemDetailsViewPagerAdapter!=null) {
            View view = itemDetailsViewPagerAdapter.getmCurrentView();
            if (view != null) {
                view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = view.getMeasuredHeight()+20+view.getPaddingTop()+view.getPaddingBottom();
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
