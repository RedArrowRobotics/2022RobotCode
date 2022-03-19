package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class ControlInputs {
    
    private final int driveStickDeviceId = 0;
    private final int mechanismStick1DeviceId = 1;
    private final int mechanismStick2DeviceId = 2;
    
    private final int testRunShooterButtonId = 1;
    private final int testtransferBeltButtonId = 2;

    private final int highAdaptiveShooterButtonId = 12;
    private final int intakeBeltButtonId = 1;
    private final int lowShooterButtonId = 10;
    private final int highShooterButtonId = 11;
    private final int overRideShooterButtonId = 2;
    private final int overRideBeltsButtonId = 3;
    private final int dumpBallsButtonId = 6;

    private final int claws1ButtonOpenId = 1; 
    private final int claws2ButtonOpenId = 2;
    private final int claws1ButtonCloseId = 3; 
    private final int claws2ButtonCloseId = 4;

    private final Joystick driveStick = new Joystick(driveStickDeviceId);    
    private final Joystick mechanismStick1 = new Joystick(mechanismStick1DeviceId);
    private final Joystick mechanismStick2 = new Joystick(mechanismStick2DeviceId);

    public boolean deployIntake = false;
    public boolean runIntake = false;
    public boolean shootBall = false;
    public boolean shootLow = false;
    public boolean shootHigh = false;
    public boolean shootAdaptiveHigh = false;
    public boolean runCompressor = false;
    public double driveStickX = 0.0;
    public double driveStickY = 0.0;
    public boolean switchToBasicComponentControl = false;
    public boolean switchToSensorComponentControl = false;
    public boolean testShooter = false;
    public boolean runTransferBelt = false;
    public int shotType = -1;
    public boolean overRideShooter = false;
    public boolean overRideBelts = false;
    public boolean dumpBalls = false;
    public double climbRotation = 0.0;
    public boolean claws1Button_Open = false;
    public boolean claws2Button_Open = false;
    public boolean claws1Button_Close = false;
    public boolean claws2Button_Close = false;
    public boolean climbEnableButton = false;

    public final void readControls()
    {
        driveStickX = driveStick.getX();
        driveStickY = driveStick.getY();
        
        runCompressor = driveStick.getZ() > 0.0;

        deployIntake = !(mechanismStick2.getY() > 0.0);
        runIntake = mechanismStick2.getRawButton(intakeBeltButtonId);
        shootLow = mechanismStick2.getRawButton(lowShooterButtonId);
        shootHigh = mechanismStick2.getRawButton(highShooterButtonId);
        shootAdaptiveHigh = mechanismStick2.getRawButton(highAdaptiveShooterButtonId);
        shootBall = shootLow || shootHigh || shootAdaptiveHigh;
        if (driveStick.getRawButtonPressed(overRideShooterButtonId))
        {
            overRideShooter = !overRideShooter;
        }
        overRideBelts = driveStick.getRawButton(overRideBeltsButtonId);

        if (!shootBall) shotType = -1;
        if (shootLow) shotType = 0;
        if (shootHigh) shotType = 1;
        if (shootAdaptiveHigh) shotType = 2;
        
        //switchToBasicComponentControl = mechanismStick1.getRawButton(1);
        //switchToSensorComponentControl = mechanismStick1.getRawButton(12);

        testShooter = mechanismStick2.getRawButton(testRunShooterButtonId);
        runTransferBelt = mechanismStick2.getRawButton(testtransferBeltButtonId);
        dumpBalls = driveStick.getRawButton(dumpBallsButtonId);
        
        climbRotation = mechanismStick1.getY();
        claws1Button_Open = mechanismStick1.getRawButton(claws1ButtonOpenId);
        claws2Button_Open = mechanismStick1.getRawButton(claws2ButtonOpenId);
        claws1Button_Close = mechanismStick1.getRawButton(claws1ButtonCloseId);
        claws2Button_Close = mechanismStick1.getRawButton(claws2ButtonCloseId);
        climbEnableButton = driveStick.getRawButton(1);
    }
}
