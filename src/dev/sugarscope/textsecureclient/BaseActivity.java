package dev.sugarscope.textsecureclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class BaseActivity extends Activity{

	protected Handler mHandler;
	
	
	public void startActivity(Class<?> ctx){
		Intent intent = new Intent(getBaseContext(), ctx);
		startActivity(intent);
	}
	
	public void showMessage(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
