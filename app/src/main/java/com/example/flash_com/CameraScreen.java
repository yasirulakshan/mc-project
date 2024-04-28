package com.example.flash_com;

import androidx.annotation.NonNull;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CameraScreen extends CameraActivity {

    CameraBridgeViewBase cameraBridgeViewBase;
    TextView resultTextView;

    final int MAX_COUNTER_THRESHOLD = 20000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getCameraPermission();

        cameraBridgeViewBase = findViewById(R.id.cameraView);
        resultTextView = findViewById(R.id.result_view);
        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
            }

            @Override
            public void onCameraViewStopped() {
            }
            int detectedFrames = 0;
            int undefinedFrames = 0;
            String detectedMessage = "";
            String detectedStringWindow = "";
            String detectedText = "";
            int textLength = 0;

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                Mat rgbaMat = inputFrame.rgba();
                Mat greyMat = new Mat();
                Imgproc.cvtColor(rgbaMat, greyMat, Imgproc.COLOR_BGR2GRAY);
                Mat bw = new Mat();
                Imgproc.threshold(greyMat, bw, 254, 255, Imgproc.THRESH_BINARY);

                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();
                Imgproc.findContours(bw, contours, hierarchy, Imgproc.RETR_TREE,
                        Imgproc.CHAIN_APPROX_SIMPLE);

                double maxCounterArea = 0;
                for (MatOfPoint contour : contours) {
                    Mat contourMat = new Mat();
                    contour.convertTo(contourMat, CvType.CV_32SC2);
                    double area = Imgproc.contourArea(contourMat);
                    if (maxCounterArea < area){
                        maxCounterArea = area;
                    }
                }

                if (maxCounterArea > MAX_COUNTER_THRESHOLD) {
                    undefinedFrames = 0;
                    detectedFrames++;
                } else{
                    undefinedFrames++;
                    if(undefinedFrames > 2){
                        if(detectedFrames >= 5){
                            detectedMessage = detectedMessage + "0";
                            detectedStringWindow = detectedStringWindow + "0";
                        } else if (detectedFrames> 1 & !detectedMessage.isEmpty()){
                            detectedMessage = detectedMessage + "1";
                            detectedStringWindow = detectedStringWindow + "1";
                        }

                        detectedFrames = 0;
                        if(detectedStringWindow.length( )>= 8){
                            int checkLength = detectedStringWindow.length();
                            String checkString = detectedStringWindow.substring(checkLength - 8,
                                    checkLength);
                            int number = Integer.parseInt(checkString, 2);
                            if(textLength == 0) {
                                textLength = number;
                            } else {
                                char ch = (char) number;
                                detectedText = detectedText + ch;
                                String showText = detectedText;
                                resultTextView.setText(showText);
                            }
                            if(detectedText.length() == textLength){
                                Intent intent = new Intent(CameraScreen.this, ReceiveActivity.class);
                                intent.putExtra("key", detectedText);
                                startActivity(intent);
                            }
                            detectedStringWindow = "";
                        }

                    }
                }
                return bw;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraBridgeViewBase.disableView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==3 && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                getCameraPermission();
            }
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        List<?> a = Collections.singletonList(cameraBridgeViewBase);
        StringBuilder listString = new StringBuilder();
        for (int i = 0; i < a.size(); i++){
            listString.append(a.get(i));
        }
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 3);
            }
        }
    }


}