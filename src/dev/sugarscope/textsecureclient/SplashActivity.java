package dev.sugarscope.textsecureclient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import dev.sugarscope.textsecureclient.persistance.DatabaseHandler;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.textsecureclient.users.SeedActivity;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
			Intent intent = new Intent(this, SecurityPatternActivity.class);
			startActivity(intent);
		}
		finish();
	}

}
