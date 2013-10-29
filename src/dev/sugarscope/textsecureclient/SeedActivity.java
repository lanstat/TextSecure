package dev.sugarscope.textsecureclient;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
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

public class SeedActivity extends BaseActivity{
	private EditText edtSeed;
	private String mSeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seed);
		edtSeed = (EditText) findViewById(R.id.edtSeed);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				case Tag.VERIFY_SEED:
					if(Boolean.valueOf(packet.getData()[0].toString())){
						Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
						intent.putExtra("seed", mSeed);
						startActivity(intent);
						finish();
					}else{
						showMessage("Codigo de activacion invalido");
					}
					break;
				}
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
		mSeed = edtSeed.getText().toString();
		Packet packet = new Packet(Tag.VERIFY_SEED);
		packet.setData(mSeed);
		Client.getInstance().sendPackage(packet);
	}

}
