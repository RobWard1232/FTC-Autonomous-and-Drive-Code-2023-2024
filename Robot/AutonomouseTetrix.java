package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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

//IMPORTANT figure out how to clear the apriltag list to find a new qr code while centering

import java.util.List;

@Autonomous(name = "AutonomouseTetrix")
public class AutonomouseTetrix extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor sweep;
    private DcMotor armMotor;
    private DcMotor armAngleMotor;
    private DcMotor wrist;

    //use this if april tag is ever legal again for game pieces.
    //private int something = 0;
    //private String getMetaString;

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
    private int armAngleMaxPosition = 2000; // Safer than Zero
    private int AdangerZoneOffsetUp = 0;
    private int AdangerZoneOffsetDown = 100;

    private int wristCurrentPosition = 0;
    private int wristPreviousPosition = 0;
    private int wristMin = 0;
    private int wristMax = 150;

    private double xPos;

    // add declaration for arm motors here later

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    //private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the TensorFlow Object Detection
     * processor.
     */
    //private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    //private VisionPortal myVisionPortal;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "zeroth");
        frontRight = hardwareMap.get(DcMotor.class, "first");
        backLeft = hardwareMap.get(DcMotor.class, "second");
        backRight = hardwareMap.get(DcMotor.class, "third");
        armAngleMotor = hardwareMap.get(DcMotor.class, "ford");
        armMotor = hardwareMap.get(DcMotor.class, "moto_moto");
        wrist = hardwareMap.get(DcMotor.class, "wrist");
        // voltageSensor voltSensor = hardwareMap.voltageSensor.get("Motor Controller
        // 1");
        /*
         * voltageSensor would be for later use in order to set the power correctly
         * and more consistantly
         */

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        boolean status = true;
        // add arm motor hardwareMap here later
        initDoubleVision();
        waitForStart();
        if (opModeIsActive()) {

            backward(0.5);

            while (something == 0) {
                telemetryAprilTag();

                telemetry.update();
            }

            if (status) {
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
                    buffer(0.4);
                    centerRobot(); // might need to take out
                    buffer(0.3);
                    setArmAngle(true);
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
                    buffer(0.4);
                    centerRobot();
                    buffer(0.3);
                    setArmAngle(true);
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
                    buffer(0.4);
                    centerRobot();
                    buffer(0.3);
                    setArmAngle(true);
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
                    buffer(0.4);
                    centerRobot();
                    buffer(0.3);
                    setArmAngle(true);
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
                xPos = detection.ftcPose.x;
                something = detection.id;
                getMetaString = (String) (detection.metadata.name);
                currentDetections.clear();
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
            while (timer.seconds() <= 1.5) {
                move(1, -1, -1, 1);
            }
        } else {
            /*
             * this time can be different depending on what motors
             * you're using or how much power is set to the motor
             */
            // telemetry.addData("moving right.");
            while (timer.seconds() <= 1.5) {
                move(-1, 1, 1, -1);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    // FR FL BL BR
    // currently not working correctly
    public void strafe(boolean strafeLeft /* true = left, false = right */, double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (strafeLeft) {
            while (timer.seconds() <= time) {
                move(-1.0, 1.0, -1.0, 1.0);
            }
        } else {
            while (timer.seconds() <= time) {
                move(1.0, -1.0, 1.0, -1.0);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void strafe1(boolean strafeLeft) {
        if (strafeLeft) {
            while (strafeLeft) {
                move(-1.0, 1.0, -1.0, 1.0);
            }
        } else {
            while (!strafeLeft) {
                move(1.0, -1.0, 1.0, -1.0);
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

    public void setWristPos (bool isMax) {
        int wristStep = wristCurrentPosition - wristPreviousPosition;
        wristCurrentPosition = wrist.getCurrentPosition();
        
        switch (n) {
            case true:
                while (wristCurrentPosition + wristStep < wristMax) {
                    wrist.setPower(0.3);
                }
                break;
            case false:
                while (wristCurrentPosition + wristStep > wristMin) {
                    wrist.setPower(-0.3);
                }
                break;
        }

        wristPreviousPosition = wristCurrentPosition;
            
    }


    public void setArmAngle(boolean isMax /* true = max, false = min */) {

        int armStep = armAngleCurrentPosition - anglePreviousPosition;

        armAngleCurrentPosition = armAngleMotor.getCurrentPosition();

        telemetry.addLine("arm angle motor current pos: " + armAngleCurrentPosition);

        if (isMax) {
            while (armAngleMotor.getCurrentPosition() + armStep > -830 /*
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
        anglePreviousPosition = armAngleCurrentPosition;
    }

    public void setArmPosition(boolean isMax /* true = max, false = min */) {
        int armStep = armCurrentPosition - armPreviousPosition;

        armCurrentPosition = armMotor.getCurrentPosition();

        // telemetry.addLine("arm motor current pos: " + armCurrenPosition);

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

    // possible integration of centering a qr code
    public void centerRobot() {
        if (getMetaString.indexOf("Blue") != -1 || getMetaString.indexOf("Red") != -1) {
            double targetXPos = 0.1;
            double tolerance = 0.1;

            while (aprilTag.getDetections().isEmpty()) {
                telemetry.addData("No new QR code detected. Searching...", xPos); // this should make it so that it
                                                                                  // checks for a new aprilTag, but
                                                                                  // we'll see...
                telemetry.update();
                telemetryAprilTag();
            }

            while (Math.abs(xPos - targetXPos) > tolerance || Math.abs(xPos - targetXPos) < -tolerance) {
                if (xPos < targetXPos) {
                    strafe(true, 0.1);
                    telemetry.addData("going left", xPos);
                    telemetry.update();
                } else if (xPos > targetXPos) {
                    strafe(false, 0.1);
                    telemetry.addData("going right", xPos);
                    telemetry.update();
                } else {
                    break; // idk if this will work
                }
            move(0.0, 0.0, 0.0, 0.0);
        }
    }
}
