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
    private double firstQuarterPositionOffset;
    private double lastQuarterPositionOffset;

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        startRightPosition = driveTrain.getFrontRightPosition();
        startLeftPosition = driveTrain.getFrontLeftPosition();
        
        positionDelta = 30;
        firstQuarterPositionOffset = positionDelta / 4;
        lastQuarterPositionOffset = firstQuarterPositionOffset * 3;
        SmartDashboard.putNumber("Auto Move Back - Start Right Position", startRightPosition);
        SmartDashboard.putNumber("Auto Move Back - Start Left Position", startLeftPosition);
        
        SmartDashboard.putNumber("Auto Move Back - First Quarter Position Offset", firstQuarterPositionOffset);
        SmartDashboard.putNumber("Auto Move Back - Last Quarter Position Offset", lastQuarterPositionOffset);
        
        endRightPosition = startRightPosition + positionDelta;
        endLeftPosition = startLeftPosition + positionDelta;

        SmartDashboard.putNumber("Auto Move Back - End Right Position", endRightPosition);
        SmartDashboard.putNumber("Auto Move Back - End Left Position", endLeftPosition);
        
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.compressor.disable();
        double currentFrontRightPosition = driveTrain.getFrontRightPosition();
        double currentFrontLeftPosition = driveTrain.getFrontLeftPosition();
        SmartDashboard.putNumber("Auto Move Back - Front Right Position", currentFrontRightPosition);
        SmartDashboard.putNumber("Auto Move Back - Front Left Position", currentFrontLeftPosition);
        if ( (currentFrontLeftPosition > endLeftPosition) &&
           (currentFrontRightPosition > endRightPosition) )
        {
            SmartDashboard.putBoolean("DB/LED 0", true);
            return true;
        } 

        double currentOffsetFromStart = Math.abs(currentFrontRightPosition - startRightPosition); 
        SmartDashboard.putNumber("Auto Move Back - Current Offset", currentOffsetFromStart);
        
        SmartDashboard.putBoolean("DB/LED 1", false);
        SmartDashboard.putBoolean("DB/LED 2", false);
        SmartDashboard.putBoolean("DB/LED 3", false);
        
        double driveSpeed = 0.0;
        if ( currentOffsetFromStart <= firstQuarterPositionOffset)
        {
            SmartDashboard.putBoolean("DB/LED 1", true);
            driveSpeed = currentOffsetFromStart / (firstQuarterPositionOffset) / 2 + .25; 
        }
        if (currentOffsetFromStart >= lastQuarterPositionOffset)
        {
            SmartDashboard.putBoolean("DB/LED 3", true);
            driveSpeed = ((positionDelta - currentOffsetFromStart) / (firstQuarterPositionOffset) / 2 + .25); 
        }
        if ( (currentOffsetFromStart > firstQuarterPositionOffset) && 
        (currentOffsetFromStart < lastQuarterPositionOffset) )
        {
            SmartDashboard.putBoolean("DB/LED 2", true);
            driveSpeed = .75;
        }
        SmartDashboard.putNumber("Auto Move Back - Drive Speed", driveSpeed);
        driveTrain.arcadeDrive(driveSpeed, 0.0);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
    
}
