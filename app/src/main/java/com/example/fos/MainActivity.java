package com.example.fos;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitgen.SDK.AndroidBSP.NBioBSPJNI;
import com.nitgen.SDK.AndroidBSP.NBioBSPJNI.CAPTURED_DATA;
import com.nitgen.SDK.AndroidBSP.NBioBSPJNI.IndexSearch.FP_INFO;
import com.nitgen.SDK.AndroidBSP.NBioBSPJNI.IndexSearch.SAMPLE_INFO;





public class MainActivity extends Activity implements NBioBSPJNI.CAPTURE_CALLBACK, OnClickListener{
	public static int opt;
	public static String recd_msg;
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private NBioBSPJNI				bsp;
	private NBioBSPJNI.Export       exportEngine;
	private NBioBSPJNI.IndexSearch  indexSearch;
	private byte[]					byTemplate1;
	private byte[]					byTemplate2;
	
	private byte[]					byCapturedRaw1;
	private int						nCapturedRawWidth1;
	private int						nCapturedRawHeight1;

	private byte[]					byCapturedRaw2;
	private int						nCapturedRawWidth2;
	private int						nCapturedRawHeight2;
	
	ImageView img_fp_src; 
	TextView tvInfo, tvVer, tvDevice;	
	Button btnCapture1,btnAutoOn1,sendBtn; //btnCapture2, btnVerifyTemplate, btnVerifyRaw, btnAutoOn1, btnAutoOn2;
	
	
	private boolean					bCapturedFirst, bAutoOn = false;
	
