package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;
import frc.robot.Components;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ComponentsControlV5 extends ComponentsControl {

    @Override
    public void runComponents(Components components, ControlInputs controlInputs, SensorInputs sensorInputs) 
    {    
        Double intakeRollerMotorPower = 0.0;
        Double intakeBeltMotorPower = 0.0;
        Double transferBeltMotorPower = 0.0;
        SpeedCalculator speedCalculator = new SpeedCalculator();

        if (controlInputs.runIntake)
        {
            if (!sensorInputs.upperBallPresent)
            {
                intakeBeltMotorPower = 1.0;
                transferBeltMotorPower = 0.25;

            }
            else
            {
                if (!sensorInputs.lowerBallPresent)
                {
                    intakeBeltMotorPower = 0.4;
                }
            }
            if (!sensorInputs.upperBallPresent || !sensorInputs.lowerBallPresent)
            {
                intakeRollerMotorPower = -1.0;
            }
        }

        if ( controlInputs.shootLow || controlInputs.shootHigh)
        {
            final double lowShotTargetVelocity = 2000;
            final double highShotTargetVelocity = 5800;
            double targetVelocity = 0;
            double targetVelocity2 = 0;
            if (controlInputs.shootLow)
            {
                targetVelocity = lowShotTargetVelocity+speedCalculator.differenceInSpeed(lowShotTargetVelocity);
                targetVelocity2 = lowShotTargetVelocity;
            }
            if (controlInputs.shootHigh)
            {
                targetVelocity = highShotTargetVelocity+speedCalculator.differenceInSpeed(highShotTargetVelocity);
                targetVelocity2 = highShotTargetVelocity;
            }
            
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

                double targetVelocityTolerance = 20;
                int cycleCountThreshold = 10;
                if ( (motorVelocity >= targetVelocity2 - targetVelocityTolerance) && 
                (motorVelocity <= targetVelocity2 + targetVelocityTolerance) )
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
            if (!sensorInputs.upperBallPresent)
            {
                intakeBeltMotorPower = 1.0;
                transferBeltMotorPower = 0.7;
            }
        }
        else if ( controlInputs.shootAdaptiveHigh )
        {
            final double TargetVelocity = speedCalculator.variableTarget(sensorInputs.distanceToTarget);
            double targetVelocity = TargetVelocity+speedCalculator.differenceInSpeed(TargetVelocity);

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
                SmartDashboard.putNumber("Difference In Speed", speedCalculator.differenceInSpeed(TargetVelocity));
                
                double targetVelocityTolerance = 20;
                int cycleCountThreshold = 10;
                if ( (motorVelocity >= TargetVelocity - targetVelocityTolerance) && 
                (motorVelocity <= TargetVelocity + targetVelocityTolerance) )
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
            if (!sensorInputs.upperBallPresent)
            {
                intakeBeltMotorPower = 1.0;
                transferBeltMotorPower = 0.7;
            }
        }
        else
        {
            SmartDashboard.putBoolean("DB/LED 0", false);
            if (shotInProgress)
            {
                shotInProgress = false;
                firstShooterSpinupCompleted = false;
                shooterVelWithinToleranceCycleCount = 0;
                upperBeltPowerAccum = 0.0;
            }
            components.shooterMotor.set(0.0);
        }
        components.intakeArmControl.set(controlInputs.deployIntake);

        components.intakeRollerMotor.set(intakeRollerMotorPower);
        components.intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        components.transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
    }
}