package dev.sugarscope.textsecureclient.contacts;

import dev.sugarscope.textsecureclient.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
