package com.zwt.photoselect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zwt.photoselect.R;
import com.zwt.photoselect.utils.ScreenUtils;

/**
 * Created by zhangweitao on 2018/3/9.
 * 列表分割线
 */

public class ListItemDivider extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    public ListItemDivider(Context context) {
        this.mDrawable = context.getResources().getDrawable(R.drawable.photo_item_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int left = ScreenUtils.dip2px(parent.getContext(), 16);
        final int right = parent.getWidth() - left;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicWidth());
    }
}
