package com.geniteam.lifetabs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.interfaces.RangeSettingCallback;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;

public class MyCustomView extends View{

	float x1=80;
    float y1=30;
    
    float x2=80;
    float y2=150;
    
    /*int min = 75;
    int max = 180;
    int minLimit =  45;
    int maxLimit  = 350;*/
    
    float calcMax=180;
    float calcMin=75;
    
    
  /*  float yPrev1=40;
    float yPrev2=80;*/
    
    int totalHeight=0;
    boolean isRangeSet =false;
    RangeSettingCallback mCallback;
    LifeTabPrefManager mPrefManager;
    
    
	public MyCustomView(Context context,LifeTabPrefManager pManager) {
		super(context);
        mPrefManager = pManager;
		// TODO Auto-generated constructor stub
	}
	public MyCustomView(Context context, AttributeSet attrs){
	    super(context, attrs);
	 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		 super.onDraw(canvas);
		 
		Bitmap highline  =  BitmapFactory.decodeResource(getResources(), R.drawable.high_line);
        Bitmap  lowline  =  BitmapFactory.decodeResource(getResources(), R.drawable.low_line);
        Bitmap  shadedArea  =  BitmapFactory.decodeResource(getResources(), R.drawable.shaded_bar);

        Bitmap resizedBmphigh =  Bitmap.createScaledBitmap(highline, canvas.getWidth()-(int)(canvas.getWidth() * 0.02)+2 , highline.getHeight(), false);
        Bitmap resizedBmplow  =  Bitmap.createScaledBitmap(lowline,  canvas.getWidth()-(int)(canvas.getWidth() * 0.02)+2 , lowline.getHeight(), false);
		/* Bitmap resizedBmphigh =  Bitmap.createScaledBitmap(highline, canvas.getWidth()-highline.getHeight()/2, highline.getHeight(), false);
         Bitmap resizedBmplow  =  Bitmap.createScaledBitmap(lowline, canvas.getWidth()-lowline.getHeight()/2 , lowline.getHeight(), false);
*/
         Paint paint=new Paint();
	        // Use Color.parseColor to define HTML colors
	        paint.setColor(Color.parseColor("#B40111"));
	        paint.setTextSize(  (float)(canvas.getWidth()*0.06));

            if((int)y1 > 0){
                Bitmap resizedshader1= Bitmap.createScaledBitmap(shadedArea, canvas.getWidth()-highline.getHeight(), (int)y1 +(highline.getHeight()/2) , false);
                canvas.drawBitmap(resizedshader1, highline.getHeight()/2,10, paint);
            }

            if((int)(canvas.getHeight()-y2) > 0){   //15
                Bitmap resizedshader2= Bitmap.createScaledBitmap(shadedArea, canvas.getWidth()-highline.getHeight(), (int)(canvas.getHeight()-y2), false);
                canvas.drawBitmap(resizedshader2, highline.getHeight()/2,y2 - lowline.getHeight()/2, paint);
                /*canvas.drawBitmap(resizedshader2, 8,y2 - lowline.getHeight()/2, paint);*/
                            //8
            }


            canvas.drawText((int)calcMax+"", 20, y1-5, paint);
            canvas.drawBitmap(resizedBmphigh,(int)(canvas.getWidth() * 0.01), y1, paint);

            canvas.drawText((int)calcMin+"", 20, y2-resizedBmplow.getHeight()-5, paint);
            canvas.drawBitmap(resizedBmplow,(int)(canvas.getWidth() * 0.01), y2-resizedBmplow.getHeight(), paint);
	       /* canvas.drawText(Math.round(calcMax)-1+"", 20, y1-5, paint);
	        canvas.drawBitmap(resizedBmphigh,7, y1, paint);
	        
	        canvas.drawText(Math.round(calcMin)-1+"", 20, y2-resizedBmplow.getHeight()-5, paint);
	        canvas.drawBitmap(resizedBmplow,7, y2-resizedBmplow.getHeight(), paint);*/



	        totalHeight = canvas.getHeight();
            if(!isRangeSet){
             setRanges(mPrefManager.getSharedPreferenceInt(LifeTabPrefManager.SharedPreferencesNames.MIN_VALUE_SET),mPrefManager.getSharedPreferenceInt(LifeTabPrefManager.SharedPreferencesNames.MAX_VALUE_SET));
            }
	     //   canvas.drawCircle(x,y, radius, paint);
	        invalidate();
	}
    public void setRanges(float minRange , float maxRange){

        float valMin  = 395 - minRange ;
        float yMin =  ((valMin - 45)/305) * totalHeight +1 ;

        float valMax  = 395 - maxRange ;
        float yMax =  ((valMax - 45)/305) * totalHeight +1;

        y2 = yMin ;
        y1 = yMax ;


        calcMin = (y2-1)/totalHeight * 305 + 45;
        calcMin = 395 - calcMin;

        calcMax = (y1-1)/totalHeight * 305 + 45;
        calcMax = 395 - calcMax;

        mCallback.onSuccess(calcMin,calcMax);
        isRangeSet=true;
    }
	
