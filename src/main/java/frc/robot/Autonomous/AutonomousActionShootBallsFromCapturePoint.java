package frc.robot.Autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;
import frc.robot.ComponentsControl.SpeedCalculator;

public class AutonomousActionShootBallsFromCapturePoint extends AutonomousAction {

    boolean shotInProgress = false;
    boolean firstShooterSpinupCompleted = false;
    Integer shooterVelWithinToleranceCycleCount = 0;
    Double lowerBeltPowerAccum = 0.0;
    Double upperBeltPowerAccum = 0.0;
    protected Double maxShooterVel = 0.0;
    SpeedCalculator speedCalculator = new SpeedCalculator();
    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.compressor.disable();
        driveTrain.arcadeDrive(0.0, 0.0);
        double targetVelocity = 0;
        double intakeBeltMotorPower = 0.0;
        double transferBeltMotorPower = 0.0;
        
        final double highShotTargetVelocity = 5850;
            double targetVelocity2 = 0;
            targetVelocity = highShotTargetVelocity+speedCalculator.differenceInSpeed(highShotTargetVelocity)+100;
            targetVelocity2 = highShotTargetVelocity;
                    
            if (!shotInProgress)
            {
                components.shooterMotorPIDController.setP(0.0003); //0.00008 | 0.0004 | 0.0003
                components.shooterMotorPIDController.setI(0.0000000005); //0.0000000000001 | 0.0000000001 | 0.0000000005
                components.shooterMotorPIDController.setD(0);
                components.shooterMotorPIDController.setFF(0.0001); //0.0001

                components.shooterMotorPIDController.setReference(
                    targetVelocity, ControlType.kVelocity);
                shotInProgress = true;
                firstShooterSpinupCompleted = false;
                maxShooterVel = 0.0;
            }
            else
            {
                final double motorVelocity = components.shooterMotorEncoder.getVelocity();
                SmartDashboard.putNumber("Shooter Motor Vel", motorVelocity);
                if (motorVelocity > maxShooterVel){maxShooterVel = motorVelocity;}
                SmartDashboard.putNumber("Shooter Motor Max Vel", maxShooterVel);
                SmartDashboard.putNumber("Shooter Motor PID Target", targetVelocity);
                SmartDashboard.putNumber("Difference In Speed", speedCalculator.differenceInSpeed(targetVelocity2));
                SmartDashboard.putNumber("Calculated Shooter RPM", targetVelocity2);

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
                }
                else
                {
                    shooterVelWithinToleranceCycleCount = 0;
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
