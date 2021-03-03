package org.firstinspires.ftc.teamcode.ultimategoal.util;

import android.util.Log;
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
    private static final String VUFORIA_KEY =
            "AaMHb/L/////AAABmV+wis0GQEysrIClzwptLXclDtwtlDiJsCLQCHpCy1cOp3M/aXwpSkDw0nLPjbqIZHUN0T5e3MU4L5Mu0NXOeKtHc8yawpUtGVmKA74pVOo8fBr/STmcWEIiproB4WBFMMds2s1MgcxtwGPQ15u96F+MkztyTmO1fUrHGnOilm0up4R42pldHeJjvFge4N7xa1oUujNtFniuUp6jiN48gLNI/DHFGySf+vB4fDMLCTAKyFh8Ca0haun8kQGntckEvGhvXpL/l2usagU5rHrQyiB9er5UXd5wDKZxv9+YACQpQ9Qcl4LyQa2YelQ/mljey0flEtKfMEzGWjbS+/1yBFeFUWf8EAJwi1AaBeK1xRii";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public void activateTfod() {
        if (tfod != null) {
            tfod.activate();
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
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
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
