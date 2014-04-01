/*        Copyright 2014- Anteprocess Enterprise   
*/


package com.drawitkid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;






import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
    
	MediaPlayer media;
	SoundPool sp;
	int yourSound =0;
protected static final int RESULT_SPEECH=1;
private	DrawingView drawView;

private ImageButton currPaint,drawBtn,resizer,newBtn,saveBtn,eraseBtn;

private float smallBrush,mediumBrush,largeBrush;

SeekBar seek,r2,g2,b2;
TextView t ;
Button b;
int seekR,seekG,seekB;
int value;
int color;
TextToSpeech talk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		//lets make a godly color set
	
		
		
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		 yourSound = sp.load(this, R.raw.b15, 1);
		 
		drawView = (DrawingView)findViewById(R.id.drawing);//reference it from the layout
		//we want to retrieve the first paint color button in the pallete area
		//the first retrieve the linearlayout itis contained within
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
		//get the first button and store it as the instance variable
		currPaint =(ImageButton)paintLayout.getChildAt(0);
		//we will use a different drawable image on the btn to show that it is currently selected 
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		
		newBtn = (ImageButton)findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);
		
		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);
		
		
		saveBtn = (ImageButton)findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		//for the dialog btn
		//resizer =(ImageButton)findViewById(R.id.im);
		//resizer.setOnClickListener(this);
		
	
		drawBtn=(ImageButton)findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);
		drawView.setBrushSize(mediumBrush);
	}

	
	public void paintClicked(View view){
		//use chosen color
		if(view!=currPaint){
			//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			//Now update the UI to reflect the new chosen paint and set the previous one back to normal:
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
			
		}
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		MenuInflater menuinflate = getMenuInflater();
		menuinflate.inflate(R.menu.main, menu);
		
		return true;
	}
	
	//after the onCreateOptionsMenu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.commando:
			//new page
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("Create new canvas");
			newDialog.setMessage("Create new canvas ? [You will loose your current data]");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			    	
			        drawView.startNew();
			        dialog.dismiss();
			    }
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			newDialog.show();
			break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	//getting the result from the onOptionsItemSelected
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		//getting the result here
		case RESULT_SPEECH:
		{
		if(resultCode==RESULT_OK&&null!=data){
			ArrayList<String>text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String intoColor = text.get(0);
			
			drawView.setColor(colorBack(intoColor));
		}else{
			
		}
			
		}
		break;
		
		}
		
		
	}
	
	
	

	@Override
	public void onClick(View v) {
		
		// respond to clicks
		//inside the method,check for clicks on the drawing button:
		if(v.getId()==R.id.draw_btn){
			//draw button clicked
			//create the dialog and set the title
			final Dialog brushDialog = new Dialog(this);
			
			brushDialog.setTitle("Change your background");
			//I'm going to define the dialog in the xml ok!? new file in res/layout
			//naming it brush_chooser.xml
			//step4. you can now set the layout
			brushDialog.setContentView(R.layout.brush_chooser);
			//if you click the small button
			r2=(SeekBar)brushDialog.findViewById(R.id.R);
			g2=(SeekBar)brushDialog.findViewById(R.id.G);
			b2=(SeekBar)brushDialog.findViewById(R.id.B);
			
			r2.setOnSeekBarChangeListener(this);
			g2.setOnSeekBarChangeListener(this);
			b2.setOnSeekBarChangeListener(this);
			
		
	  
			//show the dialog
			brushDialog.show();
			Button act = (Button)brushDialog.findViewById(R.id.rgbSet);
			act.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					color = 0xff000000+seekR*0x10000+seekG*0x100+seekB;
					drawView.setBackgroundColor(color);
					brushDialog.dismiss();
				}
			});
		}else if(v.getId()==R.id.save_btn){
			//save the drawings
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Wanna save your drawing to the Photogallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        //save drawing
			    	drawView.setDrawingCacheEnabled(true);
			    	//the stuff that you can actually save
			    	String imgSaved = MediaStore.Images.Media.insertImage(
			    		    getContentResolver(), drawView.getDrawingCache(),
			    		    UUID.randomUUID().toString()+".png", "drawing");
			    	if(imgSaved!=null){
			    	    Toast savedToast = Toast.makeText(getApplicationContext(), 
			    	        "Image saved to the photoGallery", Toast.LENGTH_SHORT);
			    	    savedToast.show();
			    	}
			    	else{
			    	    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
			    	        "Failed to save image", Toast.LENGTH_SHORT);
			    	    unsavedToast.show();
			    	}
			    	drawView.destroyDrawingCache();
			    }
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			saveDialog.show();
			
			
		}else if(v.getId()==R.id.new_btn){
			//voice command
			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
			try{//result_speech is like a serial number.
				startActivityForResult(i,RESULT_SPEECH);
				
				
			}catch(Exception e){
				
			}	
			
			
		}else if(v.getId()==R.id.erase_btn){
			sp.play(yourSound,1,1,0,0,1);
			//erase button
			
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.erazor);
			
			Button sm = (Button)brushDialog.findViewById(R.id.esmall);
			sm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
			        drawView.setBrushSize(smallBrush);
			      
			        brushDialog.dismiss();
					
				}
			});
			
			Button mm = (Button)brushDialog.findViewById(R.id.emedium);
			mm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
			        drawView.setBrushSize(mediumBrush);
			       
			        brushDialog.dismiss();
					
				}
			});
			
			
			Button lm = (Button)brushDialog.findViewById(R.id.elarge);
			lm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
			        drawView.setBrushSize(largeBrush);
			
			        brushDialog.dismiss();
					
				}
			});
			brushDialog.show();
		}
		
		
	}
	

	
	public void Supper(View v){
		//method that resizes 
		sp.play(yourSound,1,1,0,0,1);
		final Dialog si = new Dialog(this);
		si.setTitle("Set the size");
	
		si.setContentView(R.layout.sizer);
		si.setCancelable(true);
		si.show();
		
		
		 seek = (SeekBar)si.findViewById(R.id.seekBar1);
		t = (TextView)si.findViewById(R.id.sizerText);
		b =(Button)si.findViewById(R.id.change_btn);
		//initialise the text with 0
		
		t.setText(seek.getProgress()+"px");
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress =-10;
			public void onStopTrackingTouch(SeekBar seekBar) {
				//when stop do something 
				t.setText(seekBar.getProgress()+"px");
				   b.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(v.getId()==R.id.change_btn){
								drawView.setBrushSize(progress);
						        drawView.setLastBrushSize(progress);
						        si.dismiss();
							}
						}
					});
					
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				//do something in here
				
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				//
				progress = progressValue;
				
			}
		});

	
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		seekR= r2.getProgress();
		seekG=g2.getProgress();
		seekB=b2.getProgress();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
	public void playSound(View v){
	    if(yourSound != 0)
	        sp.play(yourSound, 1, 1, 0,0, 1);
	}
	

	//checks the voice and returns the color
    public String colorBack(String c){
    	
    	
    	//  HashMap<String, String>colorset = new HashMap<String,String>();
    	  String ChosenOne = "#000000";
    	  
  	      String blue = "#0000ff";
  	      String lightblue="#00bfff" ;
  	      String green ="#00ff00";
  	      String lightgreen ="#00ff7f";
  	      String purple ="#4b0082";
  	      String red = "#dc143c";
  	      String pink ="#ee82ee";
  	      String yellow ="#ffff00";
  	      String orange ="#ffa500";
  	      String brown ="#8B4726";
  	      String black = "#000000";
  	      String white ="#FFFFFF";
  	      String gray ="#8B8989";
  	      String darkblue ="#27408B";
  	        String magenta ="#FF00FF";
  	        
  	        
  	      if(c.equals("blue")){
  	    	  ChosenOne=blue;
  	    	Toast t = Toast.makeText(getBaseContext(), "Set blue",Toast.LENGTH_SHORT);t.show();
  	      } if(c.equals("red")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set red",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=red;
  	      } if(c.equals("brown")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set brown",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=brown;
  	      }if(c.equals("green"))
  	      {Toast t = Toast.makeText(getBaseContext(), "Set green",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=green;
  	      }if(c.equals("yellow")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set yellow",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=yellow;
  	      }if(c.equals("gray")){
  	    	  ChosenOne=gray;
  	    	Toast t = Toast.makeText(getBaseContext(), "Set gray",Toast.LENGTH_SHORT);t.show();
  	      }if(c.equals("orange")){
  	    	  ChosenOne=orange;
  	    	Toast t = Toast.makeText(getBaseContext(), "Set Orange",Toast.LENGTH_SHORT);t.show();
  	      }if(c.equals("pink")){
  	    	  ChosenOne=pink;
  	    	Toast t = Toast.makeText(getBaseContext(), "Set pink",Toast.LENGTH_SHORT);t.show();
  	      }if(c.equals("purple")){
  	    	  ChosenOne=purple;
  	    	Toast t = Toast.makeText(getBaseContext(), "Set purple",Toast.LENGTH_SHORT);t.show();
  	      }if(c.equals("light blue")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set lightblue",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=lightblue;
  	      }if(c.equals("magenta")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set magenta",Toast.LENGTH_SHORT);t.show();
  	    	  ChosenOne=magenta;
  	      }if(c.equals("dark blue")){
  	    	Toast t = Toast.makeText(getBaseContext(), "Set darkblue",Toast.LENGTH_SHORT);t.show();
  	    	ChosenOne=darkblue;
  	      }
  	      
  	      
  	      
  	  return ChosenOne;
    }


    
    
		
}









/*
 * 
 * 
 * ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(smallBrush);
			        drawView.setLastBrushSize(smallBrush);
			        brushDialog.dismiss();
			    }
			});
			//if you click the medium button
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(mediumBrush);
			        drawView.setLastBrushSize(mediumBrush);
			        brushDialog.dismiss();
			    }
			});
			 //if you click the large button
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(largeBrush);
			        drawView.setLastBrushSize(largeBrush);
			        brushDialog.dismiss();
			    }
			});
 * 
 */


/*
 * 
 * <?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.drawitkid"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk
        android:minSdkVersion="8"
        android:targetSdkVersion="18" />

    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
        <activity
            android:name="com.drawitkid.MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name="com.drawitkid.Splash"
            android:label="@string/title_activity_splash" >
        </activity>
    </application>

</manifest>

 * 
 * 
 * 
 * 
 */


