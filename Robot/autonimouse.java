package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonimouse")
public class Autonimouse extends LinearOpMode {

    private DcMotor left;
    private DcMotor right;

    ElapsedTime timer;

    @Override
    public void runOpMode() {
        left = hardwareMap.get(DcMotor.class, "zeroth");
        right = hardwareMap.get(DcMotor.class, "first");

        waitForStart();
        if (opModeIsActive()) {
            telemetry.update();

            // place to write your movement
            forward(1);
            turn(true);
            forward(4);
        }
    }

    public void forward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (timer.seconds() <= time) {
            left.setPower(-1.0);
            right.setPower(1.0);
        }

        left.setPower(0.0);
        right.setPower(0.0);
    }

    // backward function :)
    public void backward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (timer.seconds() <= time) {
            left.setPower(1.0);
            right.setPower(-1.0);
        }
        left.setPower(0.0);
        right.setPower(0.0);
    }

    // **name thing better name in the future**
    public void turn(boolean thing /* true = left, false = right */) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (thing = true) {
            while (timer.seconds() <= 1.0) {
                right.setPower(-0.5);
                left.setPower(-0.5);
            }
        } else {
            /*
             * this time can be different depending on what motors
             * you're using or how much power is set to the motor
             */
            while (timer.seconds() <= 1.0) {
                right.setPower(0.5);
                left.setPower(0.5);
            }
        }
        right.setPower(0.0);
        right.setPower(0.0);
    }

    // method if you want the robot to wait in between actions
    public void buffer(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (timer.seconds() <= time) {
            right.setPower(0.0);
            left.setPower(0.0);
        }
    }
}