	public static final int QUALITY_LIMIT = 80;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initView();
        initData();
        bsp.OpenDevice();
        ImageView my= (ImageView)findViewById(R.id.imageView1);
        my.setAlpha(50);
    }
    
    /**
     * void
     */
    public void initView(){
    
    	setContentView(R.layout.activity_main);
    	
    	img_fp_src = (ImageView)findViewById(R.id.img_fp_src);
    	

    	    	
    	tvInfo = (TextView) findViewById(R.id.textInfo);
    	tvVer = (TextView) findViewById(R.id.textVer);
    	tvDevice = (TextView) findViewById(R.id.textDevice);
    	tvDevice.setOnClickListener(this);
        btnCapture1 = (Button) findViewById(R.id.btnCapture1);
    	btnCapture1.setEnabled(false);
    
    	sendBtn=(Button) findViewById(R.id.send);
    	sendBtn.setOnClickListener(this);
    	sendBtn.setEnabled(false);
 
    
    }

    /**
     * void
     */
    public void initData(){

    	NBioBSPJNI.CURRENT_PRODUCT_ID = 0;
    	if(bsp==null){    	
    		bsp = new NBioBSPJNI("010701-613E5C7F4CC7C4B0-72E340B47E034015", this);
    		String msg = null;
    		if (bsp.IsErrorOccured())
    			msg = "NBioBSP Error: " + bsp.GetErrorCode();
    		else  {
    			msg = "Keep the finger on device and press Capture";
    			exportEngine = bsp.new Export();
    			indexSearch = bsp.new IndexSearch();
    		}
    		tvVer.setText(msg);
    	}
    	
    	
    }

    
    @Override
    public void onDestroy(){
    	
    	super.onDestroy();
    	
        if (bsp != null) {
            bsp.dispose();
            bsp = null;
        }
        
    }
   
    public int OnCaptured(CAPTURED_DATA capturedData){
    	
    	tvDevice.setText("IMAGE Quality: "+capturedData.getImageQuality());	
    	
    	if( capturedData.getImage()!=null){    		
    		if (bCapturedFirst){    		
    			
    		//	Bitmap bitmap = capturedData.getImage();
    		//	img_fp_src.setImageBitmap( bitmap);
    			img_fp_src.setImageBitmap( capturedData.getImage());
    			sendBtn.setEnabled(true);
    			//FileOutputStream outstream = null;
    			//ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			//bitmap.compress(CompressFormat.JPEG,100,bos);
    		////	try {
			//		bitmap.compress(CompressFormat.JPEG,100,new FileOutputStream(Environment.getExternalStorageDirectory()+"kiran.jpg"));
			//	} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
			//	}
    			img_fp_src.buildDrawingCache();
    			Bitmap kin=img_fp_src.getDrawingCache();
    		
    			FileOutputStream outStream=null;
    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			kin.compress(CompressFormat.JPEG,100,bos);
    			byte[] data = bos.toByteArray();
    			try{
    			 outStream = new FileOutputStream(String.format("/mnt/sdcard/image11.jpg"));
    			outStream.write(data);
    			outStream.close();
    		   }
    			catch(IOException e){
    				e.printStackTrace();}
    			}
    			
    	}

    	// quality : 40~100
    	if(capturedData.getImageQuality()>=QUALITY_LIMIT){
    		
    		return NBioBSPJNI.ERROR.NBioAPIERROR_USER_CANCEL;
    	}else if(capturedData.getDeviceError()!=NBioBSPJNI.ERROR.NBioAPIERROR_NONE){
    		
    		return capturedData.getDeviceError();
    	}else{
    		return NBioBSPJNI.ERROR.NBioAPIERROR_NONE;    		
    	}
    	
    }
    
	/* (non-Javadoc)
	 * @see com.nitgen.SDK.AndroidBSP.SampleDialogFragment.SampleDialogListener#onClickStopBtn(android.app.DialogFragment)
	 */
	public void onClickStopBtn(DialogFragment dialogFragment) {

		bAutoOn = false;
		

	}

	public void OnConnected() {
		
		
		
		String message = "Device Open Success";
		tvDevice.setText(message);
		
		btnCapture1.setEnabled(true);
		
	}
	
	public void OnDisConnected(){
		
		NBioBSPJNI.CURRENT_PRODUCT_ID = 0;
		
		
		
		String message = "NBioBSP Disconnected: " + bsp.GetErrorCode();
		tvDevice.setText(message);
		
		btnCapture1.setEnabled(false);
	
		

		
	}

    /**
     * void
     */
  /*  public void OnBtnOpenDevice(View target){

    			    	
    	

    }		*/
    
   
    
    
    
	/**
	 * void
	 */
	public void OnBtnCapture1(View target) {
		
		

		new Thread(new Runnable() {
			
			public void run() {

				OnCapture1(10000);
				
			}
		}).start();
				
	}
	
	String msg = "";
	public synchronized void OnCapture1(int timeout){
		
		NBioBSPJNI.FIR_HANDLE hCapturedFIR, hAuditFIR;
    	NBioBSPJNI.CAPTURED_DATA capturedData;
    	
    	hCapturedFIR = bsp.new FIR_HANDLE();
    	hAuditFIR = bsp.new FIR_HANDLE();
    	capturedData = bsp.new CAPTURED_DATA();
    	
    	bCapturedFirst = true;
		
		bsp.Capture(NBioBSPJNI.FIR_PURPOSE.ENROLL,hCapturedFIR,timeout, hAuditFIR, capturedData, MainActivity.this,0, null);
		
		
		
		if (bsp.IsErrorOccured())  {
        	msg = "NBioBSP Capture Error: " + bsp.GetErrorCode();
        }
        else  {
        	NBioBSPJNI.INPUT_FIR inputFIR;
        	
        	inputFIR = bsp.new INPUT_FIR();
        	
        	// Make ISO 19794-2 data
        	{
        		NBioBSPJNI.Export.DATA exportData;
        		
        		inputFIR.SetFIRHandle(hCapturedFIR);
        		
        		exportData = exportEngine.new DATA();
        		
        		exportEngine.ExportFIR(inputFIR, exportData, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA);
        		
        		if (bsp.IsErrorOccured())  {
        			runOnUiThread(new Runnable() {
						
						public void run() {
							msg = "NBioBSP ExportFIR Error: " + bsp.GetErrorCode();
							tvInfo.setText(msg);
							Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
						}
					});
            		return ;
            	}
        		
        		if (byTemplate1 != null)
        			byTemplate1 = null;
        		
        		byTemplate1 = new byte[exportData.FingerData[0].Template[0].Data.length];
        		byTemplate1 = exportData.FingerData[0].Template[0].Data;
        	}
        	
        	// Make Raw Image data
        	{
        		NBioBSPJNI.Export.AUDIT exportAudit;
        		
        		inputFIR.SetFIRHandle(hAuditFIR);
        		
        		exportAudit = exportEngine.new AUDIT();
        		
        		exportEngine.ExportAudit(inputFIR, exportAudit);
        		
        		if (bsp.IsErrorOccured())  {
        			
        			runOnUiThread(new Runnable() {
						
						public void run() {
							msg = "NBioBSP ExportAudit Error: " + bsp.GetErrorCode();
							tvInfo.setText(msg);
							Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
						}
					});
        			
            		return ;
            	}
        		
        		if (byCapturedRaw1 != null)
        			byCapturedRaw1 = null;
        		
        		byCapturedRaw1 = new byte[exportAudit.FingerData[0].Template[0].Data.length];
        		byCapturedRaw1 = exportAudit.FingerData[0].Template[0].Data;
    			
    			nCapturedRawWidth1 = exportAudit.ImageWidth;
   
				msg = " Capture Success";
				
        	}

        }
		
		runOnUiThread(new Runnable() {
			
			public void run() {
				tvInfo.setText(msg);
				
				if (byTemplate1 != null && byTemplate2 != null)  {
		
				}else{
			
				}
				
				if (byCapturedRaw1 != null && byCapturedRaw2 != null)  {
			
				}else{
		
				}
				
			}
		});
		
	}
	
	    
    
    
    
  
   
	
	
	
	
	
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	   switch(v.getId())
	   {
	   case R.id.send:
		   
		 final ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Verifying Fingerprint ...", true);

         ringProgressDialog.setCancelable(true);
		 
		         new Thread(new Runnable() {
		 
		             @Override
		 
		             public void run() {
		 
		                 try {
		                	sendToServer();
		                     // Here you should write your time consuming task...
		 
		                     // Let the progress ring for 10 seconds...
		 
		                  
		 
		                 } catch (Exception e) {
		 
		  
		 
		                 }
		 
		                 ringProgressDialog.dismiss();
		                 
		 
		             }
		 
		         }).start();			     
		         break;
		
	   case R.id.textDevice:
		   
		   bsp.OpenDevice();
		   break;
	   } 
	

	
		
	}
	
	
	
	
	public void sendToServer()
	{
		Socket socket = null;
		DataOutputStream dataOutputStream = null; 
		DataInputStream dataInputStream = null;
		try 
		{
			
			 socket = new Socket("192.168.1.53", 8889);
			 

			 
// Connection Checking Message Exchange
			 
			 
			 
			 dataOutputStream = new DataOutputStream(socket.getOutputStream());
			 dataOutputStream.writeUTF("1");
			 
			 dataInputStream = new DataInputStream(socket.getInputStream());
			 recd_msg=dataInputStream.readUTF();
			 
			 
			// Toast.makeText(MainActivity.this,"Recieved nonce"+option,Toast.LENGTH_SHORT).show();
			 

		
		if(Integer.parseInt(recd_msg)==2)
		{
			 
			 File myFile = new File ("/sdcard/image11.jpg");
			  
			  
			
			  byte [] mybytearray  = new byte [(int)myFile.length()];
	      
		      FileInputStream fis = new FileInputStream(myFile);
		      BufferedInputStream bis = new BufferedInputStream(fis);
		      bis.read(mybytearray,0,mybytearray.length);
		      
		      OutputStream os = socket.getOutputStream();
		      
		      os.write(mybytearray,0,mybytearray.length);

		      os.flush();
		      
		      fis.close();
		      bis.close();
		      os.close();
		      
		      socket = new Socket("192.168.1.53", 8889);

				 
			 dataInputStream = new DataInputStream(socket.getInputStream());
			 recd_msg=dataInputStream.readUTF();
			 opt= Integer.parseInt(recd_msg);
			 
		     socket.close();

		     Intent i;
		     if(opt == 13)
		     {
			    	i = new Intent(this, Profile.class);
			    	startActivity(i);
			    	finish();
		     }
		     if(opt == 14)
		     {
			    	i = new Intent(this, CaughtAgain.class);
			    	startActivity(i);
			    	finish();		    	  
		     }
		     if(opt == 15)
		     {
			    	i = new Intent(this, Fresher.class);
			    	startActivity(i);
			    	finish();		    	  
		     }
         
		}
		else
		{
		    Intent intent = getIntent();
		    finish();
		    startActivity(intent);
		   Toast t= Toast.makeText(MainActivity.this,"Connection lost. Activity restored.",Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			t.show();
		}
			
		}
		catch (UnknownHostException e) 
		{
			
			e.printStackTrace();
			
			
			
		}
		catch (IOException e) 
		{
			
			e.printStackTrace();
			
			
			
		}
		
		finally {
			 if( socket!= null)
			 {
				 try 
				 {      
					 socket.close();
				 }
				 
				 catch (IOException e) 
				 {
					 e.printStackTrace(); 
					 
					 
				 }
				 
			 }
			 
			 if( dataInputStream!= null){
				 
				 try {
					 
					 dataInputStream.close();
					 
					  }
				 catch (IOException e) {
					 
					 e.printStackTrace();
					 
					  }
				 
				 
			 }
			 
			 if( dataOutputStream!= null){
				 
				 try 	{
					 
					 dataOutputStream.close();
					 
				 		}
				 
				 catch (IOException e) {
					 
					 e.printStackTrace();
				 	}
				 
				 
			 	}
				 
				 

		}
	}

}



