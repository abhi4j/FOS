package com.example.fos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
	Button login;
	EditText pass1, user1;
	public static String pass2, user2;
	public static int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login=(Button)findViewById(R.id.Log);
		pass1=(EditText)findViewById(R.id.pass);
		user1=(EditText)findViewById(R.id.uname);
		login.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*Socket socket = null;
		DataOutputStream dos = null; 
		DataInputStream dis = null;*/
		
		pass2= pass1.getText().toString().trim();
		user2= user1.getText().toString().trim();
		/*try
		{
			socket = new Socket("192.168.1.53", 8889);
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("28");
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(user2);
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(pass2);
			
			dis = new DataInputStream(socket.getInputStream());
			String str= dis.readUTF();
			num=Integer.parseInt(str);
			socket.close();
			
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
			 
			 if( dis!= null){
				 
				 try {
					 
					 dis.close();
					 
					  }
				 catch (IOException e) {
					 
					 e.printStackTrace();
					 
					  }
				 
				 
			 }
			 
			 if( dos!= null){
				 
				 try 	{
					 
					 dos.close();
					 
				 		}
				 
				 catch (IOException e) {
					 
					 e.printStackTrace();
				 	}
				 
				 
			 	}
				 
				 

		}// Finally ends*/
		if(user2.equals("Abhishek") && pass2.equals("abhishek"))
		{
		
		Intent i=new Intent(this,OpMenu.class);
		startActivity(i);
		finish();
    	Toast t =Toast.makeText(Login.this,"Logged in....",Toast.LENGTH_LONG);
    	t.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    	t.show();
		}
		else
		{
		    Intent intent = getIntent();
		    finish();
		    startActivity(intent);
	    	Toast t =Toast.makeText(Login.this,"Enter Correct Username and Password...",Toast.LENGTH_LONG);	
	    	t.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	    	t.show();
		
		
		}
	}// Main end

	
	
	public void onBackPressed() {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Confirm Exit");
	    builder.setMessage("Are you sure you want to exit the app?");

	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

	        

			

			public void onClick(DialogInterface dialog, int which) {
	            // Do nothing but close the dialog
	        	
				finish();
				dialog.dismiss();
	            
	        }

	    });

	    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // Do nothing
	            dialog.dismiss();
	        }
	    });

	    AlertDialog alert = builder.create();
	    alert.show();
		   
		   
	} // Back Press Finished

}







/*    <uses-sdk
android:minSdkVersion="1"  android:targetSdkVersion="17"
/> */
