package com.imagesoftware.anubhav.vacmet.adapters;

/**
 * Created by anubhav on 2/10/17.
 */


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.imagesoftware.anubhav.vacmet.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SampleLinkedTableAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {
    private static final int[] COLORS = new int[]{
            0xffe62a10, 0xffe91e63, 0xff9c27b0, 0xff673ab7, 0xff3f51b5,
            0xff5677fc, 0xff03a9f4, 0xff00bcd4, 0xff009688, 0xff259b24,
            0xff8bc34a, 0xffcddc39, 0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722};

    private final LayoutInflater mLayoutInflater;
    private final int mHeaderHeight;
    private final int mHeaderWidth;
    private Context context;
    private List<String> lengthList;
    private List<String> widthList;
    private List<Double> despOrders;
    private List<Double> totalOrderedItemsList;
    private List<String> itemDeliveryDatesList;
    private List<String> stockList;
    private List<String> treatment1List;
    private List<String> treatment2List;
    private List<String> shadesList;
    private List<Double> inProdList;
    private List<String> columnList;
    private int mColumnWidth;
    private int mRowHeight;

    public SampleLinkedTableAdapter(Context context, List<String> lengthList, List<String> widthList, List<Double> list, List<Double> orderedList, List<String> itemDeliveryDatesList, List<String> stockQtyList, List<String> treatment1List, List<String> treatment2List, List<String> shadesList, List<Double> inProdList) {
        this.context = context;
        this.lengthList = lengthList;
        this.widthList = widthList;
        this.despOrders = list;
        this.totalOrderedItemsList = orderedList;
        this.itemDeliveryDatesList = itemDeliveryDatesList;
        this.stockList = stockQtyList;
        this.treatment1List = treatment1List;
        this.treatment2List = treatment2List;
        this.shadesList = shadesList;
        this.inProdList = inProdList;

        mLayoutInflater = LayoutInflater.from(context);
        Resources res = context.getResources();
        mHeaderHeight = res.getDimensionPixelSize(R.dimen.d60);
        mHeaderWidth = res.getDimensionPixelSize(R.dimen.d80);
        mColumnWidth = res.getDimensionPixelSize(R.dimen.d120);
        mRowHeight = res.getDimensionPixelSize(R.dimen.d120);
        columnList = new ArrayList<>(Arrays.asList("Delivery Date(s):", "Length(s):", "Width(s):", "Desp-Qty/Order-Qty", "Stock Qty(s)", "Treatment1", "Treatment2"));
    }

    @Override
    public int getRowCount() {
        if (lengthList != null || widthList != null) {
            if (lengthList != null && widthList != null) {
                if (lengthList.size() >= widthList.size()) {
                    return lengthList.size();
                } else {
                    return widthList.size();
                }
            }
            if (widthList == null) {
                return lengthList.size();
            } else {
                return widthList.size();
            }
        }
        return 1;
    }

    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new TestViewHolder(mLayoutInflater.inflate(R.layout.item_card, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderColumnViewHolder(mLayoutInflater.inflate(R.layout.item_header_column, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderRowViewHolder(mLayoutInflater.inflate(R.layout.item_header_row, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderLeftTopViewHolder(mLayoutInflater.inflate(R.layout.item_header_left_top, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        TestViewHolder vh = (TestViewHolder) viewHolder;
       /* String itemData = mTableDataSource.getItemData(row, column); // skip headers

        if (TextUtils.isEmpty(itemData)) {
            itemData = "";
        }

        itemData = itemData.trim();*/
        vh.tvText.setVisibility(View.VISIBLE);
        vh.tvText.setText("--item--");

    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        TestHeaderColumnViewHolder vh = (TestHeaderColumnViewHolder) viewHolder;

        vh.tvText.setText(columnList.get(column));  // skip left top header
      /*  int color = COLORS[column % COLORS.length];

        GradientDrawable gd = new GradientDrawable(
                mIsRtl ? GradientDrawable.Orientation.RIGHT_LEFT : GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ColorUtils.setAlphaComponent(color, 50), 0x00000000});
        gd.setCornerRadius(0f);
        vh.vGradient.setBackground(gd);
        vh.vLine.setBackgroundColor(color);*/
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        TestHeaderRowViewHolder vh = (TestHeaderRowViewHolder) viewHolder;
        vh.tvText.setText("-first row-");
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        TestHeaderLeftTopViewHolder vh = (TestHeaderLeftTopViewHolder) viewHolder;
        vh.tvText.setText("-check-");
    }

    @Override
    public int getColumnWidth(int column) {
        return mColumnWidth;
    }

    @Override
    public int getHeaderColumnHeight() {
        return mHeaderHeight;
    }

    @Override
    public int getRowHeight(int row) {
        return mRowHeight;
    }

    @Override
    public int getHeaderRowWidth() {
        return mHeaderWidth;
    }

    //------------------------------------- view holders ------------------------------------------

    private static class TestViewHolder extends ViewHolderImpl {
        TextView tvText;


        private TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);

        }
    }

    private static class TestHeaderColumnViewHolder extends ViewHolderImpl {
        TextView tvText;
        View vGradient;
        View vLine;

        private TestHeaderColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            vGradient = itemView.findViewById(R.id.vGradient);
            vLine = itemView.findViewById(R.id.vLine);
        }
    }

    private static class TestHeaderRowViewHolder extends ViewHolderImpl {
        TextView tvText;

        TestHeaderRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderLeftTopViewHolder extends ViewHolderImpl {
        TextView tvText;

        private TestHeaderLeftTopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }
}




