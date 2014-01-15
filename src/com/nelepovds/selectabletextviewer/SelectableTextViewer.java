package com.nelepovds.selectabletextviewer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectableTextViewer extends RelativeLayout {

	private ImageView imgStartSelect;
	private int mStartSelect = -1;
	private ImageView imgEndSelect;
	private int mEndSelect = -1;
	private int mImgWidth = -1;
	private int mImgHeight = -1;

	private TextView textView;

	private View mCurrentControlFocused;

	public static interface ISelectableTextViewerListener {

		public void updateSelection(SelectableTextViewer selectableTextViewer);

		public void endSelectingText(SelectableTextViewer selectableTextViewer,
				String selectedText);

	}

	private ISelectableTextViewerListener selectableTextViewerListener;

	public void setSelectableTextViewerListener(
			ISelectableTextViewerListener selectableTextViewerListener) {
		this.selectableTextViewerListener = selectableTextViewerListener;
	}

	public SelectableTextViewer(Context context) {
		super(context);
		if (!isInEditMode()) {
			this.initControls();
		}
	}

	public SelectableTextViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			this.initControls();
		}
	}

	public SelectableTextViewer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			this.initControls();
		}
	}

	private void initControls() {
		this.textView = new TextView(getContext());
		this.addView(textView);
		this.setOnLongClickListener(new TextView.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showSelectionControls();
				return false;
			}
		});

		this.createImgControllersForSelection();

	}

	protected void createImgControllersForSelection() {
		this.imgStartSelect = new ImageView(getContext());
		this.imgEndSelect = new ImageView(getContext());
		this.imgStartSelect.setImageResource(R.drawable.cursor);
		this.imgEndSelect.setImageResource(R.drawable.cursor);
		this.addView(imgStartSelect, 40, 50);
		this.addView(imgEndSelect, 40, 50);

		this.imgEndSelect.setClickable(true);
		this.imgStartSelect.setClickable(true);
		OnClickListener onClickForChangeFocus = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentControlFocused = v;
			}
		};
		this.imgEndSelect.setOnClickListener(onClickForChangeFocus);
		this.imgStartSelect.setOnClickListener(onClickForChangeFocus);

		View.OnTouchListener onTouchSelectionControl = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mCurrentControlFocused = v;
				int eid = event.getAction();
				switch (eid) {
				case MotionEvent.ACTION_MOVE:

					RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) v
							.getLayoutParams();
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();
					if (mImgWidth == -1) {
						mImgWidth = imgStartSelect.getMeasuredWidth();
					}
					if (mImgHeight == -1) {
						mImgHeight = imgStartSelect.getMeasuredHeight();
					}

					mParams.leftMargin = x - mImgWidth;
					mParams.topMargin = y - mImgHeight * 3;
					v.setLayoutParams(mParams);
					updateSelectionByMovementImgControls(mParams.leftMargin
							+ mImgWidth / 2, mParams.topMargin);
					break;
				case MotionEvent.ACTION_UP:
					if (selectableTextViewerListener != null) {
						selectableTextViewerListener.endSelectingText(
								SelectableTextViewer.this, getSelectedText());
					}
					break;
				default:
					break;
				}
				return true;

			}
		};
		this.imgEndSelect.setOnTouchListener(onTouchSelectionControl);
		this.imgStartSelect.setOnTouchListener(onTouchSelectionControl);

		this.imgEndSelect.setVisibility(View.GONE);
		this.imgStartSelect.setVisibility(View.GONE);

	}

	protected void updateSelectionByMovementImgControls(int x, int y) {
		if (mCurrentControlFocused.equals(imgStartSelect)) {
			this.mStartSelect = getOffsetByCoordinates(x, y);
		} else if (mCurrentControlFocused.equals(imgEndSelect)) {
			this.mEndSelect = getOffsetByCoordinates(x, y);
		}
		updateSelectionSpan();

	}

	protected Layout updateSelectionSpan() {
		Layout retLayout = this.textView.getLayout();
		if (this.mStartSelect > -1 && this.mEndSelect > -1) {
			if (this.mStartSelect > this.mEndSelect) {
				int temp = mEndSelect;
				this.mEndSelect = mStartSelect;
				this.mStartSelect = temp;
				showSelectionControls();
			}

			SpannableStringBuilder spannable = new SpannableStringBuilder(
					this.textView.getText());

			spannable.clearSpans();
			spannable.setSpan(new BackgroundColorSpan(Color.LTGRAY),
					this.mStartSelect, this.mEndSelect, Spannable.SPAN_USER);
			this.textView.setText(spannable);
			this.textView.requestLayout();
			if (this.selectableTextViewerListener != null) {
				this.selectableTextViewerListener.updateSelection(this);
			}
		}
		return retLayout;
	}

	protected void showSelectionControls() {
		if (this.mStartSelect > -1 && this.mEndSelect > -1) {
			Layout layout = updateSelectionSpan();

			Rect parentTextViewRect = new Rect();

			RelativeLayout.LayoutParams startLP = (LayoutParams) this.imgStartSelect
					.getLayoutParams();
			float xStart = layout.getPrimaryHorizontal(this.mStartSelect);
			float yStart = layout.getLineBounds(
					layout.getLineForOffset(this.mStartSelect),
					parentTextViewRect);
			startLP.setMargins((int) xStart, (int) yStart, -1, -1);
			this.imgStartSelect.setLayoutParams(startLP);
			this.imgStartSelect.setVisibility(View.VISIBLE);

			RelativeLayout.LayoutParams endLP = (LayoutParams) this.imgEndSelect
					.getLayoutParams();
			float xEnd = layout.getPrimaryHorizontal(this.mEndSelect);
			float yEnd = layout.getLineBounds(
					layout.getLineForOffset(this.mEndSelect),
					parentTextViewRect);
			endLP.setMargins((int) xEnd, (int) yEnd, -1, -1);
			this.imgEndSelect.setLayoutParams(endLP);
			this.imgEndSelect.setVisibility(View.VISIBLE);

			System.out.println("xStart:" + xStart + "\t yStart:" + yStart
					+ "\t xEnd:" + xEnd + "\t yEnd:" + yEnd + "");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (this.imgStartSelect != null) {
				if (this.imgStartSelect.getVisibility() == View.GONE) {
					this.onTouchDownCalcSelections(event);
				} else {
					this.stopSelecting();
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void hideSelectionControls() {
		this.imgStartSelect.setVisibility(View.GONE);
		this.imgEndSelect.setVisibility(View.GONE);

	}

	private int getOffsetByCoordinates(int x, int y) {
		int retOffset = -1;
		Layout layout = this.textView.getLayout();
		if (layout != null) {
			int line = layout.getLineForVertical(y);
			retOffset = layout.getOffsetForHorizontal(line, x);
		}
		return retOffset;
	}

	private void onTouchDownCalcSelections(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		this.mStartSelect = getOffsetByCoordinates(x, y);

		// Calculate text end
		String tempStr = this.textView.getText().toString();
		tempStr = tempStr.substring(this.mStartSelect);
		Pattern pt = Pattern.compile("\\s");
		Matcher mt = pt.matcher(tempStr);
		if (mt.find()) {
			String match = mt.group(0);
			tempStr = tempStr.substring(0, tempStr.indexOf(match));
		}
		this.mEndSelect = this.mStartSelect + tempStr.length();

	}

	public void setText(SpannableStringBuilder builder) {
		this.textView.setText(builder);

	}

	public ImageView getImgEndSelect() {
		return imgEndSelect;
	}

	public ImageView getImgStartSelect() {
		return imgStartSelect;
	}

	/**
	 * For this all doing
	 * 
	 * @return
	 */
	public String getSelectedText() {
		String retSelectedString = null;
		if (this.mStartSelect > -1 && this.mEndSelect > -1) {
			retSelectedString = this.textView.getText()
					.subSequence(this.mStartSelect, this.mEndSelect).toString();
		}
		return retSelectedString;
	}

	/**
	 * Hides cursors and clears
	 * 
	 * @return
	 */
	public void stopSelecting() {
		this.hideSelectionControls();
		SpannableStringBuilder spannable = new SpannableStringBuilder(
				this.textView.getText());

		spannable.clearSpans();
		this.textView.setText(spannable);
	}

}
