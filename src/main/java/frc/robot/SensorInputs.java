package frc.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorInputs {
    private final int lowerIntakeSensorDioId = 1;
    private final int upperIntakeSensorDioId = 2;

    private DigitalInput lowerIntakeSensor = new DigitalInput(lowerIntakeSensorDioId);
    private DigitalInput upperIntakeSensor = new DigitalInput(upperIntakeSensorDioId);
    private AnalogInput foo = new AnalogInput(0);

    public boolean lowerBallPresent = false;
    public boolean upperBallPresent = false;

    public final void readSensors()
    {
        SmartDashboard.putNumber("foo", foo.getVoltage());
        lowerBallPresent = lowerIntakeSensor.get();
        SmartDashboard.putBoolean("Lower Intake Sensor", lowerBallPresent);
        upperBallPresent = upperIntakeSensor.get();
        SmartDashboard.putBoolean("Upper Intake Sensor", upperBallPresent);
    }

}
