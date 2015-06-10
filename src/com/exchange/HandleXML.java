package com.exchange;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class HandleXML {
	private String urlstring = null;
	XmlPullParserFactory xmlFactoryObject;
	private Context context;
	public Map<String, String> valyuta = new HashMap<String, String>();
	private DBHandler dbHandler;

	public HandleXML(String url, Context context) {
		urlstring = url;
		this.context = context;
	}

	public boolean checkInternetConenction() {
		ConnectivityManager check = (ConnectivityManager) (this.context)
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (check != null) {
			NetworkInfo[] info = check.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Toast.makeText(context, "internet is connected",
								Toast.LENGTH_SHORT).show();
						fetchXML();
						return true;
					}
		}
		Toast.makeText(context, "No Internet Connection ... ", Toast.LENGTH_SHORT)
				.show();
		return false;
	}

	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlstring);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();

					InputStream stream = conn.getInputStream();

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(
							XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);
					parseXMLAndStoreIt(myparser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		dbHandler = new DBHandler(context, "storeXML", null, 1);
		int event;
		try {
			String valutaTagAtr = null;
			String tagName = "";
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				String value = myParser.getText();
				
				switch (event) {
				case XmlPullParser.START_TAG:

					if (name.equals("Valute")) {
						valutaTagAtr = myParser.getAttributeValue(0);
						Log.i("----------",valutaTagAtr);
					}

					tagName = name;

					break;
				case XmlPullParser.TEXT:

					if (tagName.equals("Value")) {
						if (valutaTagAtr != null)
							valyuta.put(valutaTagAtr, value);
						Log.i("++++++++",value);
					}

					break;

				case XmlPullParser.END_TAG:
					break;
				}
				event = myParser.next();

			}
			dbHandler.addValute(valyuta);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
