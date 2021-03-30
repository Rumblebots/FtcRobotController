package org.firstinspires.ftc.teamcode.ultimategoal.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class WebcamTFOD {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "Adtq/uL/////AAABmcCLRtyrCEwSqHDpqAaHZLCD+lfGkOZPclBIM7sL/JkwWFsdHGQMnkm8o8Tiab7lV6crYVwfD11J/sTJemH5K/2J0uXWfVuQmRYx+DfVIiLwK9kiDL0Kd06+SALTzejuZffRygoUdtjxvZUK9IJ1xfJHxUeJ4kX76Si2p9DhprZBPizrlBu+tKtlUp92asp+vD00mlS1Jp+oYlwzIXymJ5gaanAjskfHcATQhkXCm6Rg9gzafiOOF3MuugcLrzOEpcApRkqWbjC3+jCG1K4IAng1I1J1sq/QebazLfF1CNA7lVq/pNMRq4q4ljCHwlZMgGuxdGQPXhk1qSiKnBVNkT0q0iSEI9Um9Cojj6DOymAy";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public void activateTfod() {
        if (tfod != null) {
            tfod.activate();
//            tfod.setZoom(2.5, 16/9.0);
        }
    }
    public List<Recognition> getUpdatedRecognitions() {
        return tfod.getUpdatedRecognitions();
    }
    public void init(HardwareMap hwMap) {
        initVuforia(hwMap);
        initTfod(hwMap);
    }
    private void initVuforia(HardwareMap hardwareMap) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "webcam");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod(HardwareMap hardwareMap) {
//        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
//                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
//        tfodParameters.minResultConfidence = 0.6f;
//        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
//        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
    public TargetZone autoInitDetect() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                if (updatedRecognitions.size() == 0 ) {
                    return TargetZone.A;
                } else {
                    // list is not empty.
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {

                        // check label to see which target zone to go after.
                        if (recognition.getLabel().equals("Single")) {
                            return TargetZone.B;
                        } else if (recognition.getLabel().equals("Quad")) {
                            return TargetZone.C;
                        } else {
                            return TargetZone.UNKNOWN;
                        }
                    }
                }
            }
        }
        return TargetZone.UNKNOWN;
    }
    public void stopTFOD() {
        tfod.shutdown();
    }
}
