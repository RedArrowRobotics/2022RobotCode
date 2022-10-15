package frc.robot.Autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;
import frc.robot.ComponentsControl.SpeedCalculator;

public class AutononmousActionShootAdaptiveFromCapturePoint extends AutonomousAction {
 
    private double TargetVelocity = 0.0;
    private SpeedCalculator speedCalculator = new SpeedCalculator();
    private boolean shotInProgress = false;
    private double maxShooterVel = 0.0;
    Integer shooterVelWithinToleranceCycleCount = 0;
    Double lowerBeltPowerAccum = 0.0;
    Double upperBeltPowerAccum = 0.0;
    double intakeBeltMotorPower = 0.0;
    double transferBeltMotorPower = 0.0;
    double targetVelocity = 0;
    
    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.compressor.disable();
        driveTrain.arcadeDrive(0.0, 0.0);
        if (!shotInProgress)
        {
            TargetVelocity = speedCalculator.variableTarget(sensors.alternateDistanceToTarget);
            targetVelocity = TargetVelocity+speedCalculator.differenceInSpeed(TargetVelocity)+150;
            SmartDashboard.putNumber("Distance at calculation", sensors.alternateDistanceToTarget);
            
            components.shooterMotorPIDController.setP(0.0003); //0.00008 | 0.0004 | 0.0003
            components.shooterMotorPIDController.setI(0.0000000005); //0.0000000000001 | 0.0000000001 | 0.0000000005
            components.shooterMotorPIDController.setD(0);
            components.shooterMotorPIDController.setFF(0.0001); //0.0001

            components.shooterMotorPIDController.setReference(
                targetVelocity, ControlType.kVelocity);
            shotInProgress = true;
            maxShooterVel = 0.0;
        }
        else
        {
            final double motorVelocity = components.shooterMotorEncoder.getVelocity();
            SmartDashboard.putNumber("Shooter Motor Vel", motorVelocity);
            if (motorVelocity > maxShooterVel){maxShooterVel = motorVelocity;}
            SmartDashboard.putNumber("Shooter Motor Max Vel", maxShooterVel);
            SmartDashboard.putNumber("Shooter Motor PID Target", targetVelocity);
            SmartDashboard.putNumber("Difference In Speed", speedCalculator.differenceInSpeed(TargetVelocity));
            SmartDashboard.putNumber("Calculated Shooter RPM", TargetVelocity);

            double targetVelocityTolerance = 83;
            int cycleCountThreshold = 10;
            if ( (motorVelocity >= targetVelocity - targetVelocityTolerance) && 
            (motorVelocity <= targetVelocity + targetVelocityTolerance) )
            {
                if (shooterVelWithinToleranceCycleCount >= cycleCountThreshold)
                {
                    upperBeltPowerAccum = upperBeltPowerAccum+0.05;
                    transferBeltMotorPower = Math.min(upperBeltPowerAccum,1.0);
                }
                else
                {
                    shooterVelWithinToleranceCycleCount++;
                    upperBeltPowerAccum = 0.0;
                }
                SmartDashboard.putNumber("Accum power", upperBeltPowerAccum);
                SmartDashboard.putNumber("Transfer power", transferBeltMotorPower);
            }
            else
            {
                SmartDashboard.putBoolean("DB/LED 0", true);
                shooterVelWithinToleranceCycleCount = 0;
            }
            SmartDashboard.putString("Shooter Cycle Count", "Auto Adaptive: "+shooterVelWithinToleranceCycleCount);
        }
        if (!sensors.upperBallPresent)
        {
            intakeBeltMotorPower = 1.0;
            transferBeltMotorPower = 0.7;
        }
        components.compressor.disable();

        components.intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        components.transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }
    
}
