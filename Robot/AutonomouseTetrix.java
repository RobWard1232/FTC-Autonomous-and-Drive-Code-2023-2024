package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
    private DcMotor armAngleMotor;
    
    // add declaration for arm motors here later

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "zeroth");
        frontRight = hardwareMap.get(DcMotor.class, "first");
        backLeft = hardwareMap.get(DcMotor.class, "second");
        backRight = hardwareMap.get(DcMotor.class, "third");
        
        
        // add arm motor hardwareMap here later

        waitForStart();
        if (opModeIsActive()) {
            //telemetry.update();

            // place to write your movement underneath
            //buffer(15);
            forward(1.7);
            buffer(0.3);
            //change turn to false if you need the robot to go right instead.
            turn(false);
            buffer(0.3);
            forward(6);
            
            //for if you are closer to the board
            //strafe(true, 1.5);
        }
    }

    public void move (double FR, double FL, double BL, double BR) {
        frontLeft.setPower(-FR * 0.5);
        frontRight.setPower(FL * 0.5);
        backLeft.setPower(BL * 0.5);
        backRight.setPower(-BR * 0.5);
    }
                       //negative positive   positive.  negative
    public void debug (double FR, double FL, double BL, double BR, double time) {
        
        ElapsedTime timer = new ElapsedTime();
        
        while (timer.seconds() <= time) {
            frontLeft.setPower(FR);
            frontRight.setPower(FL);
            backLeft.setPower(BL);
            backRight.setPower(BR);
        }
    }

    public void forward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        //telemetry.addData("moving forward.");
        while (timer.seconds() <= time) {
            move(1.0, 1.0, 1.0, 1.0);
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    // backward function :)
    public void backward(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        //telemetry.addData("moving backward.");
        while (timer.seconds() <= time) {
            move(-1.0, -1.0, -1.0, -1.0);
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void turn(boolean isLeft /* true = left, false = right */) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (isLeft) {
            //telemetry.addData("moving left.");
            while (timer.seconds() <= 1.6) {
                move(1, -1, -1, 1);
            }
        } else {
            /*
             * this time can be different depending on what motors
             * you're using or how much power is set to the motor
             */
            //telemetry.addData("moving right.");
            while (timer.seconds() <= 1.6) {
                move(-1, 1, 1, -1);
            }
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    public void strafe(boolean strafeLeft /* true = left, false = right */, double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        if (strafeLeft) {
            while (timer.seconds() <= time) {
                move(-1.0, 1.0, 1.0, -1.0);
            }
        } else {
            move(1.0, -1.0, -1.0, 1.0);
        }
        move(0.0, 0.0, 0.0, 0.0);
    }

    // method if you want the robot to wait in between actions
    public void buffer(double time) {
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        //telemetry.addData("WAIT");
        while (timer.seconds() <= time) {
            move(0.0, 0.0, 0.0, 0.0);
        }
    }
}