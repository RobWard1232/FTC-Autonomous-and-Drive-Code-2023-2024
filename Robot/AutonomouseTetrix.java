package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
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
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
//import org.openftc.easyopencv.OpenCvCameraFactory;

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
    private Servo servo;
    private Servo servo1;

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
    private int armAngleMaxPosition = 2000; // Safer than Zero
    private int AdangerZoneOffsetUp = 0;
    private int AdangerZoneOffsetDown = 100;
    
    private int wristPreviousPosition = 0;
    private int wristCurrentPosition = 0;
    private int wristMinPosition = 50;
    private int wristMaxPosition = 100;
    
    private double servoPos0 = 0.25;
    private double servoPos1 = 0.499999999;
    
    private double xPos;

    /**
     * The variable to store our instance of the TensorFlow Object Detection
     * processor.
     */
    //private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;
    
    //private static final boolean USE_WEBCAM = true;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "first");
        frontRight = hardwareMap.get(DcMotor.class, "zeroth");
        backLeft = hardwareMap.get(DcMotor.class, "third");
        backRight = hardwareMap.get(DcMotor.class, "second");
        armAngleMotor = hardwareMap.get(DcMotor.class, "ford");
        armMotor = hardwareMap.get(DcMotor.class, "moto_moto");
        wrist = hardwareMap.get(DcMotor.class, "wrist");
        servo = hardwareMap.get(Servo.class, "serv0");
        servo1 = hardwareMap.get(Servo.class, "serv1");
        // voltageSensor voltSensor = hardwareMap.voltageSensor.get("Motor Controller
        // 1");
        /*
         * voltageSensor would be for later use in order to set the power correctly
         * and more consistantly
         */

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
        //status is what side you are on true = blue
        boolean status = true;
        boolean isShort = true;
        
        boolean debug = true;
        
        waitForStart();
        // Set the minimum confidence at which to keep recognitions.
        //tfod.setMinResultConfidence((float) 0.75);
        if (opModeIsActive()) {
            //telemetry.addline("wristPosition: ", wrist.getCurrentPosition());
            if (status) {
                if (isShort) { // change to specified areas later
                    backward(1.2);
                    buffer(0.3);
                    turn(true);
                    buffer(0.3);
                    backward(2.5);// might have to change later.
                    buffer(0.3);
                    //setArmAngle(true);
                } else if (!isShort) {
                    backward(1.2);
                    buffer(0.3);
                    turn(false);
                    buffer(0.3);
                    backward(6);// might have to change later.
                    buffer(0.3);
                    //setArmAngle(true);
                }
            } else {
                if (isShort) {
                    backward(1.2);
                    buffer(0.3);
                    turn(true);
                    buffer(0.3);
                    backward(2.5);
                    buffer(0.3);
                    //setArmAngle(true);
                } else if (!isShort) {
                    backward(1.2);
                    buffer(0.3);
                    turn(false);
                    buffer(0.3);
                    backward(6);
                    buffer(0.3);
                    //setArmAngle(true);
                }
            }
        }
    }


    public void move(double FR, double FL, double BL, double BR) {
        frontLeft.setPower(-FL * 0.5);
        frontRight.setPower(FR * 0.5);
        backLeft.setPower(-BL * 0.5);
        backRight.setPower(BR * 0.5);
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
                //frflblbr
                move(-1, 1, 1, -1);
            }
        } else {
            /*
             * this time can be different depending on what motors
             * you're using or how much power is set to the motor
             */
            // telemetry.addData("moving right.");
            while (timer.seconds() <= 1.5) {
                move(1, -1, -1, 1);
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

    public void setArmAngle(boolean isMax /* true = max, false = min */) {

        int armStep = armAngleCurrentPosition - armPreviousPosition;

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
        armAnglePreviousPosition = armAngleCurrentPosition;
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
    
    public void setWrist(boolean isMax) {
        int wristStep = wristCurrentPosition - wristPreviousPosition;
        
        wristCurrentPosition = wrist.getCurrentPosition();
        
        if (isMax) {
            while (wristCurrentPosition + wristStep < wristMaxPosition) {
                wrist.setPower(0.3);
            }
            wrist.setPower(0.0);
        } else {
            while (wristCurrentPosition + wristStep > wristMinPosition) {
                wrist.setPower(-0.3);
            }
            wrist.setPower(0.0);
        }
        
        wristPreviousPosition = wristCurrentPosition;
    }
    
    public void setServo (boolean left, boolean right) {
        if (left && !right) {
            
        }
    }


}
