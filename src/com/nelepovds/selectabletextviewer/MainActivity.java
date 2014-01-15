package com.nelepovds.selectabletextviewer;

import com.nelepovds.selectabletextviewer.SelectableTextViewer.ISelectableTextViewerListener;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
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
	public ScrollView scroll;

	public String someText = "<h1>sd fsda klfhajks dfkadhsjk</h1> <p>hajskdhfjk adshjk ashdkj fahsdjkhfjk asdhkj fasdfhjk asdfadsashk"
			+ "as kjdfhjkasd fhjas djkfahsdj kfhjkasdh jkafsdh</p> "
			+ "sadjk fhakjsd fashd jkfhsjkdfk hsadjk hfasdjkh jkfashdkj fhaskjd hakfjsdhfjk ahsdl "
			+ "jkad jkfhajk sdhfjk ashdjk hjkasdjkf ashdjkfhadjs khjkasdhjk faksdahsdh fjklhsda"
			+ "jasd kfhasdjk fahskjdh ajksh";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.textViewer = (SelectableTextViewer) findViewById(R.id.selectableTextViewer1);
		this.scroll = (ScrollView) findViewById(R.id.scroll);
		//this.textViewer.setInsideScrollView(scroll);

		SpannableStringBuilder builder = new SpannableStringBuilder(someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText);
		this.textViewer.setText((SpannableStringBuilder)Html.fromHtml(someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText + someText + someText
				+ someText + someText + someText));
		

		this.textViewer
				.setSelectableTextViewerListener(new ISelectableTextViewerListener() {

					@Override
					public void updateSelection(
							SelectableTextViewer selectableTextViewer) {

					}

					@Override
					public void endSelectingText(
							SelectableTextViewer selectableTextViewer,
							String selectedText) {
						 System.out.println("Selected text:" + selectedText);

					}

					@Override
					public void stopSelectingText(
							SelectableTextViewer selectableTextViewer,
							String selectedText) {
						// TODO Auto-generated method stub

					}
				});

	}
}
