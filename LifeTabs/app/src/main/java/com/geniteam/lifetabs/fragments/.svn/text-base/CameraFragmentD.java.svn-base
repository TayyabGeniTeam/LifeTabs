package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
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
import com.geniteam.lifetabs.objects.Sample;
import com.geniteam.lifetabs.utils.CompLowCost;
import com.geniteam.lifetabs.utils.LifeTabUtils;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.opencv.core.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
@SuppressWarnings("deprecation")

/**
 * Created by Brian Dryja on 5/1/2016.
 */
public class CameraFragmentD extends LifeTabCustomFragment implements SurfaceHolder.Callback,View.OnClickListener {
    private static final int S0 = 0, S25 = 1, S50 = 2, S100 = 3, S150 = 4, S200 = 5,
            S250 = 6, S300 = 7, S350 = 8, S400 = 9, WHT_REA = 10, CLR_CHG = 11;
    private int[] segmentValues = new int[10];
    private Point[][] samplePoints = new Point[12][4];
    private Sample[] samples = new Sample[12];

    MainActivity mContext;
    View view;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    Camera.Parameters param;
    ImageView flashButton;

    private boolean debugEnabled = true;
    private String debugFilename = null;
    private File photoStorage = Environment.getExternalStorageDirectory();
    private double barcodeWidth;
    private double barcodeAngle;
    private Point barcodeOrigin;

    public CameraFragmentD() {
        segmentValues[0] = 0;
        segmentValues[1] = 25;
        segmentValues[2] = 50;
        segmentValues[3] = 100;
        segmentValues[4] = 150;
        segmentValues[5] = 200;
        segmentValues[6] = 250;
        segmentValues[7] = 300;
        segmentValues[8] = 350;
        segmentValues[9] = 400;

        samplePoints[S0][0] = new Point(49,21);
        samplePoints[S0][1] = new Point(89,113);
        samplePoints[S0][2] = new Point(120,99);
        samplePoints[S0][3] = new Point(79,8);
        samplePoints[S25][0] = new Point(126,163);
        samplePoints[S25][1] = new Point(209,219);
        samplePoints[S25][2] = new Point(227,192);
        samplePoints[S25][3] = new Point(144,136);
        samplePoints[S50][0] = new Point(288,239);
        samplePoints[S50][1] = new Point(387,234);
        samplePoints[S50][2] = new Point(386,202);
        samplePoints[S50][3] = new Point(286,206);
        samplePoints[S100][0] = new Point(463,209);
        samplePoints[S100][1] = new Point(539,144);
        samplePoints[S100][2] = new Point(517,120);
        samplePoints[S100][3] = new Point(442,184);
        samplePoints[S150][0] = new Point(578,87);
        samplePoints[S150][1] = new Point(612,-7);
        samplePoints[S150][2] = new Point(581,-18);
        samplePoints[S150][3] = new Point(547,76);
        samplePoints[S200][0] = new Point(613,-81);
        samplePoints[S200][1] = new Point(583,-176);
        samplePoints[S200][2] = new Point(553,-168);
        samplePoints[S200][3] = new Point(582,-72);
        samplePoints[S250][0] = new Point(544,-236);
        samplePoints[S250][1] = new Point(466,-299);
        samplePoints[S250][2] = new Point(446,-274);
        samplePoints[S250][3] = new Point(522,-210);
        samplePoints[S300][0] = new Point(394,-328);
        samplePoints[S300][1] = new Point(294,-332);
        samplePoints[S300][2] = new Point(293,-300);
        samplePoints[S300][3] = new Point(392,-295);
        samplePoints[S350][0] = new Point(207,-304);
        samplePoints[S350][1] = new Point(124,-249);
        samplePoints[S350][2] = new Point(142,-222);
        samplePoints[S350][3] = new Point(226,-276);
        samplePoints[S400][0] = new Point(87,-198);
        samplePoints[S400][1] = new Point(46,-107);
        samplePoints[S400][2] = new Point(75,-93);
        samplePoints[S400][3] = new Point(117,-183);
        samplePoints[WHT_REA][0] = new Point(105,-1);
        samplePoints[WHT_REA][1] = new Point(149,89);
        samplePoints[WHT_REA][2] = new Point(178,75);
        samplePoints[WHT_REA][3] = new Point(135,-15);
        samplePoints[CLR_CHG][0] = new Point(296,-1);
        samplePoints[CLR_CHG][1] = new Point(384,-1);
        samplePoints[CLR_CHG][2] = new Point(384,-90);
        samplePoints[CLR_CHG][3] = new Point(296,-90);
    }

