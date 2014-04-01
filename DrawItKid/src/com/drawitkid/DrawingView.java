/*        Copyright 2014- Anteprocess Enterprise   
*/


package com.drawitkid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
public class DrawingView extends View {

	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color  0xFF660000
	private int paintColor = 0xFF111111;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	private float brushSize, lastBrushSize;
	//erasor
	private boolean erase = false;
	
	public DrawingView(Context context,AttributeSet attrs) {
		super(context,attrs);
		setupDrawing();
		
	}
	public void startNew(){
		//method to create a new page
	    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	}

	private void setupDrawing() {
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		// get drawing area setup for interaction
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// view given size
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		
	}
	
	public void setErase(boolean isErase){
		//set erase true or false      
		erase = isErase;
		
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
		
		}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// draw view
		//super.onDraw(canvas);
		canvas.drawBitmap(canvasBitmap, 0,0,canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// detect user touch
		float touchX = event.getX();
		float touchY = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		invalidate();//invalidate will cause the onDraw method to execute
		return true;
	}
	//method to set the color 
	public void setColor(String newColor){
		//set color     
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
		
		}
	//add the method to set the brush size
	public void setBrushSize(float newSize){
		//update size
		//Inside the method, update the brush size with the passed value:
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
			    newSize, getResources().getDisplayMetrics());
			brushSize=pixelAmount;
			drawPaint.setStrokeWidth(brushSize);
	}
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
	    return lastBrushSize;
	}

}
