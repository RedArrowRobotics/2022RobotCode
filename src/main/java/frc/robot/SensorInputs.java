package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class SensorInputs {
    private final int lowerIntakeSensorDioId = 1;
    private final int upperIntakeSensorDioId = 2;

    private DigitalInput lowerIntakeSensor = new DigitalInput(lowerIntakeSensorDioId);
    private DigitalInput upperIntakeSensor = new DigitalInput(upperIntakeSensorDioId);

    public boolean lowerBallPresent = false;
    public boolean upperBallPresent = false;

    public final void readSensors()
    {
        lowerBallPresent = lowerIntakeSensor.get();
        upperBallPresent = upperIntakeSensor.get();
    }

}
