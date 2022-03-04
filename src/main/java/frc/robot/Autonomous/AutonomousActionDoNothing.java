package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionDoNothing extends AutonomousAction {
    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
    
    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.compressor.disable();
        driveTrain.arcadeDrive(0.0, 0.0);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
}
