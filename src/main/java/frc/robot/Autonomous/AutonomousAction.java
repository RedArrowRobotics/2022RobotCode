package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.SensorInputs;
import frc.robot.DriveTrain;

public abstract class AutonomousAction {
    public abstract void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors);
    public abstract boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors);
    public abstract void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors);
}

