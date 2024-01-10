//**WORK IN PROGRESS!!**//

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

@TeleOp // should possibly be @autonomous according to youtube
public class MecanumDrive extends OpMode {

    //Servo servo_claw_arm = null;
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;
    DcMotor sweep = null;
    DcMotor armAngleMotor = null;
    //DcMotor lift = null;
    DcMotor armMotor = null;

    int armCurrentPosition = 0;
    int armAnglePosition = 0;
    int armAnglePrevPos = 0;
    int armPreviousPosition = 0;
    int armMinPosition = 0;
    int armMaxPosition = -1111;//motor telemetry is backwards
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

    double drivePower = 1.0;

    public void init() {

        //servo_claw_arm = hardwareMap.servo.get("servo_claw_arm");
        leftFront = hardwareMap.get(DcMotor.class, "zeroth");
        rightFront = hardwareMap.get(DcMotor.class, "first");
        leftRear = hardwareMap.get(DcMotor.class, "third");
        rightRear = hardwareMap.get(DcMotor.class, "second");
        armMotor = hardwareMap.get(DcMotor.class, "moto_moto");
        armAngleMotor = hardwareMap.get(DcMotor.class, "ford");
        sweep = hardwareMap.get(DcMotor.class, "fifd");
        //servo_claw_arm = hardwareMap.get(Servo.class, "Servo");
        

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void loop() {
        
        int armStep = armCurrentPosition - armPreviousPosition;

        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        //might need to change +- later :)
        leftFront.setPower(y + x + rx);
        leftRear.setPower((y - x + rx));
        rightFront.setPower(y - x - rx);
        //sometimes you need to add * -1.0 to the end of it idk why.
        rightRear.setPower((y + x - rx)); //lol.

        // arm
        // code---------------------------------------------------------------------------------
        armCurrentPosition = armMotor.getCurrentPosition();
        armStep = armCurrentPosition - armPreviousPosition; // -step means we are moving in the negative direction
                                                                // (which is up)

        telemetry.addData("current pos", armCurrentPosition);
        telemetry.addData("sstep", armStep);
        telemetry.addData("bool1", armCurrentPosition + armStep > armMaxPosition);
        telemetry.addData("bool2", armCurrentPosition + armStep < armMinPosition);
        telemetry.update();

        // Up on the left stick is negative for some stupid reason

        // Go Up      comparison signs are flipped because telemetry is reading negative numbers
        if (gamepad1.right_trigger > 0.01 && armCurrentPosition + armStep > armMaxPosition) {
                armMotor.setPower(-gamepad1.right_trigger);
        } else if (gamepad1.left_trigger > 0.01 && armCurrentPosition + armStep < armMinPosition) {//Go Down
                armMotor.setPower(gamepad1.left_trigger);
        } else {
            armMotor.setPower(0.0);
        }

        // arm angle code
        if (gamepad1.dpad_up ){//&& armAngleCurrentPosition + armStep > armAngleMaxPosition) {
            // open servo
            armAngleMotor.setPower(0.5);
        } else if (gamepad1.dpad_down ){//&& armAngleCurrentPosition + armStep < armAngleMinPosition) {
            // close servo
            armAngleMotor.setPower(-0.5);
        } else {
            armAngleMotor.setPower(0.0);
        }

        // lift
        /*if (gamepad1.y && armCurrentPosition < armMaxPosition) {// up
            lift.setPower(1.0);
        } else if (gamepad1.a && armCurrentPosition > armMinPosition) {
            lift.setPower(-1.0);
        }*///for later use.

        // sweep code
        if (gamepad1.right_bumper) {// up
            sweep.setPower(1.0); // do not change power or else it will explode
        } else if (gamepad1.left_bumper) {// down
            sweep.setPower(-1.0);
        } else {
            sweep.setPower(0.0);
        }

        armAnglePreviousPosition = armAngleCurrentPosition; // Capture the current position for future use
        armPreviousPosition = armCurrentPosition; // Capture the current position for future use

    }
}
