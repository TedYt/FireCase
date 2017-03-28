package com.hytera.fcls.comutil;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

/**
 * 视图设计工具类<br>
 *
 */
public class LayoutUtils {

	// 手机版
	private static final int LAYOUT_WIDTH_PORT_FORPHONE = 1080;
	private static int LAYOUT_WIDTH_PORT = LAYOUT_WIDTH_PORT_FORPHONE;// 竖屏设计稿宽度

	/** 纯辅助方法 */
	public static int tranSize(Context context, int px) {
		int tranSize = px;
			tranSize = (int) (px * ((float) getWindowWidth(context) / LAYOUT_WIDTH_PORT));// 竖屏
		if (tranSize <= 0) {
			tranSize = 1;
		}
		return tranSize;
	}

	/**
	 * 自动适配。前提如下：<br>
	 * 0. 每个布局只能使用一次，再次调用此方法会再次调整(放大再放大) 1. 基础宽高: ViewGroup的子类<br>
	 * 2. 文字: TextView的子类（Button&EditText都是）<br>
	 * 3. Margin: NONE<br>
	 * 4. Padding: NONE<br>
	 * 
	 * @param container
	 *            需要适配的 顶部容器
	 */
	public static void doResize(Context context, ViewGroup container) {
		Object[] args;
		Float scale = 1.0f;
		// 比较从XML抓取的值是否发生偏移,主要是冲掉其它适配方式.tag_layout_compare_view|100.0f
		View compareView = container.findViewWithTag("tag_layout_compare_view");
		if (compareView != null) {
			LayoutParams lp = compareView.getLayoutParams();
			if (lp != null && lp.width > 0) {
				args = new Object[] { scale };
				scale = 100.0f / lp.width;
			}
		}
		args = new Object[] { scale };
		doResizeWithArgs(context, container, args);
	}

	private static void doResizeWithArgs(Context context, ViewGroup container, Object[] args) {
		resize2nd(context, container, args);
		for (int i = 0; i < container.getChildCount(); i++) {
			View view = container.getChildAt(i);
			if (view instanceof ViewGroup)
				doResizeWithArgs(context, (ViewGroup) view, args);
			else
				resize2nd(context, view, args);
		}
	}

	/**
	 * 获取视图真实显示大小
	 * 
	 * @param context
	 *            上下文
	 * @param designPx
	 *            设计稿上的尺寸
	 * @return
	 */
	private static int getResizePx(Context context, int designPx, Object... args) {
		int size = tranSize(context, designPx);
		if (args != null && args.length > 0) {
			size *= (Float) args[0];
		}
		if (size <= 0) {
			size = 1;
		}
		return size;
	}

	/** 纯辅助方法 */
	private static void resize2nd(Context context, View view, Object... args) {
		// 基础宽高
		LayoutParams lp = view.getLayoutParams();
		if (lp != null) {
			if (lp.width > 0)
				lp.width = getResizePx(context, lp.width, args);
			if (lp.height > 0)
				lp.height = getResizePx(context, lp.height, args);
			// 文字
			if (view instanceof TextView) {
				TextView txtview = (TextView) view;
				txtview.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						LayoutUtils.getResizePx(context, (int) txtview.getTextSize(), args));
			}
			// MARGIN
			if (lp instanceof MarginLayoutParams) {
				MarginLayoutParams mlp = (MarginLayoutParams) lp;
				if (mlp.leftMargin > 0)
					mlp.leftMargin = getResizePx(context, mlp.leftMargin, args);
				if (mlp.rightMargin > 0)
					mlp.rightMargin = getResizePx(context, mlp.rightMargin, args);
				if (mlp.topMargin > 0)
					mlp.topMargin = getResizePx(context, mlp.topMargin, args);
				if (mlp.bottomMargin > 0)
					mlp.bottomMargin = getResizePx(context, mlp.bottomMargin, args);
			}
		}
		// PADDING
		int left, right, top, bottom;
		left = view.getPaddingLeft();
		right = view.getPaddingRight();
		top = view.getPaddingTop();
		bottom = view.getPaddingBottom();
		if (left + right + top + bottom > 0) {
			if (left > 0)
				left = getResizePx(context, left, args);
			if (right > 0)
				right = getResizePx(context, right, args);
			if (top > 0)
				top = getResizePx(context, top, args);
			if (bottom > 0)
				bottom = getResizePx(context, bottom, args);
			view.setPadding(left, top, right, bottom);
		}
	}

	public static int getWindowWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getWindowHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
}
