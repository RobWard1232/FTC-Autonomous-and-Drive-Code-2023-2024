package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

/* maybe add method like movement(1.0, 1.0, 1.0, 1.0); 
* where each double accounts for one motor, 
* I think that could clean up and shorten the program a lot
*/

@Autonomous(name = "AutonomouseTetrix")
public class AutonomouseTetrix extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor sweep;
    private DcMotor armMotor;
    private DcMotor armAngleMotor;

    private int something = 0;
    private String getMetaString;

    private int armCurrentPosition = 0;
    private int armAnglePosition = 0;
    private int armAnglePrevPos = 0;
    private int armPreviousPosition = 0;
    private int armMinPosition = 0;
    private int armMaxPosition = -1111;// motor telemetry is backwards
    private int dangerZoneOffsetUp = 0;
    private int dangerZoneOffsetDown = 100;

    private int armAngleCurrentPosition = 0;
    private int armAnglePreviousPosition = 0;
    private int angleCurrentPosition = 0;
    private int anglePreviousPosition = 0;
    private int armAngleMinPosition = 0;
    private int armAngleMaxPosition = 1200; // Safer than Zero
    private int AdangerZoneOffsetUp = 0;
    private int AdangerZoneOffsetDown = 100;

    private int xPos;

    // add declaration for arm motors here later

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the TensorFlow Object Detection
     * processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal myVisionPortal;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "zeroth");
        frontRight = hardwareMap.get(DcMotor.class, "first");
        backLeft = hardwareMap.get(DcMotor.class, "second");
        backRight = hardwareMap.get(DcMotor.class, "third");
        armAngleMotor = hardwareMap.get(DcMotor.class, "ford");
        armMotor = hardwareMap.get(DcMotor.class, "moto_moto");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        boolean isShort = true;
        // add arm motor hardwareMap here later
        initDoubleVision();
        waitForStart();
        if (opModeIsActive()) {

            backward(0.5);

            while (something == 0) {
                telemetryAprilTag();

                telemetry.update();
            }

            if (isShort) {
                if (getMetaString.indexOf("Blue") != -1) { // change to specified areas later
                    backward(1.2);
                    buffer(0.3);
                    turn(true);
                    buffer(0.3);
                    backward(2.5);// might have to change later.
                    buffer(0.3);
                    if (something == 1) {
                        strafe(true, 0.3);
                    } else if (something == 3) {
                        strafe(false, 0.3);
                    }
                    buffer(0.3);
                    setArmAnglePosition(true);
                } else if (getMetaString.indexOf("Red") != -1) {
                    backward(1.2);
                    buffer(0.3);
                    turn(false);
                    buffer(0.3);
                    backward(2.5);// might have to change later.
                    buffer(0.3);
                    if (something == 4) {
                        strafe(true, 0.3);
                    } else if (something == 6) {
                        strafe(false, 0.3);
                    }
                    buffer(0.3);
                    setArmAnglePosition(true);
                }
            } else {
                if (getMetaString.indexOf("Blue") != -1) {
                    backward(1.2);
                    buffer(0.3);
                    turn(true);
                    buffer(0.3);
                    backward(6);
                    buffer(0.3);
                    if (something == 1) {
                        strafe(true, 0.3);
                    } else if (something == 3) {
                        strafe(false, 0.3);
                    }
                    buffer(0.3);
                    setArmAnglePosition(true);
                } else if (getMetaString.indexOf("Red") != -1) {
                    backward(1.2);
                    buffer(0.3);
                    turn(false);
                    buffer(0.3);
                    backward(6);
                    buffer(0.3);
                    if (something == 1) {
                        strafe(true, 0.3);
                    } else if (something == 3) {
                        strafe(false, 0.3);
                    }
                    buffer(0.3);
                    setArmAnglePosition(true);
                }
            }
        }
    }

    private void initDoubleVision() {
        // -----------------------------------------------------------------------------------------
        // AprilTag Configuration
        // -----------------------------------------------------------------------------------------

        aprilTag = new AprilTagProcessor.Builder()
                .build();

        // -----------------------------------------------------------------------------------------
        // TFOD Configuration
        // -----------------------------------------------------------------------------------------

        tfod = new TfodProcessor.Builder()
                .build();

        // -----------------------------------------------------------------------------------------
        // Camera Configuration
        // -----------------------------------------------------------------------------------------

        myVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessors(tfod, aprilTag)
                .build();
    }

    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x,
                        detection.ftcPose.y, detection.ftcPose.z));
                xPos = detection.ftcPos.x;
                something = detection.id;
                getMetaString = (String) (detection.metadata.name);
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(
                        String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        } // end for() loop
    } // end method telemetryAprilTag()

    public void move(double FR, double FL, double BL, double BR) {
        frontLeft.setPower(-FR * 0.5);
        frontRight.setPower(FL * 0.5);
        backLeft.setPower(BL * 0.5);
        backRight.setPower(-BR * 0.5);
    }

    // negative positive positive. negative
    public void debug(double FR, double FL, double BL, double BR, double time) {

        ElapsedTime timer = new ElapsedTime();

        while (timer.seconds() <= time) {
            frontLeft.setPower(FR);
            frontRight.setPower(FL);
            backLeft.setPower(BL);
            backRight.setPower(BR);
        }
    }

    // backward function :)
    public void backward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        // telemetry.addData("moving backward.");
        while (timer.seconds() <= time) {
            move(-1.0, -1.0, -1.0, -1.0);
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void forward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        // telemetry.addData("moving forward.");
        while (timer.seconds() <= time) {
            move(1.0, 1.0, 1.0, 1.0);
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void turn(boolean isLeft /* true = left, false = right */) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (isLeft) {
            // telemetry.addData("moving left.");
            while (timer.seconds() <= 1.6) {
                move(1, -1, -1, 1);
            }
        } else {
            /*
             * this time can be different depending on what motors
             * you're using or how much power is set to the motor
             */
            // telemetry.addData("moving right.");
            while (timer.seconds() <= 1.6) {
                move(-1, 1, 1, -1);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    // currently not working correctly
    public void strafe(boolean strafeLeft /* true = left, false = right */, double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (strafeLeft) {
            while (timer.seconds() <= time) {
                move(1.0, 1.0, -1.0, -1.0);
            }
        } else {
            while (timer.seconds() <= time) {
                move(-1.0, -1.0, 1.0, 1.0);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void strafe1(boolean strafeLeft) {
        if (strafeLeft) {
            while (strafeLeft) {
                move(1.0, 1.0, -1.0, -1.0);
            }
        } else {
            while (!strafeLeft) {
                move(-1.0, -1.0, 1.0, 1.0);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    // method if you want the robot to wait in between actions
    public void buffer(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        // telemetry.addData("WAIT");
        while (timer.seconds() <= time) {
            move(0.0, 0.0, 0.0, 0.0);
        }
    }

    public void setArmAnglePosition(boolean isMax /* true = max, false = min */) {

        int armStep = armAngleCurrentPosition - armPreviousPosition;

        armAngleCurrentPosition = armAngleMotor.getCurrentPosition();

        telemetry.addLine("arm angle motor current pos: " + armAngleCurrentPosition);

        if (isMax) {
            while (armAngleMotor.getCurrentPosition() + armStep > -500 /*
                                                                        * <- this number might need to change depending
                                                                        * on the batterys charge?
                                                                        */) {
                armAngleMotor.setPower(-1.0);
            }
            armAngleMotor.setPower(0.0);
        } else if (!isMax) {
            while (armAngleMotor.getCurrentPosition() > 0) {
                armAngleMotor.setPower(1.0);
            }
            armAngleMotor.setPower(0.0);
        }
        armAnglePreviousPosition = armAngleCurrentPosition;
    }

    public void setArmPosition(boolean isMax /* true = max, false = min */) {
        int armStep = armCurrentPosition - armPreviousPosition;

        armCurrentPosition = armMotor.getCurrentPosition();

        telemetry.addLine("arm motor curren pos: " + armCurrenPosition);

        if (isMax) {
            while (armMotor.getCurrentPosition() + armStep > armMaxPosition) {
                armMotor.setPower(-1.0);
            }
            armMotor.setPower(0.0);
        } else if (!isMax) {
            while (armMotor.getCurrentPosition() + armStep > 0) {
                armMotor.setPower(1.0);
            }
            armMotor.setPower(0.0);
        }
        armPreviousPosition = armCurrentPosition;
    }

    // possible integration of centering a qr code **has not been tested yet**
    public void centerRobot() {

        if (xPos > 300) {
            while (xPos > 300) {
                strafe(true);
            }
        } else if (xPos < 300) {
            while (xPos < 300) {
                strafe(false);
                // might need an if statement to break it?
            }
        }
    }
}
