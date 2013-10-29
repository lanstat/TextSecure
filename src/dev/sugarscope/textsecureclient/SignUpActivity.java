package dev.sugarscope.textsecureclient;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.settings.Tag;
import dev.sugarscope.transport.Packet;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;

@SuppressLint("HandlerLeak")
public class SignUpActivity extends BaseActivity {
	private String mSeed;
	private EditText mPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		mPhone = (EditText)findViewById(R.id.edtPhone);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mSeed = bundle.getString("seed");
		}
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				case Tag.SIGN_UP:
					showMessage("Registrado");
					break;
				}
			}
			
		};
	}
	
	private String getImei(){
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Client.getInstance().getReader().setHandler(mHandler);
	}

	public void clickMe(View v){
		final String phone = mPhone.getText().toString();
		Packet packet =new Packet(Tag.SIGN_UP);
		packet.setData(mSeed, getImei(), phone);
		Client.getInstance().sendPackage(packet);
	}

}
