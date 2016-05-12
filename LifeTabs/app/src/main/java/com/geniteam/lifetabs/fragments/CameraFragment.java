package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.utils.LifeTabUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
@SuppressWarnings("deprecation")


public class CameraFragment extends LifeTabCustomFragment implements SurfaceHolder.Callback,View.OnClickListener {
    MainActivity mContext;
    View view;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    Camera.Parameters param;
    int imgH ;
    int imgW ;
    int centerX ;
    int centerY ;
    Bitmap bitmap;
    int colorPointToTest=0;
    int whiteX,whiteY;
    double [] distanceList = new double[10];
    double [] percentageList = new double[10];
    int [] segmentValues = new int[]{100,150,200,250,300,350,400,0,25,50};
    int [] pointsRGB = new int[10];
    int calculatedValue;
    int centerRed   = 0;
    int centerGreen = 0;
    int centerBlue  = 0;
    ImageView flashButton;
    Bitmap newBmp;
    int cxo =0 ;
    int cyo =0 ;

    //BD:
    String currentImageName = null;

    public void updateImageName(String newName){
        updateImageName(newName,false);
    }

    public void updateImageName(String newName, boolean prepend){
        File sdcard = Environment.getExternalStorageDirectory();
        File from = new File(sdcard,currentImageName);

        if(prepend == false){
            File tof = new File(sdcard,newName);
            currentImageName = newName;
            from.renameTo(tof);
        }else{
            File tof = new File(sdcard,newName + currentImageName);
            currentImageName = newName + currentImageName;
            from.renameTo(tof);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment cameraFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.CAMERA);

        if(cameraFragment == null){
            cameraFragment = new CameraFragment();
        }
        return cameraFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.camera_layout, container, false);
        setBasicContents(view);
        return view;
    }

    /**
     * setting basic contents
     * @param v
     */
    public void setBasicContents(View v){
        mContext.toolbar.setVisibility(View.GONE);
        v.findViewById(R.id.imv_home).setOnClickListener(this);
        v.findViewById(R.id.imv_camera).setOnClickListener(this);
        v.findViewById(R.id.imv_flash).setOnClickListener(this);

        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        flashButton = (ImageView)v.findViewById(R.id.imv_flash);
        surfaceView.setOnClickListener(this);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                String path = null;
                try {
                    String filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    path = "/sdcard/" + filename;
                    currentImageName = filename;
                    outStream = new FileOutputStream(path);

                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(mContext, "Picture Saved", Toast.LENGTH_LONG).show();
                    refreshCamera();
                    try {
                        resultsProcessing(path);
                    }catch(Exception e){
                        Toast.makeText(mContext, "Could not position strip from photos.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

        };
    }

    /**
     * goto home screen
     */
    public void goToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
    }

    /**
     * goto results screen
     * @param date
     * @param time
     * @param value
     * @param unit
     */
    public void goToResultsSection(String date, String time, String value ,String unit){

        ReadingBO item = new ReadingBO(date,time.toUpperCase(),value,unit);
        Bundle args = new Bundle();
        args.putSerializable("sugar_value",item);
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment resultFragment= ResultFragment.newInstance(fragmentManager);
        mContext.currFragment = resultFragment;
        resultFragment.setArguments(args);
        mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, resultFragment, LifeTabsConstents.RESULT).commit();
    }
    /**
     * processing start on captured image
     * @param path
     */
    public void resultsProcessing(String path){
        File imageFile= new File(path);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imgH = bitmap.getHeight();
        imgW = bitmap.getWidth();

        //BD: getting center point of image
        centerX = imgW/2;
        centerY = imgH/2;

        //BD: pulling single center pixel to get color value, and assuming color is in center
        //this doesnt look like its actually used anywhere
        int centerRgb = bitmap.getPixel(centerX, centerY);
        centerRed   = (centerRgb >> 16) & 0xFF;
        centerGreen = (centerRgb >> 8) & 0xFF;
        centerBlue  = centerRgb & 0xFF;
        /**
         * to find center of circle from image
         */


        //BD: finding the center of the strip and setting the global cxo and xyo. Doing this by
        //looking for circles on the strip - BAD!
            if (!OpenCVLoader.initDebug()) {
                Log.e("TAG", "Cannot connect to OpenCV Manager");
            } else {
                detectCircle(bitmap);
            }

        //BD: globally set from above call - should (assumed) give us the center of our color change area
        centerX  = cxo;
        centerY =  cyo;
        updateImageName("CtrClrX"+String.valueOf(cxo)+"Y"+String.valueOf(cyo)+"_", true);

        int point1X=0,point1Y=0,point2X=0,point2Y=0,point3X=0,point3Y=0,point4X=0,point4Y=0;

        /**
         * left,right,top and bottom 4-points to check position
         */

        //BD: pulling 4 random points from the image based off of our assumed center AND using
        //pixel values which may change based on camera MP count - BAD!
        //This appears to be for checking the white reactive area
        point1X = centerX;
        point1Y = centerY-186;
        updateImageName("P1X"+String.valueOf(point1X)+"Y"+String.valueOf(point1Y)+"_", true);


        point2X = centerX+186;
        point2Y = centerY;
        updateImageName("P2X"+String.valueOf(point2X)+"Y"+String.valueOf(point2Y)+"_", true);

        point3X = centerX-186;
        point3Y = centerY;
        updateImageName("P3X"+String.valueOf(point3X)+"Y"+String.valueOf(point3Y)+"_", true);

        point4X = centerX;
        point4Y = centerY+186;
        updateImageName("P4X"+String.valueOf(point4X)+"Y"+String.valueOf(point4Y)+"_", true);

        //BD: getting a white point based on our other terrible assumptions
        // we add 50 to the Y val in the test func to locate the white control
        //otherwise this point is considered the white reactive area
        whiteX = point1X-100;
        whiteY = point1Y-30;
        updateImageName("WtReaX"+String.valueOf(whiteX)+"Y"+String.valueOf(whiteY)+"_WtCtrX"+String.valueOf(whiteX)+"Y"+String.valueOf(whiteY+50)+"_", true);

        //BD: Pulling ONE pixel from each of the white reactive area assumed points
        int rgbPoint1 = bitmap.getPixel(point1X, point1Y);
        int rgbPoint2 = bitmap.getPixel(point2X, point2Y);
        int rgbPoint3 = bitmap.getPixel(point3X, point3Y);
        int rgbPoint4 = bitmap.getPixel(point4X, point4Y);


        //BD: if color matches (based on that function) by >90% we have points that we assume are all white reactive area
        //Is our assumed white reactive area whiteX and whiteY not tested?
            if (    findColorDifference(rgbPoint1, rgbPoint2) == true &&
                    findColorDifference(rgbPoint1, rgbPoint3) == true &&
                    findColorDifference(rgbPoint1, rgbPoint4) == true) {

                //BD: Tests are again performed off of only singular points
                if (stripAgeTest()) {

                    //BD: What is this? This sets 255 as radius every time.
                    double radius = Math.sqrt(Math.pow((centerX - (centerX)), 2) + Math.pow((centerY - centerY + 255), 2));
                    updateImageName("Radi"+String.valueOf(radius), true);

                    //BD: our center color area is only one point. and it is centerX+20 and centerY
                    getColorsPointFromBoundry(radius, centerX, centerY);
                    updateImageName("CompCtrX"+String.valueOf(centerX+20)+"Y"+String.valueOf(centerY)+"_", true);
                } else {
                    Toast.makeText(mContext, "Unable to accurately check white control", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(mContext, "Could not position strip from photo", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * performing strip age test
     * @return
     */
    public boolean stripAgeTest(){

        // white control area

        int controlY = whiteY+50;
        int controlX = whiteX ;
        // white reactive area
        int reactiveY = whiteY;
        int reactiveX = whiteX;

        int whiteControl = bitmap.getPixel(controlX,controlY);
        int whiteReactive = bitmap.getPixel(reactiveX,reactiveY);

        int red1 =   (whiteControl >> 16) & 0xFF;
        int green1 = (whiteControl >> 8) & 0xFF;
        int blue1 =   whiteControl & 0xFF;

        int red2 =   (whiteReactive >> 16) & 0xFF;
        int green2 = (whiteReactive >> 8) & 0xFF;
        int blue2 =   whiteReactive & 0xFF;

        int whiteRed   = 255;
        int whiteGreen = 255;
        int whiteBlue  = 255;

        double whitedistance1 =Math.sqrt(  Math.pow(red1 - whiteRed, 2) + Math.pow(green1 - whiteGreen, 2) + Math.pow(blue1 - whiteBlue, 2) );
        double whitedistance2 =Math.sqrt(  Math.pow(red2 - whiteRed, 2) + Math.pow(green2 - whiteGreen, 2) + Math.pow(blue2 - whiteBlue, 2) );

        double whitePercentage1  = whitedistance1 /  Math.sqrt((255)^2+(255)^2+(255)^2);
        double whitePercentage2  = whitedistance2 /  Math.sqrt((255)^2+(255)^2+(255)^2);

        double distance =Math.sqrt(  Math.pow(red1 - red2, 2) + Math.pow(green1 - green2, 2) + Math.pow(blue1 - blue2, 2) );
        double percentage = distance / Math.sqrt((255)^2+(255)^2+(255)^2);
        percentage = 100 - percentage;

        if(percentage  > 95 && (100-whitePercentage1)> 80 && (100-whitePercentage2)> 80 )
        {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * getting colors from strip boundry
     * @param radius
     * @param originX
     * @param originY
     */
    public void getColorsPointFromBoundry(double radius,int originX , int originY) {

        int angle = 0;
        // Create a bitmap of the same size
         newBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // Create a canvas  for new bitmap
        Canvas c = new Canvas(newBmp);
        // Draw your old bitmap on it.
        c.drawBitmap(bitmap, 0, 0, new Paint());

        for(int i=0 ; i<10 ; i++){   //12

            int pX = (int) Math.round( radius * Math.cos(Math.toRadians(angle)) + originX );
            int pY = (int) Math.round( radius * Math.sin(Math.toRadians(angle)) + originY );

		    if(pX < originX ){
			    pX = pX + 25;
		    }else {
                pX = pX - 25;
            }

            if(pX < 0){
                pX=0;
            }
            if(pY<0){
                pY=0;
            }

            if(pX >= newBmp.getWidth() ){
                pX = newBmp.getWidth()-20;
            }
            if(pY >= newBmp.getHeight()){
                pY = newBmp.getHeight()-20;
            }

            int pRGB = bitmap.getPixel(pX, pY);

            pointsRGB[i] = pRGB;

            int red1 = (pRGB >> 16) & 0xFF;
            int green1 = (pRGB >> 8) & 0xFF;
            int blue1 = pRGB & 0xFF;

            angle = angle + 36;
        }


        OutputStream stream;
        try {
            stream = new FileOutputStream("/sdcard/test.jpg");
            newBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       matchColorFromArray(originX,originY);

    }

    /*public boolean getEnsure(){

        int count  = 0;
        int white = bitmap.getPixel(whiteX,whiteY);

        int red1 =   255;
        int green1 = 255;
        int blue1 =  255;

        for(int i=0 ; i<pointsRGB.length ; i++){

            int pRGB = pointsRGB[i];

            int red2 = (pRGB >> 16) & 0xFF;
            int green2 = (pRGB >> 8) & 0xFF;
            int blue2 = pRGB & 0xFF;

            double distance =Math.sqrt(  Math.pow(red1 - red2, 2) + Math.pow(green1 - green2, 2) + Math.pow(blue1 - blue2, 2) );
            double percentage = distance / Math.sqrt((255)^2+(255)^2+(255)^2);
            percentage = 100 - percentage;

            if(percentage > 90){
                count ++;
            }
        }

        if(count > 2){
            return false;
        }

        return true;
    }*/

    public float returnMatchPercent(int rgb1 , int rgb2){
        int red1   = (rgb1   >> 16) & 0xFF;
        int green1 = (rgb1   >> 8) & 0xFF;
        int blue1 =  rgb1   & 0xFF;

        int red2   = (rgb2   >> 16) & 0xFF;
        int green2 = (rgb2   >> 8) & 0xFF;
        int blue2  =  rgb2   & 0xFF;

        int diffRed   = Math.abs(red1   - red2);
        int diffGreen = Math.abs(green1 - green2);
        int diffBlue  = Math.abs(blue1  - blue2);

        float pctDiffRed   = (float)diffRed   / 255;
        float pctDiffGreen = (float)diffGreen / 255;
        float pctDiffBlue   = (float)diffBlue  / 255;

        float perc = (pctDiffRed + pctDiffGreen + pctDiffBlue) / 3 * 100 ;

        perc = 100 - perc;

        return perc;

    }

    public void matchColorFromArray(int originX , int originY){

        int cX,cY;

        cX =  originX + 20;
        cY =  originY;

        int colorToMatch  =  bitmap.getPixel(cX, cY);
        int red2   = (colorToMatch   >> 16) & 0xFF;
        int green2 = (colorToMatch   >> 8) & 0xFF;
        int blue2  =  colorToMatch   & 0xFF;

        for(int i=0 ; i< pointsRGB.length ; i++){

            int mRGB = pointsRGB[i];

            int red1   = (mRGB >> 16) & 0xFF;
            int green1 = (mRGB >> 8) & 0xFF;
            int blue1  =  mRGB & 0xFF;

            percentageList[i] = returnMatchPercent(mRGB , colorToMatch);
        }

        double [] result = LifeTabUtils.printTwoMaxNumbers(percentageList);

        double segment1Percentage = result[0];
        double segment2Percentage = result[1];

        int firstSegmentIndex  = getValueIndex(result[0]);
        int secondSegmentIndex = getValueIndex(result[1]);

        if(segment1Percentage > 90){

            calculatedValue = segmentValues[firstSegmentIndex];
            if(calculatedValue > 350 || calculatedValue <45){
                Toast.makeText(mContext,"Unable to accurately compare color strip colors",Toast.LENGTH_SHORT).show();
            }else {
                String datenTime = LifeTabUtils.getDataToSave();
                LifeTabUtils.writeToFile(datenTime, calculatedValue + "", segment1Percentage + "", mContext, "success");
                goToResultsSection(datenTime.split(" ")[0], datenTime.split(" ")[1] + " " + datenTime.split(" ")[2], calculatedValue + "", "mg/dL");
                updateImageName("Val"+String.valueOf(calculatedValue)+"_", true);
            }
        }

        if(segment1Percentage > 80 && segment2Percentage >80){

            firstSegmentIndex  = getValueIndex(result[0]);
            secondSegmentIndex = getValueIndex(result[1]);

            if(firstSegmentIndex + 1 == secondSegmentIndex || firstSegmentIndex - 1 == secondSegmentIndex ){
                calculatedValue = (int) getCalculatedValue(segmentValues[firstSegmentIndex],segmentValues[secondSegmentIndex],segment1Percentage,segment2Percentage);
                Log.d("Range is : ", calculatedValue + "");
                String datenTime = LifeTabUtils.getDataToSave();
                LifeTabUtils.writeToFile(datenTime ,calculatedValue+"" , segment1Percentage+"" ,mContext,"success");
                goToResultsSection(datenTime.split(" ")[0] ,datenTime.split(" ")[1]+" "+datenTime.split(" ")[2] ,calculatedValue+"" ,"mg/dL" );
            }else{

                calculatedValue = segmentValues[firstSegmentIndex];
                if(calculatedValue > 350 || calculatedValue <45){
                    Toast.makeText(mContext,"Unable to accurately compare color strip colors",Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("Range is : ", calculatedValue + "");
                    String datenTime = LifeTabUtils.getDataToSave();
                    LifeTabUtils.writeToFile(datenTime ,calculatedValue+"" , segment1Percentage+"" ,mContext,"success");
                    goToResultsSection(datenTime.split(" ")[0] ,datenTime.split(" ")[1]+" "+datenTime.split(" ")[2] ,calculatedValue+"" ,"mg/dL" );
                }

            }
        }
        else if(segment1Percentage > 80 && segment2Percentage < 80){
            calculatedValue= segmentValues[firstSegmentIndex];
            if(calculatedValue > 350 || calculatedValue <45){
                Toast.makeText(mContext,"Unable to accurately compare color strip colors",Toast.LENGTH_SHORT).show();
            }else{
                Log.d("Range is : ", calculatedValue + "");
                String datenTime = LifeTabUtils.getDataToSave();
                LifeTabUtils.writeToFile(datenTime ,calculatedValue+"" , segment1Percentage+"" ,mContext,"success");
                goToResultsSection(datenTime.split(" ")[0] ,datenTime.split(" ")[1]+" "+datenTime.split(" ")[2] ,calculatedValue+"" ,"mg/dL" );
            }

        }
        else if(segment1Percentage  < 80 && segment2Percentage  > 80){
            calculatedValue = segmentValues[secondSegmentIndex];
            if(calculatedValue > 350 || calculatedValue <45){
                Toast.makeText(mContext,"Unable to accurately compare color strip colors",Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("Range is : ", calculatedValue+ "");
                String datenTime = LifeTabUtils.getDataToSave();
                LifeTabUtils.writeToFile(datenTime ,calculatedValue+"" , segment2Percentage+"" ,mContext,"success");
                goToResultsSection(datenTime.split(" ")[0] ,datenTime.split(" ")[1]+" "+datenTime.split(" ")[2] ,calculatedValue+"" ,"mg/dL" );
            }

        }else{
            Toast.makeText(mContext,"Unable to accurately compare color strip colors",Toast.LENGTH_SHORT).show();
            String datenTime = LifeTabUtils.getDataToSave();
            LifeTabUtils.writeToFile(datenTime ,calculatedValue+"" , segment1Percentage+"" ,mContext,"fail");
            goToResultsSection(datenTime.split(" ")[0] ,datenTime.split(" ")[1]+" "+datenTime.split(" ")[2] ,0+"" ,"mg/dL" );

        }

    }

    public int getValueIndex(double value){
        for(int i=0 ; i< percentageList.length;i++){
            if(percentageList[i] == value){
                return i;
            }
        }
        return -1;
    }

    public int findBestMatchColor(){

        double minDistance = distanceList[0];
        int minIndex = 0;

        for(int a=1 ;a<distanceList.length ; a++){
            if(distanceList[a] < minDistance){
                minDistance = distanceList[a];
                minIndex = a;
            }
        }

        return pointsRGB[minIndex];

    }

    public boolean findColorDifference (int color1 , int color2){

        int red1 =   (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 =   color1 & 0xFF;

        int red2 =   (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 =   color2 & 0xFF;

        double distance =Math.sqrt(  Math.pow(red1 - red2, 2) + Math.pow(green1 - green2, 2) + Math.pow(blue1 - blue2, 2) );
        double percentage = distance / Math.sqrt((255)^2+(255)^2+(255)^2);
        percentage = 100 - percentage;

        if(percentage  > 90){
            return true;
        }
        else{
    	     return false;
        }
    }

    /*public void setOrigins(int originX, int originY , int totalX , int totalY){

        int startLimitX = 0;
        int endLimitX = 0;
        int startLimitY = 0;
        int endLimitY = 0;
        int xtoSet;
        int ytoSet;

        for(int a=originX ; a <totalX ; a++ ){

            if(pointsComparator(bitmap.getPixel(a, originY))){
                endLimitX = a-1;
                break;
            }
            else{
                continue;
            }

        }

        for(int a=originX ; a > 0 ; a-- ){


            if(pointsComparator(bitmap.getPixel(a, originY))){
                startLimitX = a+1;
                colorPointToTest = startLimitX - 40;
                break;
            }
            else{
                continue;
            }

        }

        xtoSet = Math.round((startLimitX + endLimitX)/2) ;

        for(int a=originY ; a <totalY ; a++ ){


            if(pointsComparator(bitmap.getPixel(xtoSet, a))){
                endLimitY = a-1;
                break;
            }
            else{
                continue;
            }

        }

        for(int a=originY ; a > 0 ; a-- ){


            if(pointsComparator(bitmap.getPixel(xtoSet, a))){
                startLimitY = a+1;
                break;
            }
            else{
                continue;
            }

        }

        ytoSet = Math.round((startLimitY + endLimitY)/2);

        if(xtoSet!=0){
            centerX = xtoSet;
        }
        if(ytoSet!=0){
            centerY = ytoSet;
        }


    }*/

    public double getCalculatedValue(int b , int c , double bPercentAge , double cPercentAge)
    {

        if(b > 350 || b < 45){
            Log.i("Range is : ", c + "");
            if(c>=45 && c<=350){
                return c;
            }else{
                return 0;
            }
        }

        if(c > 350 || c < 45){
            Log.i("Range is : ", b + "");
           if(b>=45 && b<=350){
               return b;
           }
            else{
               return 0;
           }
        }

        double difference = bPercentAge - cPercentAge;


        double x = 50 - difference;

        double value  =Math.round ( ((c - b) * (x/100)) + b );

        Log.i("Range is : ", value + "");
        return Math.round(value);
    }

    @Override
    public boolean onKeyDown() {
        goToHome();
        return true;
    }


    public void captureImage() throws IOException {
        //  camera.au
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        }

        catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        param = camera.getParameters();

        List<Camera.Size> sizes = param.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

        param.setPreviewSize(1602, 1080);
        param.setPictureSize(1280, 720);

        String s = param.getFlashMode();
        if(s!=null){
            List<String> flashModes = param.getSupportedFlashModes();

            if(flashModes!=null) {

                if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                } else {
                  //  param.setFlashMode(flashModes.get(0));
                }
            }
        }

        //checking if focus mode is available
        List<String> focusModes = param.getSupportedFocusModes();

        if(focusModes!=null) {

            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else {
               // param.setFocusMode(focusModes.get(0));
            }
        }

       try{
           camera.setParameters(param);
       }catch(Exception e){
            e.printStackTrace();
       }
       camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void toggleFlashLight(){

       if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
           if(param.getFlashMode() == Camera.Parameters.FLASH_MODE_OFF){
              // param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

               List<String> flashModes = param.getSupportedFlashModes();

               if(flashModes!=null) {

                   if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                       param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                   } else {
                      // param.setFlashMode(flashModes.get(0));
                   }
               }

               camera.setParameters(param);
               camera.startPreview();
           }
           else if(param.getFlashMode() != Camera.Parameters.FLASH_MODE_OFF){
               //param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
               List<String> flashModes = param.getSupportedFlashModes();

               if(flashModes!=null) {

                   if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                       param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                   } else {
                     //  param.setFlashMode(flashModes.get(0));
                   }
               }
               camera.setParameters(param);
               camera.startPreview();
           }
       }else{
           flashButton.setEnabled(false);
          // Toast.makeText(mContext,"Your device have no flash light!",Toast.LENGTH_SHORT).show();
       }
    }

    public void setCameraFocus(){

        if(camera == null){
            camera = Camera.open();
        }

        Camera.Parameters parameters = camera.getParameters();
        //checking if focus mode is available
        List<String> focusModes = parameters.getSupportedFocusModes();

        if(focusModes!=null) {

            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else {
               // parameters.setFocusMode(focusModes.get(0));
            }

        }
       // param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    /**
     * method to detect circle from image
     * @param bp
     */
    public void detectCircle(Bitmap bp){

        Bitmap inbmp = bp;

       /* convert bitmap to mat */
        Mat mat = new Mat(inbmp.getWidth(), inbmp.getHeight(),
                CvType.CV_8UC4); //CV_8UC1
        Mat grayMat = new Mat(inbmp.getWidth(), inbmp.getHeight(),
                CvType.CV_8UC4);

        Utils.bitmapToMat(inbmp, mat);

       /* convert to grayscale */
        int colorChannels = (mat.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
                : ((mat.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);

        Imgproc.cvtColor(mat, grayMat, colorChannels);

       /* reduce the noise so we avoid false circle detection */
        Imgproc.GaussianBlur(grayMat, grayMat, new Size(9, 9), 2, 2);

        // accumulator value
        double dp = 1.2d;
        // minimum distance between the center coordinates of detected circles in pixels
        double minDist = 100;

        // min and max radii (set these values as you desire)
        int minRadius = 0, maxRadius = 0;

        // param1 = gradient value used to handle edge detection
        // param2 = Accumulator threshold value for the
        // cv2.CV_HOUGH_GRADIENT method.
        // The smaller the threshold is, the more circles will be
        // detected (including false circles).
        // The larger the threshold is, the more circles will
        // potentially be returned.
        double param1 = 70, param2 = 72;

       /* create a Mat object to store the circles detected */
        Mat circles = new Mat(inbmp.getWidth(),
                inbmp.getHeight(), CvType.CV_8UC1);

       /* find the circle in the image */
        Imgproc.HoughCircles(grayMat, circles,
                Imgproc.CV_HOUGH_GRADIENT, dp, minDist, param1,
                param2, minRadius, maxRadius);

       /* get the number of circles detected */
        int numberOfCircles = (circles.rows() == 0) ? 0 : circles.cols();

        //  int minRad =0;
        numberOfCircles=1;

       /* draw the circles found on the image */
        for (int i=0; i<numberOfCircles; i++) {


       /* get the circle details, circleCoordinates[0, 1, 2] = (x,y,r)
        * (x,y) are the coordinates of the circle's center
        */
            double[] circleCoordinates = circles.get(0, i);


            int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];

            Point center = new Point(x, y);

            int radius = (int) circleCoordinates[2];

            Toast.makeText(mContext, x+" : "+y+"   "+radius, Toast.LENGTH_SHORT).show();

          /* circle's outline */
            Core.circle(mat, center, radius, new Scalar(0,
                    255, 0), 4);

           /* circle's center outline *//*
            Core.rectangle(mat, new Point(x - 5, y - 5),
                    new Point(x + 5, y + 5),
                    new Scalar(0, 128, 255), -1);

            Core.rectangle(mat, new Point(x , y + radius),
                    new Point(x , y + radius),
                    new Scalar(0, 128, 255), -1);*/


            //  if(radius > minRad) {
            cxo = x;
            cyo = y;

            //rad = radius;
            //    minRad = radius;
            //  }

        }
        Utils.matToBitmap(mat, inbmp);
    }


    @Override
    public void onClick(View click) {
         switch (click.getId()){
             case R.id.imv_home:
                 goToHome();
                 break;
             case R.id.imv_camera:
                 try {
                     captureImage();
                 }catch(IOException e){
                     e.printStackTrace();
                 }
                 break;
             case R.id.imv_flash:
                 try {
                     toggleFlashLight();
                 }catch(Exception e){
                     e.printStackTrace();
                 }
                 break;
             case R.id.surfaceView:
                 setCameraFocus();
                 break;
             default:
                 break;
         }
    }
}

