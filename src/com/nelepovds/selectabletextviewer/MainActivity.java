package com.nelepovds.selectabletextviewer;

import com.nelepovds.selectabletextviewer.SelectableTextViewer.ISelectableTextViewerListener;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView.BufferType;

public class MainActivity extends Activity {

	public SelectableTextViewer textViewer;

	public String someText = "sd fsda klfhajks dfkadhsjk hajskdhfjk adshjk ashdkj fahsdjkhfjk asdhkj fasdfhjk asdfadsashk"
			+ "as kjdfhjkasd fhjas djkfahsdj kfhjkasdh jkafsdh "
			+ "sadjk fhakjsd fashd jkfhsjkdfk hsadjk hfasdjkh jkfashdkj fhaskjd hakfjsdhfjk ahsdl "
			+ "jkad jkfhajk sdhfjk ashdjk hjkasdjkf ashdjkfhadjs khjkasdhjk faksdahsdh fjklhsda"
			+ "jasd kfhasdjk fahskjdh ajksh";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textViewer = new SelectableTextViewer(this);

		ScrollView sc = new ScrollView(this);
		// sc.addView(textViewer);
		setContentView(textViewer);

		SpannableStringBuilder builder = new SpannableStringBuilder(someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText);
		this.textViewer.setText(builder);

		this.textViewer
				.setSelectableTextViewerListener(new ISelectableTextViewerListener() {

					@Override
					public void updateSelection(
							SelectableTextViewer selectableTextViewer) {
						System.out.println("Update selection");

					}

					@Override
					public void endSelectingText(
							SelectableTextViewer selectableTextViewer,
							String selectedText) {
						System.out.println("Selected text:"+selectedText);


					}
				});

	}

}
