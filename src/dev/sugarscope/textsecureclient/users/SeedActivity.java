package dev.sugarscope.textsecureclient.users;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.BaseActivity;
import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.R.id;
import dev.sugarscope.textsecureclient.R.layout;
import dev.sugarscope.textsecureclient.controllers.ControlUsuario;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;

@SuppressLint("HandlerLeak")
public class SeedActivity extends BaseActivity{
	private EditText edtSeed;
	private String mSeed;
	private ControlUsuario mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seed);
		
		mController = new ControlUsuario();
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
		mController.verificarCodigoActivacion(mSeed);
	}

}
