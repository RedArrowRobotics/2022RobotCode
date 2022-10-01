package frc.robot.Autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionMoveAndCaptureSlow extends AutonomousAction {
    private double startRightPosition; 
    private double startLeftPosition;
    private double endRightPosition;
    private double endLeftPosition;
    private double rotationsToTravel;
    private double firstQuarterPositionOffset;
    private double lastQuarterPositionOffset;

    public AutonomousActionMoveAndCaptureSlow(double inchesToTravel)
    {
        rotationsToTravel = inchesToTravel * 12.75 / (Math.PI * 6);
    }

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        startRightPosition = driveTrain.getFrontRightPosition();
        startLeftPosition = driveTrain.getFrontLeftPosition();
        
        firstQuarterPositionOffset = rotationsToTravel / 4;
        lastQuarterPositionOffset = firstQuarterPositionOffset * 3;
        SmartDashboard.putNumber("Auto Move Back - Start Right Position", startRightPosition);
        SmartDashboard.putNumber("Auto Move Back - Start Left Position", startLeftPosition);
        
        SmartDashboard.putNumber("Auto Move Back - First Quarter Position Offset", firstQuarterPositionOffset);
        SmartDashboard.putNumber("Auto Move Back - Last Quarter Position Offset", lastQuarterPositionOffset);
        
        endRightPosition = startRightPosition + rotationsToTravel;
        endLeftPosition = startLeftPosition + rotationsToTravel;

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
        
        boolean leftMoveCompleted = currentFrontLeftPosition > endLeftPosition;
        boolean rightMoveCompleted = currentFrontRightPosition > endRightPosition;
        boolean ballCaptured = sensors.lowerBallPresent;

        if ( ( leftMoveCompleted && rightMoveCompleted)  ||  
            ballCaptured)
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
            driveSpeed = currentOffsetFromStart / (firstQuarterPositionOffset) / 4 + .25; 
        }
        if (currentOffsetFromStart >= lastQuarterPositionOffset)
        {
            SmartDashboard.putBoolean("DB/LED 3", true);
            driveSpeed = ((rotationsToTravel - currentOffsetFromStart) / (firstQuarterPositionOffset) / 4 + .25); 
        }
        if ( (currentOffsetFromStart > firstQuarterPositionOffset) && 
        (currentOffsetFromStart < lastQuarterPositionOffset) )
        {
            SmartDashboard.putBoolean("DB/LED 2", true);
            driveSpeed = .5;
        }

        if (currentOffsetFromStart > ( firstQuarterPositionOffset) )
        {
            components.intakeArmControl.set(true); 
            components.intakeRollerMotor.set(-1.0);    
        }
        else
        {
            components.intakeArmControl.set(false);
            components.intakeRollerMotor.set(0.0);
        }

        SmartDashboard.putNumber("Auto Move Back - Drive Speed", driveSpeed);
        driveTrain.arcadeDrive(driveSpeed, 0.0);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.intakeArmControl.set(false);
        components.intakeRollerMotor.set(0.0);
        driveTrain.arcadeDrive(0.0, 0.0);    
    }
    
}
