package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class ControlInputs {
    
    private final int driveStickDeviceId = 0;
    private final int mechanismStick1DeviceId = 1;
    //private final int mechanismStick2DeviceId = 2;
    
    private final int intakeDriveButtonId = 10;
    private final int lowShooterButtonId = 11;
    private final int highShooterButtonId = 12;

    private final Joystick driveStick = new Joystick(driveStickDeviceId);    
    private final Joystick mechanismStick1 = new Joystick(mechanismStick1DeviceId);
    //private final Joystick mechanismStick2 = new Joystick(mechanismStick2DeviceId);

    public boolean deployIntake = false;
    public boolean driveIntake = false;
    public boolean shootLow = false;
    public boolean shootHigh = false;
    public boolean runCompressor = false;
    public double driveStickX = 0.0;
    public double driveStickY = 0.0;

    public final void readControls()
    {
        driveStickX = driveStick.getX();
        driveStickY = driveStick.getY();
        
        runCompressor = driveStick.getZ() > 0.0;

        deployIntake = mechanismStick1.getX() > 0.0;
        driveIntake = mechanismStick1.getRawButton(intakeDriveButtonId);
        shootLow = mechanismStick1.getRawButton(lowShooterButtonId);
        shootHigh = mechanismStick1.getRawButton(highShooterButtonId);
    }
}
