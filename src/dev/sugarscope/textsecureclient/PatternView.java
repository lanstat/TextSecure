package dev.sugarscope.textsecureclient;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi")
public class PatternView extends View{
	Circle[] mDots;
	int width = 200;
	int height = 200;
	ArrayList<Integer> mSelected;

	public PatternView(Context context) {
		super(context);
		mSelected = new ArrayList<Integer>();
	}
	
	
	
	public PatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mSelected = new ArrayList<Integer>();
	}



	public void init(Display display){
		mDots = new Circle[9];
		Point size = new Point();
		display.getSize(size);
		
		int dX = (int)(size.x*.5f - width*.5f);
		int dY = (int)((size.y-20)*.5f - height*.5f);
		int pX = width/3;
		int pY = height/3;
		int iter = 0;
		final int RADIUS = 20;
		for(int i=0; i<3; i++){
			for (int j = 0; j < 3; j++) {
				int x = (int)((pX* i) + (pX*.5f));
				int y = (int)((pY* j) + (pY*.5f));
				mDots[iter] = new Circle(dX+x, dY+y, RADIUS);
				iter++;
			}
		}
	}
	
	public ArrayList<Integer> getSelected(){
		return mSelected;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(mDots!=null)
		for (Circle circle : mDots) {
			circle.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < mDots.length; i++) {
				if(mDots[i].isCollide(x, y)){
					mSelected.add(i);
					mDots[i].setSelected();
					invalidate();
					break;
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public class Circle{
		public int x;
		public int y;
		public int radius;
		Paint paint;
		
		public Circle(int x, int y, int radius) {
			super();
			this.x = x;
			this.y = y;
			this.radius = radius;
			paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setAntiAlias(true);
		}
		
		public void setSelected(){
			paint.setColor(Color.RED);
		}
		
		public void draw(Canvas canvas){
			canvas.drawCircle(x, y, radius, paint);
		}
		
		public boolean isCollide(float pX, float pY){
			if(x<pX && x+radius*2>pX && y <pY && y+radius*2 >pY){
				return true;
			}
			return false;
		}
	}
}
