package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.SensorInputs;

public class AutonomousActionDoNothing extends AutonomousAction {
    @Override
    public boolean Execute(Components components, SensorInputs sensors) {
        return false;
    }
}
