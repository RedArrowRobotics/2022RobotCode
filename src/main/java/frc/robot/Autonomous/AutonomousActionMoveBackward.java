package frc.robot.Autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        SmartDashboard.putNumber("Auto Move Back - Start Right Position", startRightPosition);
        SmartDashboard.putNumber("Auto Move Back - Start Left Position", startLeftPosition);
        
        endRightPosition = startRightPosition - positionDelta;
        endLeftPosition = startLeftPosition - positionDelta;

        SmartDashboard.putNumber("Auto Move Back - End Right Position", endRightPosition);
        SmartDashboard.putNumber("Auto Move Back - End Left Position", endLeftPosition);
        
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        double currentFrontRightPosition = driveTrain.getFrontRightPosition();
        double currentFrontLeftPosition = driveTrain.getFrontLeftPosition();
        SmartDashboard.putNumber("Auto Move Back - Front Right Position", currentFrontRightPosition);
        SmartDashboard.putNumber("Auto Move Back - Front Left Position", currentFrontLeftPosition);
        if ( (currentFrontLeftPosition < endRightPosition) &&
           (currentFrontRightPosition < endLeftPosition) )
        {
            return true;
        } 

        double currentOffsetFromStart = Math.abs(currentFrontRightPosition - startRightPosition); 
        SmartDashboard.putNumber("Auto Move Back - Current Offset", currentOffsetFromStart);
        
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
        SmartDashboard.putNumber("Auto Move Back - Drive Speed", driveSpeed);
        driveTrain.arcadeDrive(0-driveSpeed, 0.0);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
    
}
