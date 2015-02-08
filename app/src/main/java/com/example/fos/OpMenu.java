package com.example.fos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class OpMenu extends Activity implements OnClickListener {
	Button fing,newEntry;
	ImageView logout_btn;
	Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_op_menu);

		fing=(Button)findViewById(R.id.finger);
		logout_btn=(ImageView)findViewById(R.id.logout);
		newEntry=(Button)findViewById(R.id.newEntry);
		

		fing.setOnClickListener(this);
		newEntry.setOnClickListener(this);
		logout_btn.setOnClickListener(this);
		bmp=BitmapFactory.decodeFile("/mnt/sdcard/logout.png");
		logout_btn.setImageBitmap(bmp);
	
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.op_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){

		case R.id.finger:
			
			Intent fingerop=new Intent(this, MainActivity.class);
			startActivity(fingerop);
			break;
		
		case R.id.newEntry:
			fingerop = new Intent(this,TextEntry.class);
			startActivity(fingerop);
			break;

			
		case R.id.logout:
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle("Confirm Logout");
		    builder.setMessage("Are you sure you want to logout?");

		    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		        

				

				public void onClick(DialogInterface dialog, int which) {
		            // Do nothing but close the dialog
		        	
					goBackToLogin();
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
		    break;
			
	}
	
	}
	
	public void goBackToLogin()
	{
		Intent intent_logout=new Intent(this, Login.class);
		startActivity(intent_logout);
		finish();
	}
	
	
	
	@Override
	public void onBackPressed() {
		Toast t=Toast.makeText(OpMenu.this,"Logout First...",Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		t.show();
	}  
	
	

}
