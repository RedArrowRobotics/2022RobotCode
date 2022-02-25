package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class ControlInputs {
    
    private final int driveStickDeviceId = 0;
    private final int mechanismStick1DeviceId = 1;
    private final int mechanismStick2DeviceId = 2;
    
    private final int testRunShooterButtonId = 1;
    private final int testtransferBeltButtonId = 2;

    private final int highAdaptiveShooterButtonId = 9;
    private final int intakeBeltButtonId = 10;
    private final int lowShooterButtonId = 11;
    private final int highShooterButtonId = 12;

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

    public final void readControls()
    {
        driveStickX = driveStick.getX();
        driveStickY = driveStick.getY();
        
        runCompressor = driveStick.getZ() > 0.0;

        deployIntake = mechanismStick2.getY() > 0.0;
        runIntake = mechanismStick2.getRawButton(intakeBeltButtonId);
        shootLow = mechanismStick2.getRawButton(lowShooterButtonId);
        shootHigh = mechanismStick2.getRawButton(highShooterButtonId);
        shootAdaptiveHigh = mechanismStick2.getRawButton(highAdaptiveShooterButtonId);
        shootBall = shootLow || shootHigh || shootAdaptiveHigh;
        
        if (!shootBall) shotType = -1;
        if (shootLow) shotType = 0;
        if (shootHigh) shotType = 1;
        if (shootAdaptiveHigh) shotType = 2;
        
        switchToBasicComponentControl = mechanismStick1.getRawButton(1);
        switchToSensorComponentControl = mechanismStick1.getRawButton(12);

        testShooter = mechanismStick2.getRawButton(testRunShooterButtonId);
        runTransferBelt = mechanismStick2.getRawButton(testtransferBeltButtonId);
    }
}
