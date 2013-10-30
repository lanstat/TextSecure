package dev.sugarscope.textsecureclient.users;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.BaseActivity;
import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.controllers.ControlUsuario;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;
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
	private String mNumber;
	private ControlUsuario mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		mController = new ControlUsuario();
		mPhone = (EditText)findViewById(R.id.edtPhone);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mSeed = bundle.getString("seed");
		}else{
			finish();
		}
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				case Tag.SIGN_UP:
					showMessage("Registro exitoso");
					mController.guardar(mNumber);
					finish();
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
		mNumber = mPhone.getText().toString();
		Packet packet =new Packet(Tag.SIGN_UP);
		packet.setData(mSeed, getImei(), mNumber);
		Client.getInstance().sendPackage(packet);
	}

}
