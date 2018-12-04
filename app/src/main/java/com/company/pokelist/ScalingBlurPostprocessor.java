package com.company.pokelist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.nativecode.NativeBlurFilter;
import com.facebook.imagepipeline.request.BasePostprocessor;

public class ScalingBlurPostprocessor extends BasePostprocessor {

    private final Paint mPaint = new Paint();
    private final int mIterations;
    private final int mBlurRadius;
    private final int mScaleRatio;

    public ScalingBlurPostprocessor(int iterations, int blurRadius, int scaleRatio) {
        Preconditions.checkArgument(scaleRatio > 0);
        mIterations = iterations;
        mBlurRadius = blurRadius;
        mScaleRatio = scaleRatio;
    }

    @Override
    public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
        int width = sourceBitmap.getWidth() / mScaleRatio;
        int height = sourceBitmap.getHeight() / mScaleRatio;
        final CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(width, height);

        try {
            final Bitmap destBitmap = bitmapRef.get();
            final Canvas canvas = new Canvas(destBitmap);

            int left = -destBitmap.getWidth();
            int right = destBitmap.getWidth()*2;
            int top = -destBitmap.getHeight();
            int bottom = destBitmap.getHeight()*2;

            canvas.drawBitmap(sourceBitmap,null, new Rect(left, top, right, bottom), mPaint);
            NativeBlurFilter.iterativeBoxBlur(destBitmap, mIterations, Math.max(1, mBlurRadius / mScaleRatio));
            return CloseableReference.cloneOrNull(bitmapRef);
        } finally {
            CloseableReference.closeSafely(bitmapRef);
        }
    }
}