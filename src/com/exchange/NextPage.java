package com.exchange;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class NextPage extends Activity implements OnClickListener {

	String[] data = { "Silver", "Gold", "Platin" };
	private TextView textView1;
	private EditText editText1;
	private Spinner spin; 
	private DBHandler dbHandler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next_page);
		open(null);
		View b_next = (Button) findViewById(R.id.b_next);
		b_next.setOnClickListener(this);

		textView1 = (TextView) findViewById(R.id.textView1);
		editText1 = (EditText) findViewById(R.id.editText1);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spin = (Spinner) findViewById(R.id.spin);
		spin.setAdapter(adapter);
		spin.setSelection(0);

		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				calcRate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		editText1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				calcRate();
				if (editText1.getText().toString().equals("")) {
					textView1.setText("");
				}
			}
		});

	}

	public void open(View v) {
		dbHandler = new DBHandler(getBaseContext(), "storeXML", null, 1);
	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.###");
		return Double.valueOf(twoDForm.format(d));
	}

	protected void calcRate() {
		switch (spin.getSelectedItemPosition()) {
		case 0:
			try {
				String value = editText1.getText().toString();
				textView1.setText(Double.toString(roundTwoDecimals(Double
						.valueOf(value) * dbHandler.getValue("XAG") / 31.1035))
						+ " AZN");

			} catch (NumberFormatException e) {

			}
			break;

		case 1:
			try {
				String value = editText1.getText().toString();
				textView1.setText(Double.toString(roundTwoDecimals(Double
						.valueOf(value) * dbHandler.getValue("XAU") / 31.1035))
						+ " AZN");
			} catch (NumberFormatException e) {

			}
			break;
		case 2:
			try {
				String value = editText1.getText().toString();
				textView1.setText(Double.toString(roundTwoDecimals(Double
						.valueOf(value) * dbHandler.getValue("XPT") / 31.1035))
						+ " AZN");
			} catch (NumberFormatException e) {

			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.b_next) {
			this.finish();
		 }
	}

}