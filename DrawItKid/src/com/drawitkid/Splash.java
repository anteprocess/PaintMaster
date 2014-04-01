
/*        Copyright 2014- Anteprocess Enterprise   
*/


package com.drawitkid;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Splash extends Activity {
	MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		 mp = MediaPlayer.create(Splash.this, R.raw.b17); mp.start();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(Splash.this,MainActivity.class);
				startActivity(i);
				
				 
				finish();
				
			}
		},2000);
		
		
		
	}

	
	


}
