/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lucasr.twowayview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TWGridView extends TWLanedView {
    private static final String LOGTAG = "TWGridView";

    private static final int NUM_COLS = 2;
    private static final int NUM_ROWS = 2;

    int mNumColumns;
    int mNumRows;

    public TWGridView(Context context) {
        this(context, null);
    }

    public TWGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TWGridView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, NUM_COLS, NUM_ROWS);
    }

    protected TWGridView(Context context, AttributeSet attrs, int defStyle,
                         int defaultNumColumns, int defaultNumRows) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TWGridView, defStyle, 0);
        mNumColumns = Math.max(defaultNumColumns, a.getInt(R.styleable.TWGridView_numColumns, -1));
        mNumRows = Math.max(defaultNumRows, a.getInt(R.styleable.TWGridView_numRows, -1));
        a.recycle();
    }

    private int getRectDimension(Rect r) {
        if (mIsVertical) {
            return r.bottom - r.top;
        } else {
            return r.right - r.left;
        }
    }

    @Override
    protected int getLaneCount() {
        return (mIsVertical ? mNumColumns : mNumRows);
    }

    @Override
    protected int getLaneForPosition(int position, Flow flow) {
        return (position % getLaneCount());
    }

    @Override
    protected void attachChildToLayout(View child, int position, Flow flow, Rect childFrame) {
        super.attachChildToLayout(child, position, flow, childFrame);

        final int lane = getLaneForPosition(position, flow);
        final int dimension = getRectDimension(childFrame);
        for (int i = 0; i < lane; i++) {
            mLanes.getLane(i, mTempRect);
            if (getRectDimension(mTempRect) == 0) {
                mLanes.offset(i, dimension);
            }
        }
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    public void setNumColumns(int numColumns) {
        if (mNumColumns == numColumns) {
            return;
        }

        mNumColumns = numColumns;
        if (mIsVertical) {
            forceCreateLanes();
        }
    }

    public int getNumRows() {
        return mNumRows;
    }

    public void setNumRows(int numRows) {
        if (mNumRows == numRows) {
            return;
        }

        mNumRows = numRows;
        if (!mIsVertical) {
            forceCreateLanes();
        }
    }
}