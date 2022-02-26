package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionMoveForward extends AutonomousAction {
    private double startRightPosition; 
    private double startLeftPosition;
    private double endRightPosition;
    private double endLeftPosition;

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        startRightPosition = driveTrain.getFrontRightPosition();
        startLeftPosition = driveTrain.getFrontLeftPosition();
        
        endRightPosition = startRightPosition + 127.5;
        endLeftPosition = startLeftPosition + 127.5;
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        if ( (driveTrain.getFrontRightPosition() > endRightPosition) &&
           (driveTrain.getFrontLeftPosition() > endLeftPosition) )
        {
            return true;
        } 
        driveTrain.arcadeDrive(0.75, 0.0);
        return false;
    }
    
}
