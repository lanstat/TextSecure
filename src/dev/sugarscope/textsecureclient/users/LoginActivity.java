package dev.sugarscope.textsecureclient.users;

import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.R.layout;
import dev.sugarscope.textsecureclient.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
