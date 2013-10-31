package dev.sugarscope.textsecureclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SecurityPatternActivity extends Activity {
	
	PatternView mScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_security_pattern);
		mScreen = (PatternView)findViewById(R.id.pattern);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScreen.init(getWindowManager().getDefaultDisplay());
	}
	
	
}
