# Robotics Code Repository

Welcome to the repository for our robotics code! This codebase is designed for the FTC Robotics competitions and is mainly tailored to our robot specifically (#15081).

## Files

### 1. AutonomouseTetrix.java
This class was made for mecanum drive trains, it has built in methods such as forward, backward, strafe, strafe1, turn, etc... 
you may have to change the positive and negative signs within the move method in order for it to work properly. do keep in mind that the code is solely time based as well.
If you have specific questions about the implementation or functionality of this class, feel free to ask.

### 2. Drive.java
The Drive class is responsible for piloting a robot with two wheels, there is some logic within that has been tailored toward this seasons game so a recommendation would be to 
comment any logic that does not pertain to your competition. If you have any inquiries or need clarification on how this class operates, don't hesitate to reach out.

### 3. MecanumDrive.java
MecanumDrive.java is focused on a drive train with mecanum wheels this also has custom tailored logic and code within it, although it is overall fantastic drive code for mecanum wheels.
For more details on its functionality or if you need assistance understanding its role, feel free to ask questions.

### 4. autonimouse.java
The autonimouse.java class handles autonomous code that uses a dual wheel drive train, although I have not updated it so that it has vision quite yet.
If you have specific queries or require more information about this class, feel free to reach out.

## How to Use
  Drive.java & MecanumDrive.java:
    these two files are controlled with a logitech controller, the left stick is used for moving forward, backward and strafing (left, right, and diagonally).
    The right stick's x direction is used to turn the robot left and right.
    Trigger left and right are used for our lift system on our arm.
    Dpad up and down are used to adjust the arm angle.

  AutonomouseTetrix.java & autonimouse.java:
    
    ### 1. initDoubleVision():
    Configures AprilTag and TensorFlow Object Detection (TFOD) processors. Initializes the vision portal with the webcam and processors.
    
    ### 2. telemetryAprilTag():
    Retrieves AprilTag detections and displays information about each detected tag.
    
    ### 3. move(double FR, double FL, double BL, double BR):
    Moves the robot based on motor power values for front right, front left, back left, and back right motors.
    
    ### 4. debug(double FR, double FL, double BL, double BR, double time):
    Allows debugging by setting motor powers for a specified duration.
    
    ### 5. backward(double time):
    Moves the robot backward for a specified duration.
    
    ### 6. forward(double time):
    Moves the robot forward for a specified duration.
    
    ### 7. turn(boolean isLeft):
    Rotates the robot left or right based on the boolean parameter.
    
    ### 8. strafe(boolean strafeLeft, double time):
    Strafes the robot left or right for a specified duration.
    
    ### 9. strafe1(boolean strafeLeft):
    Strafes the robot continuously left or right until the condition is met.
    
    ### 10. buffer(double time):
    Stops the robot in place for a specified duration.
    
    ### 11. setArmAngle(boolean isMax):
    Sets the position of the arm angle motor to either the maximum or minimum position.
    
    ### 12. setArmPosition(boolean isMax):
    Sets the position of the arm motor to either the maximum or minimum position.
    
    ### 13. centerRobot():
    Adjusts the robot's position to center it based on the detected x-position of an AprilTag.
    
    Please note that these methods might need adjustments based on your specific hardware and requirements.
    
## Contributing
If you'd like to contribute to the development of this robotics code, we welcome your input! Please email me with any suggestions or recommendations!

## Issues
If you encounter any issues or have suggestions for improvement, please create a new issue in the [Issues section]: https://github.com/RobWard1232/FTC-Autonomous-and-Drive-Code-2023-2024/issues.
We appreciate your feedback!