	public float diff(float a, float b){
		float x= a-b;
		if(x<0){
			x = x*-1;
		}
		return x;
	}

    public void getRangeValues(RangeSettingCallback pCallback){
        mCallback = pCallback;
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		
		//yPrev1 = y1;
		//yPrev2 = y2;
		
		/*if(diff(y1 , y2) < 20){
			
		}else{*/
		
		if(event.getY() > totalHeight || event.getY()<0 )
		{
			return false;
		}
		
		if(diff(y1 , event.getY()) < 100){ //10  //40
			
			/*if(y1 < event.getY()){
				max -- ;
			}else{
				max ++;
			}*/
			
			calcMax = (event.getY()-1)/totalHeight * 305 + 45;
			calcMax = 395 - calcMax;
			
			
			if(diff(y1 , y2) < 110){  //30 //50
				if(y1 >= event.getY()){
					y1= event.getY();
				}
			}else{
				y1= event.getY();
			}
			
			
		}
		
		if(diff(y2 , event.getY()) < 100){  //10
			
			/*if(y2 < event.getY()){
				
				min--;
			}else{
				min++;
			}*/
			
			calcMin = (event.getY()-1)/totalHeight * 305 + 45;
			calcMin = 395 - calcMin;
			
			if(diff(y1 , y2) < 110){ //30
				if(y2 <= event.getY()){
					y2= event.getY();
				}
			}else{
				y2= event.getY();
			}
			
		}
		
	//	}
		
		/*y1= event.getY();
		y2= event.getY();
		
		if(diff(y1,yPrev1) < 10){
				
			if(yPrev1 < y1){
				min--;
			}else{
				min++;
			}
		}
		
		if(diff(y2,yPrev2) < 10){
			if(yPrev2 < y2){
				max--;
			}else{
				max++;
			}
		}*/
		
		
		
		
		/*if(yPrev1 < y1){
			min--;
		}else{
			min++;
		}
		
		if(yPrev2 < y2){
			max--;
		}else{
			max++;
		}*/
		
		
		
		
		
		
       /* boolean b = mViewport.onTouchEvent(event);
        boolean a = super.onTouchEvent(event);*/

        //cList.remove(0);
      //  setImage(event.getX(),event.getY());

        // is it a click?
       /* if (mTapDetector.onTouchEvent(event)) {
            for (Series s : mSeries) {
                s.onTap(event.getX(), event.getY());
            }
            if (mSecondScale != null) {
                for (Series s : mSecondScale.getSeries()) {
                    s.onTap(event.getX(), event.getY());
                }
            }
        }

        return b || a;*/

        mCallback.onSuccess(calcMin,calcMax);

		return true;
    }
	

}
