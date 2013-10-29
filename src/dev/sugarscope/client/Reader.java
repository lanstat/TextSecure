package dev.sugarscope.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Handler;
import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Reader implements Runnable {
	private byte[] marrBuffer;
	private BufferedInputStream mclsInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mblnIsAlive = true;
	private Handler mHandler;
	
	public Reader(InputStream lclsInput) throws IOException{
		marrBuffer = new byte[0];
		mclsInput = new BufferedInputStream(lclsInput);
	}
	
	public void setHandler(Handler handler){
		mHandler = handler;
	}
	
	public void setRunning(boolean lblnRunning){
		mblnIsAlive = lblnRunning;
		try {
			mclsInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (mblnIsAlive) {
			try {
				byte[] larrBuffer = new byte[BUFFER_SIZE];
				mclsInput.read(larrBuffer);
				if(mclsInput.available()==0){
					marrBuffer = Utils.concat(marrBuffer, larrBuffer);
					final Packet lclsPacket = (Packet)Utils.deserialize(marrBuffer);
					if(mHandler!=null){
						mHandler.sendMessage(mHandler.obtainMessage(0, lclsPacket));
					}
					marrBuffer = new byte[0];
				}else{
					marrBuffer = Utils.concat(marrBuffer, larrBuffer);
				}
			} catch (Exception e) {
				mblnIsAlive = false;
			} 
		}
	}

}
