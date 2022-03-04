package frc.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorInputs {
    private final int lowerIntakeSensorDioId = 1;
    private final int upperIntakeSensorDioId = 2;

    private DigitalInput lowerIntakeSensor = new DigitalInput(lowerIntakeSensorDioId);
    private DigitalInput upperIntakeSensor = new DigitalInput(upperIntakeSensorDioId);
    private AnalogInput ultrasonic = new AnalogInput(0);
    private AnalogInput ultrasonicMB7051 = new AnalogInput(1);

    public boolean lowerBallPresent = false;
    public boolean upperBallPresent = false;
    public double distanceToTarget = 0.0;
    public double alternateDistanceToTarget = 0.0;
    public final void readSensors()
    {
        lowerBallPresent = !lowerIntakeSensor.get();
        SmartDashboard.putBoolean("Intake Sensor - Lower", lowerBallPresent);
        upperBallPresent = !upperIntakeSensor.get();
        SmartDashboard.putBoolean("Intake Sensor - Upper", upperBallPresent);
        distanceToTarget = ultrasonic.getVoltage();
        SmartDashboard.putNumber("Distance To Target", distanceToTarget);
        alternateDistanceToTarget = ( ultrasonicMB7051.getVoltage()/0.07463 )+1;
        SmartDashboard.putNumber("Distance To Target - MB7051", alternateDistanceToTarget);

    }

}
