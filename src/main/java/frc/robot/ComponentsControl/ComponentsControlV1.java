package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Components;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlV1 extends ComponentsControl {

    @Override
    public void runComponents(Components components, ControlInputs controlInputs, SensorInputs sensorInputs) 
    {    
        Double intakeRollerMotorPower = controlInputs.runIntake ? -1.0 : 0.0;
        Double intakeBeltMotorPower = controlInputs.runIntake ? 1.0 : 0.0;
        Double transferBeltMotorPower = 0.0;

        if ( controlInputs.shootLow || controlInputs.shootHigh)
        {
            final double lowShotTargetVelocity = 3000;
            final double highShotTargetVelocity = 6350;
            double targetVelocity = 0;
            double firstPIDLoopVelocityTargetOffset = 100;
            if (controlInputs.shootLow)
            {
                targetVelocity = lowShotTargetVelocity;
            }
            if (controlInputs.shootHigh)
            {
                targetVelocity = highShotTargetVelocity;
            }
            
            if (!shotInProgress)
            {
                components.shooterMotorPIDController.setReference(
                    targetVelocity-firstPIDLoopVelocityTargetOffset, ControlType.kVelocity, 0);
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
                    if (!secondShooterSpinupInProcess)
                    {
                        components.shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity, 2);
                        secondShooterSpinupInProcess = true;
                    }
                }
                if (secondShooterSpinupInProcess)
                {
                    double targetVelocityTolerance = 20;
                    int cycleCountThreshold = 10;
                    if ( (motorVelocity >= targetVelocity - targetVelocityTolerance) && 
                         (motorVelocity <= targetVelocity + targetVelocityTolerance) )
                    {
                        if (shooterVelWithinToleranceCycleCount >= cycleCountThreshold)
                        {
                            transferBeltMotorPower = 1.0;
                        }
                        else
                        {
                            shooterVelWithinToleranceCycleCount++;
                        }
                    }
                    else
                    {
                        SmartDashboard.putBoolean("DB/LED 0", true);
                        shooterVelWithinToleranceCycleCount = 0;
                    }
                }
                /*if (motorVelocity >= targetVelocity)
                {
                    reallyShoot = true;
                }*/
                /*if ( (motorVelocity > targetVelocity - velocityTolerance)  &&
                    (motorVelocity < targetVelocity + velocityTolerance) )
                {
                    transferBeltMotorPower = 1.0;
                }*/
            }
        }
        else
        {
            SmartDashboard.putBoolean("DB/LED 0", false);
            if (shotInProgress)
            {
                shotInProgress = false;
                firstShooterSpinupCompleted = false;
                secondShooterSpinupInProcess = false;
                shooterVelWithinToleranceCycleCount = 0;
            }
            components.shooterMotor.set(0.0);
        }

        /*if (controlInputs.runTransferBelt)
        {
            transferBeltMotorPower = 1.0;
        }
        else
        {
            transferBeltMotorPower = reallyShoot ? 1.0 : 0.0;
        }*/
        components.intakeArmControl.set(controlInputs.deployIntake);

        components.intakeRollerMotor.set(intakeRollerMotorPower);
        components.intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        components.transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);

        if (controlInputs.testShooter)
        {
            components.shooterMotor.set(.60);
        }
    }
}
