package frc.robot.Autonomous;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionMoveBackward extends AutonomousAction {
    private double startRightPosition; 
    private double startLeftPosition;
    private double endRightPosition;
    private double endLeftPosition;
    private double positionDelta;
    private double firstQuarterPosition;
    private double lastQuarterPosition;

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        startRightPosition = driveTrain.getFrontRightPosition();
        startLeftPosition = driveTrain.getFrontLeftPosition();
        
        positionDelta = 65;
        firstQuarterPosition = positionDelta / 4;
        lastQuarterPosition = firstQuarterPosition * 3;

        endRightPosition = startRightPosition - positionDelta;
        endLeftPosition = startLeftPosition - positionDelta;
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        double currentFrontRightPosition = driveTrain.getFrontRightPosition();
        double currentFrontLeftPosiiton = driveTrain.getFrontLeftPosition();
        
        if ( (currentFrontLeftPosiiton < endRightPosition) &&
           (currentFrontRightPosition < endLeftPosition) )
        {
            return true;
        } 

        double currentOffsetFromStart = currentFrontRightPosition - startRightPosition; 

        double driveSpeed = 0.0;
        if ( currentOffsetFromStart <= firstQuarterPosition)
        {
            driveSpeed = currentOffsetFromStart / (firstQuarterPosition) + .5; 
        }
        if (currentOffsetFromStart >= lastQuarterPosition)
        {
            driveSpeed = -(positionDelta - currentOffsetFromStart) / (firstQuarterPosition) + 1; 
        }
        if ( (currentOffsetFromStart > firstQuarterPosition) && 
        (currentOffsetFromStart < lastQuarterPosition) )
        {
            driveSpeed = 1.0;
        }

        driveTrain.arcadeDrive(0-driveSpeed, 0.0);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
    
}
