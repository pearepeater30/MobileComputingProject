package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.List_Item;

import java.io.IOException;

public class OCRActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    private DBHelper dbHelper;
    private Button addItemButton;

    private static final String TAG = "OCRActivity";
    private static final int requestPermissionID = 101;
    private static int list_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        /**Retrieving list ID from the previous list using Intent*/
        Intent intent = getIntent();
        list_ID = intent.getIntExtra("List_ID", 0);
        /**Setting up the Camera View, text view and button*/
        cameraView = findViewById(R.id.cameraView);
        textView = findViewById(R.id.displayedText);
        addItemButton = findViewById(R.id.addToListOCRButton);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, requestPermissionID);
        }
        else{
            checkCameraHardware(getApplicationContext());
        }
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**On click on the button, will add text detected to the list.*/
                addToList();
            }
        });

    }


    /**
     * Starting the camera source
     */
    private void startCameraSource(){
        /**creating a TextRecognizer object*/
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        /**Notify the user if the dependencies have been loaded*/
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        }

        /**Set up the Camera for taking detecting text*/
        else {
            /**Set up the properties fo the property view*/
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(320, 240)
                    .setRequestedFps(24)
                    .setAutoFocusEnabled(true).build();
            /**Implementing methods for the surfaceView to retrieve frames detected by camera*/
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    /**Start the camera view*/
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
            /**Setup the text recognizer*/
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>()  {
                @Override
                public void release() {
                }

                /**Method used to display items detected by the text processor*/
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    /**Sparse Array used to store text detected by the text processor*/
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    /**If there are items detected by the detector, implement a runnable for the textview*/
                    if (items.size() != 0) {
                        textView.post(new Runnable() {
                            /**Setting a stringbuffer to store items detected by the detector*/
                            @Override
                            public void run() {
                                StringBuffer stringBuffer = new StringBuffer();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuffer.append(item.getValue());
                                    stringBuffer.append("\n");
                                }
                                textView.setText(stringBuffer.toString());
                            }
                        });
                    }
                }


            });
        }
    }


    /**
     * Method used to deal with the request of permission for the camera
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted, please restart app to gain access to camera", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            startCameraSource();
        } else {
            // no camera on this device
            return;
        }
    }

    /**
     * Method used to add an item to the list that has been seen by the detector
     */
    private void addToList(){
        int duration = Toast.LENGTH_SHORT;
        /**Checks if any text was detected before adding, if none were detected, show a message indicating so*/
        if(textView.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "No objects detected", duration).show();
        }
        else {
            /**creating a new DBHelper object*/
            dbHelper = new DBHelper(this);
            /**Creating a new list item object*/
            List_Item newlist = new List_Item(0,list_ID ,textView.getText().toString());
            /**add the list item object into the database*/
            dbHelper.addItemToList(newlist, list_ID);
            /**Display message that the item has been added to the list*/
            Toast.makeText(getApplicationContext(), "Item successfully added", duration).show();
            /**Return to previous activity*/
            this.finish();
        }
    }
}
