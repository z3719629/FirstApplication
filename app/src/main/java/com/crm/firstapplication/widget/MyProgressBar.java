package com.crm.firstapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2017/12/28.
 */
public class MyProgressBar extends ProgressBar {

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTrack(canvas);
    }

    /**
     * Draws the progress bar track.
     */
    void drawTrack(Canvas canvas) {
//        final Drawable d = mCurrentDrawable;
//        if (d != null) {
//            // Translate canvas so a indeterminate circular progress bar with padding
//            // rotates properly in its animation
//            final int saveCount = canvas.save();
//
//            if (isLayoutRtl() && mMirrorForRtl) {
//                canvas.translate(getWidth() - mPaddingRight, mPaddingTop);
//                canvas.scale(-1.0f, 1.0f);
//            } else {
//                canvas.translate(mPaddingLeft, mPaddingTop);
//            }
//
//            final long time = getDrawingTime();
//            if (mHasAnimation) {
//                mAnimation.getTransformation(time, mTransformation);
//                final float scale = mTransformation.getAlpha();
//                try {
//                    mInDrawing = true;
//                    d.setLevel((int) (scale * MAX_LEVEL));
//                } finally {
//                    mInDrawing = false;
//                }
//                postInvalidateOnAnimation();
//            }
//
//            d.draw(canvas);
//            canvas.restoreToCount(saveCount);
//
//            if (mShouldStartAnimationDrawable && d instanceof Animatable) {
//                ((Animatable) d).start();
//                mShouldStartAnimationDrawable = false;
//            }
//        }
    }

}
