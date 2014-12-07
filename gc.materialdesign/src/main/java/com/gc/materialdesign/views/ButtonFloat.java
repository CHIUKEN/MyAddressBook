package com.gc.materialdesign.views;

import com.gc.materialdesign.R;
import com.gc.materialdesign.utils.Utils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author:Jack Tony
 * @tips :甇�虜��敶Ｘ瘚格��殷�暺恕sizeIcon = 24;sizeRadius = 28;
 * @date :2014-11-1
 */
public class ButtonFloat extends Button {

	protected int iconSize;// ��曄�憭批�
	protected int sizeRadius;// �暹���

	ImageView icon; // �銝剔�ImageView 
	Drawable iconDrawable;// imageView銝剔�drawable

	public ButtonFloat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onInitDefaultValues() {
		super.onInitDefaultValues();
		icon = new ImageView(getContext());
		iconSize = 24;
		sizeRadius = 28;
		rippleSpeed = 3;
		rippleSize = 5;
		minWidth = sizeRadius * 2;// 56dp
		minHeight = sizeRadius * 2;// 56dp
		backgroundResId = R.drawable.background_button_float;
	}

	@Override
	protected void onInitAttributes(AttributeSet attrs) {
		super.onInitAttributes(attrs);
		// 霈曄蔭�銝剔��暹�
		int iconResource = attrs.getAttributeResourceValue(MATERIALDESIGNXML,"iconDrawable",-1);
		if (iconResource != -1) {
			iconDrawable = getResources().getDrawable(iconResource);
		}

		// animation
		boolean animate = attrs.getAttributeBooleanValue(MATERIALDESIGNXML, "animate", false);
		if (animate) {
			playAnimation();
		}
		
		if (iconDrawable != null) {
			icon.setBackgroundDrawable(iconDrawable);
		}
		// 霈曄蔭�銝剖��憭批�
		String size = attrs.getAttributeValue(MATERIALDESIGNXML, "iconSize");
		if (size != null) {
			iconSize = (int) Utils.dipOrDpToFloat(size);
		}
		setIconParams();
		addView(icon);
	}

	private void setIconParams() {
		// TODO �芸���瘜���		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				Utils.dpToPx(iconSize, getResources()), Utils.dpToPx(iconSize, getResources()));
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		icon.setLayoutParams(params);
	}
	
	/**
	 * float���
	 */
	private void playAnimation() {
		post(new Runnable() {
			@Override
			public void run() {
				float originalY = ViewHelper.getY(ButtonFloat.this) - Utils.dpToPx(24, getResources());
				ViewHelper.setY(ButtonFloat.this, 
						ViewHelper.getY(ButtonFloat.this) + getHeight() * 3);
				ObjectAnimator animator = ObjectAnimator.ofFloat(ButtonFloat.this, "y", originalY);
				animator.setInterpolator(new BounceInterpolator());
				animator.setDuration(1500);// �函�賒�園
				animator.start();
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (x != -1) {
			Rect src = new Rect(0, 0, getWidth(), getHeight());
			Rect dst = new Rect(Utils.dpToPx(1, getResources()), 
					Utils.dpToPx(2, getResources()), 
					getWidth() - Utils.dpToPx(1, getResources()), 
					getHeight() - Utils.dpToPx(2, getResources()));
			canvas.drawBitmap(cropCircle(makeCircle()), src, dst, null);
		}
		invalidate();
	}

	// 銝餉��其�撠�瞍芰����典���
	public Bitmap cropCircle(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// GET AND SET
	
	public void isAnimate(boolean isAnimate) {
		if (isAnimate) {
			playAnimation();
		}
	}
	
	public ImageView getIcon() {
		return icon;
	}
	
	public Drawable getIconDrawable() {
		return iconDrawable;
	}

	public void setIconDrawable(Drawable drawableIcon) {
		this.iconDrawable = drawableIcon;
		icon.setImageDrawable(drawableIcon);
	}

	/**
	 * 霈曄蔭button銝剖��憭批�嚗�霈斗撅葉�曄內�� 
	 * 憒��曄�憭批�頞�鈭��桃�憭批�嚗銋��桐��寞�曄�餈��曉之嚗�啗���曄�銝箸迫
	 * @param size
	 */
	public void setIconSize(int size) {
		iconSize = size;
		setIconParams();
	}

	/**
	 * @return �銝剖��曄��之撠�	 */
	public int getIconSize() {
		return iconSize;
	}

	@Override
	@Deprecated
	public TextView getTextView() {
		// ���寞�
		return null;
	}

}
