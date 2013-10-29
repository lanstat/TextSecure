package dev.sugarscope.textsecureclient;

import dev.sugarscope.textsecureclient.settings.Settings;
import persistance.DatabaseHandler;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		DatabaseHandler handler = new DatabaseHandler(this);
		String phone = handler.getSetting("phone");
		if(phone==null){
			Intent intent = new Intent(this, SeedActivity.class);
			startActivity(intent);
		}else{
			Settings.phone = phone;
			Intent intent = new Intent(this, InboxActivity.class);
			startActivity(intent);
		}
		finish();
	}

}
