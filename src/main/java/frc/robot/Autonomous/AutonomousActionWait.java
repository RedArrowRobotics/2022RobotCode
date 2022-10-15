package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionWait extends AutonomousAction {

    private int cycleCount = 0;
    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        cycleCount++;
        return (cycleCount > 50);
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        // TODO Auto-generated method stub
        
    }
    
}
