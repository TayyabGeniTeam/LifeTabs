package com.geniteam.lifetabs.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Created by Tayyab on 5/9/2016.
 */
public class Camera2FragmentD  extends LifeTabCustomFragment implements View.OnClickListener{

    private static final int S0 = 0, S25 = 1, S50 = 2, S100 = 3, S150 = 4, S200 = 5,
            S250 = 6, S300 = 7, S350 = 8, S400 = 9, WHT_REA = 10, CLR_CHG = 11;
    private int[] segmentValues = new int[10];
    private Point[][] samplePoints = new Point[12][4];
    private Sample[] samples = new Sample[12];



    private final static String TAG = "Camera2testJ";
    private Size mPreviewSize;

    private TextureView mTextureView;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mPreviewSession;

    private ImageView mBtnShot;
    private ImageView mBtnFlash;

    private View view;
    MainActivity mContext;

    int pHeight;
    int pWidth;

    private boolean debugEnabled = true;
    private String debugFilename = null;
    private File photoStorage = Environment.getExternalStorageDirectory();
    private double barcodeWidth;
    private double barcodeAngle;
    private Point barcodeOrigin;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public Camera2FragmentD() {
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

    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment cameraFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.CAMERA);

        if(cameraFragment == null){
            cameraFragment = new Camera2FragmentD();
        }
        return cameraFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.camera_layout, container, false);

        view.findViewById(R.id.surfaceView).setVisibility(View.GONE);
        mContext.toolbar.setVisibility(View.GONE);

        mTextureView = (TextureView)view.findViewById(R.id.texture);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        mBtnShot = (ImageView)view.findViewById(R.id.imv_camera);
        mBtnFlash = (ImageView) view.findViewById(R.id.imv_flash);
        view.findViewById(R.id.imv_home).setOnClickListener(this);


        mBtnFlash.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e(TAG, "mBtnShot clicked");

                if(mPreviewBuilder.get(CaptureRequest.FLASH_MODE) == CameraMetadata.FLASH_MODE_OFF){
                    turnOnFlashLight();

                }else{
                    turnOffFlashLight();
                }


            }

        });

        mBtnShot.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e(TAG, "mBtnShot clicked");
                takePicture();
            }

        });

        if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            //do nothing
        }else{
            mBtnFlash.setVisibility(View.GONE);
        }


        return view;
    }

    @SuppressLint("NewApi")
    private Size getPreferredPreviewSize(Size[] mapSizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for(Size option : mapSizes) {
            if(width > height) {
                if(option.getWidth() > width &&
                        option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if(option.getWidth() > height &&
                        option.getHeight() > width) {
                    collectorSizes.add(option);
                }
            }
        }
        if(collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return mapSizes[0];
    }

    @SuppressLint("NewApi")
    protected void takePicture() {
        Log.e(TAG, "takePicture");
        if(null == mCameraDevice) {
            Log.e(TAG, "mCameraDevice is null, return");
            return;
        }

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());

            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics
                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }
            int width  = 1280;
            int height = 720;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width  = 1280;   // here set preview size
                height = 720;
            }

            width = getPreferredPreviewSize(jpegSizes,pWidth,pHeight).getWidth();
            height =getPreferredPreviewSize(jpegSizes,pWidth,pHeight).getHeight();

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // Orientation
            int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            final String imageFilename = String.valueOf(System.currentTimeMillis()) + ".jpg";

            if(debugEnabled){
                debugFilename = imageFilename;
            }

            final File file = new File(Environment.getExternalStorageDirectory(),imageFilename);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {

                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }

                        resultsProcessing(imageFilename);


                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }

            };

            HandlerThread thread = new HandlerThread("CameraPicture");
            thread.start();
            final Handler backgroudHandler = new Handler(thread.getLooper());
            reader.setOnImageAvailableListener(readerListener, backgroudHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                                               CaptureRequest request, TotalCaptureResult result) {

                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(mContext, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    startPreview();
                }

            };

            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {

                    try {
                        session.capture(captureBuilder.build(), captureListener, backgroudHandler);
                    } catch (CameraAccessException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, backgroudHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    private void openCamera(int w,int h) {

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "openCamera E");
        try {
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size [] mlist = map.getOutputSizes(SurfaceTexture.class);

            mPreviewSize = getOptimalPreviewSize(mlist,w,h);
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    @SuppressLint("NewApi")
    private Size getOptimalPreviewSize(Size [] sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Size size : sizes) {
            double ratio = (double) size.getWidth()/ size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight()- targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getWidth() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener(){

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.e(TAG, "onSurfaceTextureAvailable, width="+width+",height="+height);
            pHeight = height;
            pWidth = width;
            openCamera(width,height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                                int width, int height) {
            Log.e(TAG, "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //Log.e(TAG, "onSurfaceTextureUpdated");
        }

    };

    @SuppressLint("NewApi")
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {

            Log.e(TAG, "onOpened");
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

            Log.e(TAG, "onDisconnected");
        }

        @Override
        public void onError(CameraDevice camera, int error) {

            Log.e(TAG, "onError");
        }

    };

    @SuppressLint("NewApi")
    @Override
    public void onPause() {

        Log.e(TAG, "onPause");
        super.onPause();
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    @SuppressLint("NewApi")
    protected void startPreview() {

        if(null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            Log.e(TAG, "startPreview fail, return");
            return;
        }

        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        if(null == texture) {
            Log.e(TAG,"texture is null, return");
            return;
        }

        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface surface = new Surface(texture);

        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
        mPreviewBuilder.addTarget(surface);

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {

                    mPreviewSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                    Toast.makeText(mContext, "onConfigureFailed", Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    protected void updatePreview() {

        if(null == mCameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }

        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);//auto-exposure, auto-white-balance, auto-focus

        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        Handler backgroundHandler = new Handler(thread.getLooper());

        try {
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
    }

    //**************

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

    private int getPxFromRel(int controlLength){
        //81 was the calculated value, adjust to get dead on
        return (int) Math.round(( (double)(controlLength) / 85) * barcodeWidth);
    }

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

    private int getPercentDiffBetweenColors(int color1, int color2){
        return CompLowCost.getPercentDistanceCurve(color1, color2);
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

    private void removeFile(String currentName){
        File file = new File(photoStorage, currentName);
        boolean deleted = file.delete();
    }

    private void setFinalFilename(String currentName){
        if(debugEnabled) {
            File from = new File(photoStorage, currentName);
            File tof = new File(photoStorage, debugFilename);
            from.renameTo(tof);
        }
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

    //***************

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @SuppressLint("NewApi")
    public void turnOffFlashLight() {
        try {
            mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);

            mBtnFlash.setImageResource(R.drawable.flash_button);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public void turnOnFlashLight() {
        try {
            mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);

            mBtnFlash.setImageResource(R.drawable.flash_button_filled);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown() {
        return true;
    }

    public void goToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()){
            case R.id.imv_home:
                goToHome();
                break;
            default:
                break;
        }
    }


}
