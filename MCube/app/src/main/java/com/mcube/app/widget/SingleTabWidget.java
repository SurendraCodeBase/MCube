package com.mcube.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mcube.app.R;

public class SingleTabWidget extends TabWidget {

	private static final String TAG = "SingleTabWidget";
	private OnTabChangedListener mOnTabChangedListener;

	private int mLayoutId;
	private int mSelectedTab;
	private Context context;

	public int getmSelectedTab() {
		return mSelectedTab;
	}

	public void setmSelectedTab(int mSelectedTab) {
		this.mSelectedTab = mSelectedTab;
	}

	public static interface OnTabChangedListener {
		public void onTabChanged(int tabIndex);
	}

	public SingleTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		setStripEnabled(false);
		setDividerDrawable(null);
	}

	public void setLayout(int layoutResId) {
		mLayoutId = layoutResId;
	}

	public void addTab(int imageResId) {
		addTab(imageResId, null);
	}

	public void addTab(String title) {
		addTab(0, title);
	}

	public void addTab(int imageResId, String title) {
		final View view = LayoutInflater.from(getContext()).inflate(mLayoutId, this,
				false);
		if (view == null) {
			throw new RuntimeException(
					"You must call 'setLayout(int layoutResId)' to initialize the tab.");
		} else {
			LayoutParams layoutParams = (LayoutParams) view
					.getLayoutParams();
			layoutParams.width = 0;
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			layoutParams.weight = 1.0f;
			view.setLayoutParams(layoutParams);
		}

		if (view instanceof TextView) {
			if (imageResId > 0) {
				((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,
						imageResId, 0, 0);
			}
			if (!TextUtils.isEmpty(title)) {
				((TextView) view).setText(title);
			}
		} else if (view instanceof ImageView) {
			if (imageResId > 0) {
				((ImageView) view).setImageResource(imageResId);
			}
		} else {
			TextView textView = (TextView) view
					.findViewById(R.id.title);
			if (textView == null) {
				throw new RuntimeException(
						"Your layout must have a TextView whose id attribute is 'android.R.id.title'");
			} else {
				textView.setText(title);
			}
			ImageView imageView = (ImageView) view
					.findViewById(R.id.icon);
			if (imageView == null) {
				throw new RuntimeException(
						"Your layout must have a ImageView whose id attribute is 'android.R.id.icon'");
			} else {
				imageView.setImageResource(imageResId);
			}
		}

		addView(view);
		view.setOnClickListener(new TabClickListener(getTabCount() - 1));
		view.setOnFocusChangeListener(this);

		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				//now we can retrieve the width and height
				int width = view.getWidth();

				Resources resources = context.getResources();
				DisplayMetrics metrics = resources.getDisplayMetrics();
				float dp = width / (metrics.densityDpi / 160f);
				Log.d(TAG,"screen density="+metrics.densityDpi);
				Log.d(TAG,"width in pixels="+width+ " width in dp="+dp);

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				else
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	public void setOnTabChangedListener(OnTabChangedListener listener) {
		mOnTabChangedListener = listener;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
	}

	private class TabClickListener implements OnClickListener {
		private final int mIndex;

		public TabClickListener(int index) {
			//Log.d(TAG,"tab clicked:"+index);
			mIndex = index;
		}

		@Override
		public void onClick(View view) {
			if (mOnTabChangedListener != null && mIndex != mSelectedTab) {
				Log.d(TAG,"tab clicked:"+mIndex);
				mSelectedTab = mIndex;
				setCurrentTab(mIndex);
				mOnTabChangedListener.onTabChanged(mIndex);
			}
		}
	}
}
