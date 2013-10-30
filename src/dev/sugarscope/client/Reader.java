package dev.sugarscope.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Handler;
import android.util.Log;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.transport.Packet;

public class Reader implements Runnable {
	private byte[] mBuffer;
	private BufferedInputStream mInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mIsAlive = true;
	private Handler mHandler;
	
	public Reader(InputStream lclsInput) throws IOException{
		mBuffer = new byte[0];
		mInput = new BufferedInputStream(lclsInput);
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
	}
	
	public void setRunning(boolean running){
		mIsAlive = running;
		try {
			mInput.close();
		} catch (IOException e) {
			Log.e(Settings.TAG, e.getMessage());
		}
	}
	
	@Override
	public void run() {
		while (mIsAlive) {
			try {
				byte[] buffer = new byte[BUFFER_SIZE];
				mInput.read(buffer);
				if(mInput.available()==0){
					mBuffer = Utils.concat(mBuffer, buffer);
					final Packet packet = (Packet)Utils.deserialize(mBuffer);
					if(mHandler!=null){
						Log.i(Settings.TAG, packet.toString());
						mHandler.sendMessage(mHandler.obtainMessage(0, packet));
					}
					mBuffer = new byte[0];
				}else{
					mBuffer = Utils.concat(mBuffer, buffer);
				}
			} catch (Exception e) {
				mIsAlive = false;
			} 
		}
	}

}
