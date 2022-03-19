package frc.robot.Autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionStraightMove extends AutonomousAction {
    Timer actionTimer;
    double timeSpan;
    double distanceToMoveInEncoderTicks;
    double toleranceInEncoderTicks;
    double startPositionLeft;
    double startPositionRight;
    double endPositionLeft;
    double endPositionRight;
    double kPowerFactor;

    public AutonomousActionStraightMove(double distanceToMoveInInches, 
        double timeSpanInSeconds,
        double toleranceInInches,
        double kPower)
    {
        actionTimer = new Timer();
        distanceToMoveInEncoderTicks = distanceToMoveInInches * 2.96;
        toleranceInEncoderTicks = toleranceInInches * 2.96;
        timeSpan = timeSpanInSeconds;
        kPowerFactor = kPower;
    }

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        actionTimer.reset();
        actionTimer.start();
        startPositionLeft = driveTrain.getFrontLeftPosition();
        startPositionRight = driveTrain.getFrontRightPosition();
        endPositionLeft = startPositionLeft + distanceToMoveInEncoderTicks;
        endPositionRight = startPositionRight + distanceToMoveInEncoderTicks;
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        double currentTime = actionTimer.get();
        double currentPositionLeft = driveTrain.getFrontLeftPosition();
        double currentPositionRight = driveTrain.getFrontRightPosition();

        boolean timeCompleted = currentTime > timeSpan;
        boolean leftCompleted = Math.abs(currentPositionLeft - endPositionLeft) < toleranceInEncoderTicks;
        boolean rightCompleted = Math.abs(currentPositionRight - endPositionRight) < toleranceInEncoderTicks;
        if ( timeCompleted || leftCompleted || rightCompleted )
        {
            driveTrain.arcadeDrive(0.0, 0.0);
            return true;
        }

        double targetPosition = startPositionLeft + distanceToMoveInEncoderTicks * Polynomial345(currentTime / timeSpan);
        double absolutePower = kPowerFactor * Math.sqrt(Math.abs(targetPosition - currentPositionLeft));
        double resultPower = Math.copySign(absolutePower, targetPosition - currentPositionLeft);
        resultPower = Math.max(-1.0, Math.min(1.0, resultPower));
        SmartDashboard.putNumber("Auto - Current Time", currentTime);
        SmartDashboard.putNumber("Auto - Current Position", currentPositionLeft);
        SmartDashboard.putNumber("Auto - Start Position", startPositionLeft);
        SmartDashboard.putNumber("Auto - Target Position", targetPosition);
        SmartDashboard.putNumber("Auto - Absoulte Power", absolutePower);
        SmartDashboard.putNumber("Auto - Result Power", resultPower);
        driveTrain.arcadeDrive(resultPower, 0.0);

        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }

    private double Polynomial345(double time)
    {   
        return (10 * Math.pow(time, 3.0)) - (15 * Math.pow(time, 4.0)) + (6 * Math.pow(time, 5.0));
    }
}
