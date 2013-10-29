package dev.sugarscope.textsecureclient;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.textsecureclient.settings.Tag;
import dev.sugarscope.transport.Packet;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SeedActivity extends BaseActivity{
	private EditText edtSeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seed);
		edtSeed = (EditText) findViewById(R.id.edtSeed);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				Toast.makeText(getBaseContext(), packet.toString(), Toast.LENGTH_SHORT).show();
			}
			
		};
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			Client.getInstance().connect(Settings.SERVER_HOST, Settings.SERVER_PORT);
			Client.getInstance().getReader().setHandler(mHandler);	
		} catch (Exception e) {
			Toast.makeText(this, "No se conecto", Toast.LENGTH_SHORT).show();
		}
	}

	public void clickMe(View v){
		final String seed = edtSeed.getText().toString();
		Packet packet = new Packet(Tag.VERIFY_SEED);
		packet.setData(seed);
		Client.getInstance().sendPackage(packet);
	}

}
