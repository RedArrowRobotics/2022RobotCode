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

    public boolean lowerBallPresent = false;
    public boolean upperBallPresent = false;
    public double distanceToTarget = 0.0;

    public final void readSensors()
    {
        lowerBallPresent = !lowerIntakeSensor.get();
        SmartDashboard.putBoolean("Intake Sensor - Lower", lowerBallPresent);
        upperBallPresent = !upperIntakeSensor.get();
        SmartDashboard.putBoolean("Intake Sensor - Upper", upperBallPresent);
        distanceToTarget = ultrasonic.getVoltage()/0.3;
        SmartDashboard.putNumber("Distance To Target", distanceToTarget);
    }

}
