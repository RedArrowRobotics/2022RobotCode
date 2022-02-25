package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.SensorInputs;

public abstract class AutonomousAction {
    public abstract boolean Execute(Components components, SensorInputs sensors);
}

