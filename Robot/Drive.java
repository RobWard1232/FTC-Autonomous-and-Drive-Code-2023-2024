//creator: super duper awesome ultimate fantastic number 1 Robert Ward IV
//Date: 06/11/23 (day/month/year) -the way dates should be written.
//purpose: drive code with comments to hopefully help the succesors

//package for teamcode (different from imports DO NOT PUT THEM TOGETHER)
package org.firstinspires.ftc.teamcode;

//imports tell the file/program what libraries it can use within it.
//generally these are all the libraries you'll need but you may need more in the future idk.
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

//tells the file where to go, whether it is user controlled or autonomous
@TeleOp
// class declaration for the file, extends means it has the capability of using
// the created method within OpMode.
public class Drive extends OpMode {

    int armCurrentPosition = 0;
    int armAnglePosition = 0;
    int armAnglePrevPos = 0;
    int armPreviousPosition = 0;
    int armMinPosition = -962;
    int armMaxPosition = -55; // Safer than Zero
    int dangerZoneOffsetUp = 0;
    int dangerZoneOffsetDown = 100;

    int liftCurrentPos;
    int liftPrevPos;
    int liftMinPos;
    int liftMaxPos;

    int armAngleCurrentPosition = 0;
    int armAnglePreviousPosition = 0;
    int angleCurrentPosition = 0;
    int anglePreviousPosition = 0;
    int armAngleMinPosition = 0;
    int armAngleMaxPosition = 1200; // Safer than Zero
    int AdangerZoneOffsetUp = 0;
    int AdangerZoneOffsetDown = 100;

    double servoPos = 0.25;

    double dangerZoneThrottle = 0.15;
    double AdangerZoneThrottle = 0.15;

    boolean previous = false;

    // declaration of the two motors on the robot
    DcMotor Left = null;
    DcMotor Right = null;
    DcMotor armMotor = null;
    DcMotor armAngleMotor = null;
    DcMotor lift = null; // is kinda close to left motor so it is uncapitalized :)
    Servo someServo = null;

    // creating a variable to set for the drivePower 1 is max
    double drivePower = 1.0;

    // init (short for initialize) is a proprietary method, do not try to create a
    // different kind.
    // init is where you create something called a hardwareMap for each of your
    // motors declared above
    public void init() {
        Left = hardwareMap.get(DcMotor.class, "zeroth");
        Right = hardwareMap.get(DcMotor.class, "first");
        armMotor = hardwareMap.get(DcMotor.class, "second");
        armAngleMotor = hardwareMap.get(DcMotor.class, "third");
        lift = hardwareMap.get(DcMotor.class, "fourd");
        someServo = hardwareMap.get(Servo.class, "servo");

        // this does something
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armAngleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armAngleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armAngleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // we use this built in method to set the power state at 0 if nothing is
        // happening.
        // this is just safe and a good practice.
        Left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // loop is another proprietary method do not try to clone within the same class.
    // loop tells the robot to continue looking for an input from the gamepads
    // (controllers)
    public void loop() {

        // armCurrentPosition = armMotor.getCurrentPosition();
        // armAngleCurrentPosition = armAnglePrevPos.getCurrentPosition();
        int armStep = armCurrentPosition - armPreviousPosition; // -step means we are moving in the negative direction
                                                                // (which is up)
        int armAngleStep = angleCurrentPosition - anglePreviousPosition;

        // telemetry to see on screen
        telemetry.addData("arm position: ", armCurrentPosition);
        telemetry.addData("right trigger: ", gamepad1.right_trigger);
        telemetry.addData("left trigger: ", gamepad1.left_trigger);
        telemetry.addData("left stick x: ", gamepad1.left_stick_x);
        telemetry.addData("left stick y: ", gamepad1.left_stick_y);
        telemetry.addData("collection system closed: ", previous);
        telemetry.update();

        // side to side code
        // if statements to check what position the left stick is currently at in order
        // to tell the robot how to set the power.
        if (gamepad1.right_stick_x > 0.01) {
            Right.setPower(-gamepad1.right_stick_x);// sets power to specified motor
            Left.setPower(-gamepad1.right_stick_x);// minimum is -1.0 maximum is 1.0, you can use everything in between
                                                   // within reason.
        } else if (gamepad1.right_stick_x < -0.01) {
            Left.setPower(-gamepad1.right_stick_x);
            Right.setPower(-gamepad1.right_stick_x);
        }

        // forward and backward code
        if (gamepad1.left_stick_y < -0.01) {// forward
            Left.setPower(gamepad1.left_stick_y);
            Right.setPower(-gamepad1.left_stick_y);
        } else if (gamepad1.left_stick_y > 0.01) {// backward
            Left.setPower(gamepad1.left_stick_y);
            Right.setPower(-gamepad1.left_stick_y);
        } else {
            Left.setPower(0);
            Right.setPower(0);
        }

        // servo collection code
        if (gamepad1.y && previous != gamepad1.y) {
            servoPos *= -1;
            someServo.setPosition(servoPos + 0.75);
        }

        previous = gamepad1.y;

        // arm angle code
        if (gamepad1.right_trigger > 0.01 && armCurrentPosition + armStep > armAngleMinPosition) {
            armMotor.setPower(-gamepad1.right_trigger);
        } else if (gamepad1.left_trigger > 0.01 && armCurrentPosition + armStep < armAngleMaxPosition) {
            armMotor.setPower(gamepad1.left_trigger);
        } else {
            armMotor.setPower(0.0);
        }

        // that code
        if (gamepad1.dpad_down && armAngleCurrentPosition > armMinPosition) {
            // open servo
            armAngleMotor.setPower(0.5);
        } else if (gamepad1.dpad_up && armAngleCurrentPosition < armMaxPosition) {
            // close servo
            armAngleMotor.setPower(-0.5);
        } else {
            armAngleMotor.setPower(0);
        }

        // lift code
        if (gamepad1.right_bumper) {// up
            lift.setPower(1.0); // do not change power or else it will explode
        } else if (gamepad1.left_bumper) {// down
            lift.setPower(-1.0);
        } else {
            lift.setPower(0.0);
        }

        armAnglePreviousPosition = armAngleCurrentPosition; // Capture the current position for future use
        armPreviousPosition = armCurrentPosition; // Capture the current position for future use
    }
}