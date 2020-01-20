package com.levanb.movies_app.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShadowMarginItemDecoration extends RecyclerView.ItemDecoration {
    private final int margin;

    public ShadowMarginItemDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top = margin;
        outRect.left = margin;
        outRect.right = margin;
        outRect.bottom = margin;
    }
}
