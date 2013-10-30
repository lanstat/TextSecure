package dev.sugarscope.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;
import dev.sugarscope.transport.Packet;

public class Client implements Observer{
	private Socket mSocket;
	private static Client mClient;
	private Reader mReader;
	private ArrayList<Packet> mStackPakects;
	private boolean mSendingInProcess;
	
	private Client(){
		mSendingInProcess = false;
		mStackPakects = new ArrayList<Packet>();
	}
	
	public static Client getInstance(){
		if(mClient==null)
			mClient = new Client();
		return mClient;
	}
	
	/**
	 * Conecta con el servidor especificado
	 * @param serverHost Direccion del servidor
	 * @param port Puerto de conexion
	 * @throws UnknownHostException Lanzado cuando no se logra encontrar el servidor.
	 * @throws IOException Lanzado cuando existe un error la comunicacion.
	 */
	public void connect(String serverHost, int port) throws UnknownHostException, IOException{
		if(mSocket==null){
			mSocket = new Socket(serverHost, port);
			mReader = new Reader(mSocket.getInputStream());
			new Thread(mReader).start();
		}else{
			Log.e("TextSecure", "El socket ya esta creado");
		}
	}
	
	/**
	 * Envia un paquete al servidor, si el cliente se encuentre en proceso de envio coloca el paquete en cola.
	 * @param lclsPacket {@link Packet} Paquete con los datos a enviar. 
	 */
	public void sendPackage(Packet lclsPacket){
		if(mSocket.isConnected()){
			try {
				if(!mSendingInProcess){
					sendPacketNow(lclsPacket);
					mSendingInProcess = true;
				}else{
					mStackPakects.add(lclsPacket);
				}
			} catch (IOException e) {
				e.printStackTrace();
				mSendingInProcess = false;
			}
		}
	}
	
	/**
	 * 
	 * @param lclsPacket
	 * @throws IOException
	 */
	private void sendPacketNow(Packet lclsPacket) throws IOException{
		final Sender lclsSender = new Sender(lclsPacket, mSocket.getOutputStream());
		lclsSender.addObserver(this);
		new Thread(lclsSender).start();
	}
	
	/**
	 * Cierra la conexion y detiene los hilos de escucha.
	 * @throws IOException 
	 */
	public void close() throws IOException{
		if(mSocket != null && mSocket.isConnected()){
			mReader.setRunning(false);
			mSocket.close();
		}
		mReader = null;
		mStackPakects = null;
		mSocket = null;
		mClient = null;
	}
	
	public Reader getReader(){
		return mReader;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(mStackPakects.isEmpty()){
			mSendingInProcess = false;
			return;
		}
		try {
			sendPacketNow(mStackPakects.get(0));
			mStackPakects.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
