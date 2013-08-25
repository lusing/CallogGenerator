package com.yunos.xulun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.opengl.GLES20;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView txtView;
	Button btnRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtView = (TextView)findViewById(R.id.txtView);
		btnRun = (Button)findViewById(R.id.btnRun);
		
		btnRun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AsyncTask<Context, Void, String>(){

					@Override
					protected String doInBackground(Context... arg0) {
						StringBuilder sb = new StringBuilder();
						Context context = arg0[0];
						
						String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
						sb.append("Extensions: "+extensions+"\n");
						
						Cursor cursor = context.getContentResolver()
								.query(CallLog.Calls.CONTENT_URI,
										new String[] { CallLog.Calls.NUMBER,
												CallLog.Calls.CACHED_NAME,
												CallLog.Calls.TYPE,
												CallLog.Calls.DATE,
												CallLog.Calls.DURATION}, null,
										null, CallLog.Calls.DEFAULT_SORT_ORDER);
						
						if (cursor == null || cursor.getCount() <= 0) {
							sb.append("No Call Log.\n");
						} else {

							for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
									.moveToNext()) {
								sb.append("Phone number is: ");
								sb.append(cursor.getString(0));
								sb.append("\tCached name is: ");
								sb.append(cursor.getString(1));
								sb.append("\tCall type is: ");
								sb.append(cursor.getString(2));
								sb.append("\tCall Date is: ");
								SimpleDateFormat sfd = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss", Locale.US);
								Date date = new Date(Long.parseLong(cursor
										.getString(3)));
								String time = sfd.format(date);
								sb.append(time);
								sb.append("\tCall Duration is: ");
								sb.append(cursor.getString(4));
								sb.append("\n");
							}
						}
						
						ContentValues values = new ContentValues();
				        values.clear();
				        values.put(CallLog.Calls.NUMBER, "13100000001");
				        values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
				        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				        
				        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
				        values.clear();
				        values.put(CallLog.Calls.NUMBER, "13200000002");
				        values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
				        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				        values.put(CallLog.Calls.DURATION, 1);
				        
				        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
				        values.clear();
				        values.put(CallLog.Calls.NUMBER, "13300000003");
				        values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
				        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				        values.put(CallLog.Calls.DURATION, 2);
				        
				        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
				        values.clear();
				        values.put(CallLog.Calls.NUMBER, "13400000004");
				        values.put(CallLog.Calls.TYPE, CallLog.Calls.MISSED_TYPE);
				        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				        
				        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
				        values.clear();
				        values.put(CallLog.Calls.NUMBER, "13500000005");
				        values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
				        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				        values.put(CallLog.Calls.DURATION, 100);
				        
				        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
				        
				        insertMissedCall(context,"13011111111");
				        insertMissedCall(context,"13011111112");
				        insertMissedCall(context,"13011111113");
				        insertMissedCall(context,"13011111114");
				        insertMissedCall(context,"13011111115");
				        insertMissedCall(context,"13011111116");
				        insertMissedCall(context,"13011111117");
				        insertMissedCall(context,"13011111118");
				        insertMissedCall(context,"13011111119");
				        
				        insertIncomingCall(context,"13211111111");
				        insertIncomingCall(context,"13311111111");
				        insertIncomingCall(context,"13411111111");
				        insertIncomingCall(context,"13511111146");
				        insertIncomingCall(context,"13611111111");
				        insertIncomingCall(context,"13900000001");
				        sb.append("\nInsert success!");
						
						return sb.toString();
					}

					@Override
					protected void onPostExecute(String result){
						txtView.setText(result);
					}
					
				}.execute(MainActivity.this);
			}
		});
	}
	
	private void insertMissedCall(Context context, String number){
		ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.TYPE, CallLog.Calls.MISSED_TYPE);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
	}
	
	private void insertIncomingCall(Context context, String number){
		ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, 360);
        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
