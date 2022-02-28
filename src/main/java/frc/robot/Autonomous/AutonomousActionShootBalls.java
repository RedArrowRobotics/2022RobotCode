package frc.robot.Autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;
import frc.robot.ComponentsControl.PIDCalculator;
import frc.robot.ComponentsControl.ShotParameters;

public class AutonomousActionShootBalls extends AutonomousAction {

    boolean shotInProgress = false;
    boolean firstShooterSpinupCompleted = false;
    Integer shooterVelWithinToleranceCycleCount = 0;
    Double lowerBeltPowerAccum = 0.0;
    Double upperBeltPowerAccum = 0.0;
    
    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {

    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        driveTrain.arcadeDrive(0.0, 0.0);
        double targetVelocity = 0;
        double firstPIDLoopVelocityTargetOffset = 0; 
        double intakeBeltMotorPower = 0.0;
        double transferBeltMotorPower = 0.0;

        if (!shotInProgress)
        {
            PIDCalculator shotCalculator = new PIDCalculator();
            ShotParameters shotParameters = shotCalculator.calculateShot(
                sensors.distanceToTarget,
                2); //2 is the variable distance shot
            
            components.shooterMotorPIDController.setP(shotParameters.P);
            components.shooterMotorPIDController.setI(shotParameters.I);
            components.shooterMotorPIDController.setD(shotParameters.D);
            components.shooterMotorPIDController.setFF(shotParameters.FF);
            targetVelocity = shotParameters.targetVelocity;

            components.shooterMotorPIDController.setReference(
                targetVelocity-firstPIDLoopVelocityTargetOffset, ControlType.kVelocity);

            shotInProgress = true;
            firstShooterSpinupCompleted = false;
        }
        else
        {
            final double motorVelocity = components.shooterMotorEncoder.getVelocity();
            SmartDashboard.putNumber("Shooter Motor Vel", motorVelocity);
            if (motorVelocity >= ( targetVelocity - firstPIDLoopVelocityTargetOffset) )
            {
                firstShooterSpinupCompleted = true;
            }
            if (firstShooterSpinupCompleted)
            {
                double targetVelocityTolerance = 20;
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
                }
                else
                {
                    SmartDashboard.putBoolean("DB/LED 0", true);
                    shooterVelWithinToleranceCycleCount = 0;
                }
            }
        }
        if (!sensors.upperBallPresent)
        {
            intakeBeltMotorPower = 1.0;
            transferBeltMotorPower = 0.7;
        }

        components.intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        components.transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
        return false;
        
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
            
    }
}
