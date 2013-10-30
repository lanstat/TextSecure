package dev.sugarscope.textsecureclient;

import dev.sugarscope.textsecureclient.messages.InboxActivity;
import dev.sugarscope.textsecureclient.persistance.DatabaseHandler;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.textsecureclient.users.SeedActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		final DatabaseHandler handler = DatabaseHandler.getInstance();
		handler.init(this);
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
