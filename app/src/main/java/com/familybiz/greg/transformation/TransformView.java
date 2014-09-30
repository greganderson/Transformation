package com.familybiz.greg.transformation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class TransformView extends ViewGroup {

	ImageView mTransformedView = null;

	ArrayList<PointF> mPoints = new ArrayList<PointF>();

	public TransformView(Context context) {
		super(context);
		setBackgroundColor(Color.BLUE);

		mTransformedView = new ImageView(context);
		mTransformedView.setImageResource(R.drawable.image);
		mTransformedView.setBackgroundColor(Color.GREEN);
		mTransformedView.setLayoutParams(new LayoutParams(100, 100));
		addView(mTransformedView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
		for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
			View child = getChildAt(childIndex);
			child.layout(
					getWidth() / 2 - child.getMeasuredWidth() / 2,
					getHeight() / 2 - child.getMeasuredHeight() / 2,
					getWidth() / 2 + child.getMeasuredWidth() / 2,
					getHeight() / 2 + child.getMeasuredHeight() / 2
			);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		mTransformedView.setX(x - mTransformedView.getWidth() / 2);
		mTransformedView.setY(y - mTransformedView.getHeight() / 2);
		//mTransformedView.setScaleX(x / getWidth() * 4.0f);
		//mTransformedView.setScaleY(y / getWidth() * 4.0f);

		if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
			mPoints.clear();

		mPoints.add(new PointF(x, y));

		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			float[] pointsX = new float[mPoints.size()];
			float[] pointsY = new float[mPoints.size()];
			for (int pointIndex = 0; pointIndex < mPoints.size(); pointIndex++) {
				PointF point = mPoints.get(pointIndex);
				pointsX[pointIndex] = point.x - mTransformedView.getWidth() / 2;
				pointsY[pointIndex] = point.y - mTransformedView.getHeight() / 2;
			}
			ObjectAnimator animator = new ObjectAnimator();
			animator.setTarget(mTransformedView);
			//animator.setPropertyName("x");
			//animator.setFloatValues(mTransformedView.getX(), mTransformedView.getY(), 400.0f);
			animator.setDuration(4000);
			animator.setValues(
					PropertyValuesHolder.ofFloat("x", pointsX),
					PropertyValuesHolder.ofFloat("y", pointsY));
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					invalidate();
				}
			});
			animator.start();
		}

		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.YELLOW);
		textPaint.setTextSize(28.0f);
		textPaint.setFakeBoldText(true);
		canvas.drawText("left: " + mTransformedView.getLeft(), 20, 30, textPaint);
		canvas.drawText("top: " + mTransformedView.getTop(), 20, 60, textPaint);
		canvas.drawText("right: " + mTransformedView.getRight(), 20, 90, textPaint);
		canvas.drawText("bottom: " + mTransformedView.getBottom(), 20, 120, textPaint);
		canvas.drawText("x: " + mTransformedView.getX(), 20, 150, textPaint);
		canvas.drawText("y: " + mTransformedView.getY(), 20, 180, textPaint);
		canvas.drawText("translationX: " + mTransformedView.getTranslationX(), 20, 210, textPaint);
		canvas.drawText("translationY: " + mTransformedView.getTranslationY(), 20, 240, textPaint);
		canvas.drawText("rotation: " + mTransformedView.getRotation(), 20, 270, textPaint);
		canvas.drawText("scaleX: " + mTransformedView.getScaleX(), 20, 300, textPaint);
		canvas.drawText("scaleY: " + mTransformedView.getScaleY(), 20, 330, textPaint);

		Paint layoutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		layoutPaint.setColor(Color.CYAN);
		layoutPaint.setStrokeWidth(2.0f);
		layoutPaint.setStyle(Paint.Style.STROKE);
		layoutPaint.setPathEffect(new DashPathEffect(new float[] {4.0f, 8.0f, 10.0f, 12.0f}, 0));
		Path layoutPath = new Path();
		layoutPath.moveTo(mTransformedView.getLeft(), mTransformedView.getTop());
		layoutPath.lineTo(mTransformedView.getRight(), mTransformedView.getTop());
		layoutPath.lineTo(mTransformedView.getRight(), mTransformedView.getBottom());
		layoutPath.lineTo(mTransformedView.getLeft(), mTransformedView.getBottom());
		layoutPath.close();
		canvas.drawPath(layoutPath, layoutPaint);

		if (mPoints.size() > 0) {
			Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			polylinePaint.setStyle(Paint.Style.STROKE);
			polylinePaint.setStrokeWidth(2.0f);
			polylinePaint.setColor(Color.RED);
			Path polylinePath = new Path();
			polylinePath.moveTo(mPoints.get(0).x, mPoints.get(0).y);
			for (PointF point : mPoints)
				polylinePath.lineTo(point.x, point.y);
			canvas.drawPath(polylinePath, polylinePaint);
		}
	}
}