    // TODO: Rename and change types and number of parameters
    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment cameraFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.CAMERA);

        if(cameraFragment == null){
            cameraFragment = new CameraFragmentD();
        }
        return cameraFragment;
    }

    //================================================================================
    // Overrides
    //================================================================================

    @Override
      public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
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
        view.findViewById(R.id.texture).setVisibility(View.GONE);
        setBasicContents(view);
        return view;
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

           /* if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            } else {
                // param.setFocusMode(focusModes.get(0));
            }*/
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

    @Override
    public boolean onKeyDown() {
        goToHome();
        return true;
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

    //================================================================================
    // Navigation
    //================================================================================

    public void goToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
}

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

    //================================================================================
    // Debug Methods
    //================================================================================

    private void updateImageName(String newName){
        updateImageName(newName,false);
    }

    private void updateImageName(String newName, boolean prepend){
        if(debugEnabled) {
            if (!prepend) {
                debugFilename = newName;
            } else {
                debugFilename = newName + debugFilename;
            }
        }
    }

    private void setFinalFilename(String currentName){
        if(debugEnabled) {
            File from = new File(photoStorage, currentName);
            File tof = new File(photoStorage, debugFilename);
            from.renameTo(tof);
        }
    }

    private void removeFile(String currentName){
        File file = new File(photoStorage, currentName);
        boolean deleted = file.delete();
    }


    //================================================================================
    // Camera Related
    //================================================================================

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

      int minexpo =  parameters.getMinExposureCompensation();
      int maxexpo =  parameters.getMaxExposureCompensation();
      float stepexpo =  parameters.getExposureCompensationStep();
      int currexpo =  parameters.getExposureCompensation();

        if(1 >= minexpo && 1<=maxexpo){
            parameters.setExposureCompensation(1);
        }

        // param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        try {
            camera.setParameters(parameters);
            camera.startPreview();
        }catch(RuntimeException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void captureImage() throws IOException {

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    camera.takePicture(null, null, jpegCallback);
                    //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                }else{
                    camera.takePicture(null, null, jpegCallback);
                }
            }
        });
        //  camera.au
       // camera.takePicture(null, null, jpegCallback);
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

                if(flashButton!=null){
                    flashButton.setImageResource(R.drawable.flash_button_filled);
                }
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

                if(flashButton!=null){
                    flashButton.setImageResource(R.drawable.flash_button);
                }
            }
        }else{
           // flashButton.setEnabled(false);
            // Toast.makeText(mContext,"Your device have no flash light!",Toast.LENGTH_SHORT).show();
        }
    }

    //================================================================================
    // Custom Algorithm
    //================================================================================

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
                FileOutputStream outStream;
                File imageFile;
                String imageFilename = null;

                try {
                    imageFilename = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    if(debugEnabled){
                        debugFilename = imageFilename;
                    }
                    imageFile = new File(photoStorage, imageFilename);
                    outStream = new FileOutputStream(imageFile);
                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(mContext, "Calculating results...", Toast.LENGTH_SHORT).show();
                    refreshCamera();
                    try {
                        resultsProcessing(imageFilename);
                    }catch(Exception e){
                        Toast.makeText(mContext, "Unable to process the strip", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

        };

        /**
         * check if camera has flash , otherwise hide flash button
         */

        if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            //do nothing
        }else{
            flashButton.setVisibility(View.GONE);
        }
    }

    private void resultsProcessing(String imageFilename) {
        boolean keepGoing = true;

        //receive photo and load into bitmap
        File imageFile = new File(photoStorage, imageFilename);
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        //stop if there were errors
        if(keepGoing) {
            //process barcode in image
            //determine strip position, size and rotation based on the barcode data
            //determine a ratio to represent the size of the strip
            if (!barcodeDetector(imageBitmap, imageFile)) {
                keepGoing = false;
            }
        }

        //stop if there were errors
        if(keepGoing) {
            //TODO: check for too much skewing
        }

        //stop if there were errors
        if(keepGoing) {
            //check if the entire strip is in the image, the circular part
            if (!isEntireStripShown(imageBitmap)) {
                Toast.makeText(mContext, "Your photo must contain the entire strip, please try again", Toast.LENGTH_LONG).show();
                keepGoing = false;
            }

        }

        //stop if there were errors
        if(keepGoing) {
            //find all coordinates for samples needed from the strip
            //pull the samples from the strip and determine color of each samples
            //draw on image sample area border (after taking color sample)
            createSampleAreas(imageBitmap, imageFile);
        }

        //stop if there were errors
        if(keepGoing) {
            //check white reactive area vs white control area for old strips
            if (!isWhiteCheckOkay()) {
                Toast.makeText(mContext, "You have an old or heat damaged strip, unable to read", Toast.LENGTH_LONG).show();
                keepGoing = false;
            }
        }

        //instantiate our result value
        int result = -1;

        //stop if there were errors
        if(keepGoing) {
            //check percentage closeness to each segment from center color area
            //calculate what value we are closest to.
            result = calculateStripValue();
            //if there was an error
            if (result == -1) {
                keepGoing = false;
            }else{
                //add the result value to the filname
                updateImageName("Val_"+String.valueOf(result)+"-", true);
            }
        }

        //set final file name or remove image
        if(debugEnabled){
            //Upadate the B_XX after every build sending to designli, this way we can keep track of
            //what builds generated what kind of results.
            updateImageName("B_10-", true);
            setFinalFilename(imageFilename);
        }else{
            removeFile(imageFilename);
        }

        //stop if there were errors
        if(keepGoing) {
            //toast user with result
            Toast.makeText(mContext, "Your reading is: " + result, Toast.LENGTH_LONG).show();

            String datenTime = LifeTabUtils.getDataToSave();
            //  LifeTabUtils.writeToFile(datenTime, calculatedValue + "", segment1Percentage + "", mContext, "success");
            goToResultsSection(datenTime.split(" ")[0], datenTime.split(" ")[1] + " " + datenTime.split(" ")[2], result + "", "mg/dL");

            //TODO:send user to the results screen

        }
    }

    private boolean barcodeDetector(Bitmap imageBitmap, File imageFile){
        Barcode stripBarcode = null;

        //run through barcode scanner class
        BarcodeDetector detector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if(!detector.isOperational()){
            Toast.makeText(mContext,"Could not set up the detector",Toast.LENGTH_LONG).show();
            return false;
        }

        //detect the barcode
        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        //decode the barcode
        if(barcodes.size()==1) {
            stripBarcode = barcodes.valueAt(0);
        }else if(barcodes.size()<1 || barcodes==null){
            Toast.makeText(mContext,"Strip could not be authenticated. Try again in good light and ensure camera has focus",Toast.LENGTH_LONG).show();
            return false;
        }else if(barcodes.size()>1){
            Toast.makeText(mContext,"Multiple barcodes were found",Toast.LENGTH_LONG).show();
            return false;
        }else{
            Toast.makeText(mContext,"Error parsing barcode detector",Toast.LENGTH_LONG).show();
            return false;
        }

        if(stripBarcode.cornerPoints.length<4){
            Toast.makeText(mContext,"Not enough points found on the barcode",Toast.LENGTH_LONG).show();
            return false;
        }else if(stripBarcode.cornerPoints.length>4) {
            Toast.makeText(mContext,"Too many points found on the barcode",Toast.LENGTH_LONG).show();
            return false;
        }else if(stripBarcode.cornerPoints.length != 4){
            Toast.makeText(mContext,"Error locating points on the barcode",Toast.LENGTH_LONG).show();
            return false;
        }

        //set barcodeWidth and barcodeAngle and barcodeOrigin for ratio calculations
        barcodeWidth = Math.sqrt(Math.pow(stripBarcode.cornerPoints[1].x-stripBarcode.cornerPoints[0].x,2)+Math.pow(stripBarcode.cornerPoints[1].y-stripBarcode.cornerPoints[0].y,2));
        barcodeAngle = Math.atan2(stripBarcode.cornerPoints[1].y-stripBarcode.cornerPoints[0].y, stripBarcode.cornerPoints[1].x-stripBarcode.cornerPoints[0].x) * 180 / Math.PI;
        barcodeOrigin = new Point(stripBarcode.cornerPoints[0].x, stripBarcode.cornerPoints[0].y);

        //draw shape on image
        if(debugEnabled) {
            Bitmap imageMutBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imageMutBitmap);
            // set drawing color
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(4.0f);

            Paint pTop = new Paint();
            pTop.setColor(Color.BLUE);
            pTop.setStyle(Paint.Style.STROKE);
            pTop.setStrokeWidth(4.0f);

            if(stripBarcode.cornerPoints.length==4) {
                // draw a line onto the canvas
                canvas.drawLine(stripBarcode.cornerPoints[0].x, stripBarcode.cornerPoints[0].y, stripBarcode.cornerPoints[1].x, stripBarcode.cornerPoints[1].y, pTop);
                canvas.drawLine(stripBarcode.cornerPoints[1].x, stripBarcode.cornerPoints[1].y, stripBarcode.cornerPoints[2].x, stripBarcode.cornerPoints[2].y, p);
                canvas.drawLine(stripBarcode.cornerPoints[2].x, stripBarcode.cornerPoints[2].y, stripBarcode.cornerPoints[3].x, stripBarcode.cornerPoints[3].y, p);
                canvas.drawLine(stripBarcode.cornerPoints[3].x, stripBarcode.cornerPoints[3].y, stripBarcode.cornerPoints[0].x, stripBarcode.cornerPoints[0].y, p);
            }

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
                imageMutBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    //This method converts line distances from the original control measurements to the measurements
    //of this actual image
    private int getPxFromRel(int controlLength){
        //81 was the calculated value, adjust to get dead on
        return (int) Math.round(( (double)(controlLength) / 85) * barcodeWidth);
    }

    //controlPoint should be in reference to the top left corner of the barcode (when using final
    //photo orientation, horizontal) being (0,0)
    private Point getPointFromRel(Point controlPoint){
        Point newPoint = new Point();

        //Adjust controlPoint values to current image ratios
        Point controlPointAdj = new Point(getPxFromRel( (int)controlPoint.x ), getPxFromRel( (int)controlPoint.y ));

        //Should work with just barcodeAngle, but needed to adjust to get more perfect results
        double angle = barcodeAngle - 1;

        //Adjust for rotation of new image
        newPoint.y = controlPointAdj.y * Math.cos( angle * Math.PI / 180 ) - controlPointAdj.x * Math.sin( angle * Math.PI / 180 );
        newPoint.x = controlPointAdj.y * Math.sin( angle * Math.PI / 180 ) + controlPointAdj.x * Math.cos( angle * Math.PI / 180 );

        //offset to give us actual image coordinates
        newPoint.y = barcodeOrigin.y - newPoint.y;
        newPoint.x = barcodeOrigin.x + newPoint.x;

        return newPoint;
    }

    private void createSampleAreas(Bitmap imageBitmap, File imageFile){
        Sample sample;

        //foreach sample area, create the sample object
        for (int i = 0; i < samplePoints.length; i++)
        {
            samples[i] = new Sample(imageBitmap, getPointFromRel(samplePoints[i][0]), getPointFromRel(samplePoints[i][1]), getPointFromRel(samplePoints[i][2]), getPointFromRel(samplePoints[i][3]));
        }

        //draw sample areas on image
        if(debugEnabled) {
            Bitmap imageBitmapp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap imageMutBitmap = imageBitmapp.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imageMutBitmap);
            // set drawing color
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(4.0f);

            Paint textP = new Paint();
            textP.setColor(Color.CYAN);
            textP.setTextSize(36.0f);

            Point tl;
            Point tr;
            Point bl;
            Point br;

            //foreach sample area
            for (int i = 0; i < samplePoints.length; i++)
            {
                //get our absolute points for the current image
                tl = getPointFromRel(samplePoints[i][0]);
                tr = getPointFromRel(samplePoints[i][1]);
                bl = getPointFromRel(samplePoints[i][2]);
                br = getPointFromRel(samplePoints[i][3]);

                // draw a line for each side of the sample area onto the canvas for areas
                canvas.drawLine((float)tl.x, (float)tl.y, (float)tr.x, (float)tr.y, p);
                canvas.drawLine((float)tr.x, (float)tr.y, (float)bl.x, (float)bl.y, p);
                canvas.drawLine((float)bl.x, (float)bl.y, (float)br.x, (float)br.y, p);
                canvas.drawLine((float)br.x, (float)br.y, (float)tl.x, (float)tl.y, p);

                //draw the label of the rgb value
                canvas.drawText("R"+Color.red(samples[i].getColor())+"G"+Color.green(samples[i].getColor())+"B"+Color.blue(samples[i].getColor()), (float)br.x, (float)br.y, textP);
            }

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
                imageMutBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isEntireStripShown(Bitmap imageBitmap){
        //get our absolute points for the current image of our check points
        Point p1 = getPointFromRel(new Point(91,189));
        Point p2 = getPointFromRel(new Point(332,287));
        Point p3 = getPointFromRel(new Point(523,232));
        Point p4 = getPointFromRel(new Point(641,-43));
        Point p5 = getPointFromRel(new Point(542,-322));
        Point p6 = getPointFromRel(new Point(335,-384));
        Point p7 = getPointFromRel(new Point(90,-287));

        Region region = new Region(0,0,imageBitmap.getWidth(),imageBitmap.getHeight());

        //check if all points are in the image size
        if(region.contains((int)p1.x, (int)p1.y) &&
                region.contains((int)p2.x, (int)p2.y) &&
                region.contains((int)p3.x, (int)p3.y) &&
                region.contains((int)p4.x, (int)p4.y) &&
                region.contains((int)p5.x, (int)p5.y) &&
                region.contains((int)p6.x, (int)p6.y) &&
                region.contains((int)p7.x, (int)p7.y)){
            return true;
        }else{
            return false;
        }

    }

    private boolean isWhiteCheckOkay(){

        int diff = getPercentDiffBetweenColors(samples[S0].getColor(), samples[WHT_REA].getColor());

        if(debugEnabled) {
            updateImageName("WhtCkP:"+diff+"_", true);
        }

        if(diff > 90){
            return true;
        }else{
            return false;
        }
    }

    private int calculateStripValue(){

        int bestSample = -1;
        int bestValue = 0;

        StringBuilder tmp = new StringBuilder(50);

        //foreach sample, compare it to the center sample, -2 because we don't want to compare it to itself or white reactive area
        for (int i = 0; i < samples.length-2; i++){
            samples[i].setPercentToCenter(getPercentDiffBetweenColors(samples[i].getColor(), samples[CLR_CHG].getColor()));
            //Toast.makeText(mContext, "percent:"+String.valueOf(percent)+"_i:"+String.valueOf(i), Toast.LENGTH_SHORT).show();
            tmp.append("i:"+i+"-p:"+samples[i].getPercentageToCenter()+"_");
            if(bestValue < samples[i].getPercentageToCenter()){
                bestValue = samples[i].getPercentageToCenter();
                bestSample = i;
            }
        }

        if(bestSample == -1){
            Toast.makeText(mContext, "No definitive result could be calculated", Toast.LENGTH_LONG).show();
            return -1;
        }

        int highSample;
        int highPercent;
        boolean includeHigh = false;
        double upperAverage = segmentValues[bestSample];
        int lowSample;
        int lowPercent;
        boolean includeLow = false;
        double lowerAverage = segmentValues[bestSample];

        if(bestSample < 9){
            //get segment above
            highSample = bestSample + 1;
            highPercent = samples[highSample].getPercentageToCenter();
            //if the difference is less than our percentage tolerance, use!
            if ((bestValue * 0.06) > bestValue - highPercent){
                includeHigh = true;
            }

            if ((bestValue * 0.03) > bestValue - highPercent){
                upperAverage = (segmentValues[bestSample] + segmentValues[highSample])/2;
            }else {
                upperAverage = (segmentValues[bestSample] + segmentValues[bestSample] + segmentValues[highSample])/3;
            }
        }

        if(bestSample > 0) {
            //get segment below
            lowSample = bestSample - 1;
            lowPercent = samples[lowSample].getPercentageToCenter();
            if ((bestValue * 0.06) > bestValue - lowPercent){
                includeLow = true;
            }

            if ((bestValue * 0.03) > bestValue - lowPercent){
                lowerAverage = (segmentValues[bestSample] + segmentValues[lowSample])/2;
            }else {
                lowerAverage = (segmentValues[bestSample] + segmentValues[bestSample] + segmentValues[lowSample])/3;
            }
        }

        double result;

        if(includeHigh && includeLow){

            result = (upperAverage + lowerAverage)/2;

        }else if(includeHigh && !includeLow){

            result = upperAverage;

        }else if(!includeHigh && includeLow){

            result = lowerAverage;

        }else{ //just use our given segment value

            result = segmentValues[bestSample];

        }

        if(debugEnabled) {
            updateImageName(tmp.toString(), true);
        }

        return (int)Math.round(result);
    }

    private int getPercentDiffBetweenColors(int color1, int color2){
        return CompLowCost.getPercentDistanceCurve(color1, color2);
    }
}