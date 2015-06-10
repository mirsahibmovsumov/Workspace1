package com.exchange;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	String[] data = { "AZN", "EUR", "USD" };

	private Spinner spinner;
	private TextView t2;
	private EditText e1;
	private DBHandler handler;
	private static String url1 = "http://www.cbar.az/currencies/";
	private static String url2 = ".xml";

	Calendar now = Calendar.getInstance();
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String nowDate = formatter.format(now.getTime());
	String[] separateCurrentDate = nowDate.split("-");
	String year = separateCurrentDate[0];
	String month = separateCurrentDate[1];
	String day = separateCurrentDate[2];

	// final Calendar c = Calendar.getInstance();
	// int mYear = c.get(Calendar.YEAR);
	// int mMonth = c.get(Calendar.MONTH);
	// int mDay = c.get(Calendar.DAY_OF_MONTH);

	private String url = url1 + correct(day) + "." + correct(month) + "."
			+ year + url2;

	public void open(View view) {
		handler = new DBHandler(getBaseContext(), "storeXML", null, 1);
		new HandleXML(getUrl(), getBaseContext()).checkInternetConenction();
		Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
	}

	private String correct(String c) {

		if (c.length() == 2) {
			return c;
		}
		return "0" + c;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		open(null);

		t2 = (TextView) findViewById(R.id.t2);
		e1 = (EditText) findViewById(R.id.e1);

		View button1 = (Button) findViewById(R.id.button);
		button1.setOnClickListener(this);

		e1.addTextChangedListener(new TextWatcher() {

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
				if (e1.getText().toString().equals("")) {
					t2.setText("");
				}
			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				calcRate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.#####");
		return Double.valueOf(twoDForm.format(d));
	}

	private void calcRate() {
		switch (spinner.getSelectedItemPosition()) {

		case 0:
			try {
				String a = e1.getText().toString();
				double azn = Double.parseDouble(a);
				t2.setText("EURO -- "
						+ Double.toString(roundTwoDecimals(azn
								* handler.getValue("EUR")))
						+ "\nUSD -- "
						+ Double.toString(roundTwoDecimals(azn
								* (1.0 / handler.getValue("USD")))));

			} catch (NumberFormatException e) {

			}

			break;

		case 1:
			try {
				String e = e1.getText().toString();
				double euro = Double.parseDouble(e);

				t2.setText("AZN -- "
						+ Double.toString(roundTwoDecimals(euro
								* (1.0 / handler.getValue("EUR"))))
						+ "\nUSD -- "
						+ Double.toString(roundTwoDecimals(euro
								* handler.getValue("EUR")
								/ handler.getValue("USD"))));

			} catch (NumberFormatException e) {
			}

			break;
		case 2:
			try {
				String d = e1.getText().toString();
				double dollar = Double.parseDouble(d);
				t2.setText("AZN -- "
						+ Double.toString(roundTwoDecimals(dollar
								* handler.getValue("USD")))
						+ "\nEURO -- "
						+ Double.toString(roundTwoDecimals(dollar
								* handler.getValue("USD")
								/ handler.getValue("EUR"))));

			} catch (NumberFormatException e) {
			}

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button) {
			Intent i = new Intent(this, NextPage.class);
			startActivity(i);
		}

	}

	public String getUrl() {
		return url;
	}

}
