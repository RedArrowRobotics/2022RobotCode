package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlV1 extends ComponentsControl {

    @Override
    public void runComponents(ControlInputs controlInputs, SensorInputs sensorInputs) {
        
        Double intakeRollerMotorPower = controlInputs.runIntake ? -1.0 : 0.0;
        Double intakeBeltMotorPower = controlInputs.runIntake ? 1.0 : 0.0;
        Double transferBeltMotorPower = 0.0;

        if ( controlInputs.shootLow || controlInputs.shootHigh)
        {
            final double lowShotTargetVelocity = 3000;
            final double highShotTargetVelocity = 6350;
            double targetVelocity = 0;
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
                shooterMotorPIDController.setReference(targetVelocity-100, ControlType.kVelocity, 0);
                shotInProgress = true;
                reallyShoot = false;
            }
            else
            {
                double motorVelocity = shooterMotorEncoder.getVelocity();
                SmartDashboard.putNumber("Shooter Motor Vel", motorVelocity);
                double velocityTolerance = 100;
                if (motorVelocity >= targetVelocity - velocityTolerance)
                {
                    reallyShoot = true;
                }
                if (reallyShoot)
                {
                    shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity, 2);
                    waitForSpinUp = true;
                }
                if (waitForSpinUp)
                {
                    if (motorVelocity >= targetVelocity - 20 && motorVelocity <= targetVelocity + 20)
                    {
                        if (shooterThresholdCount >= 10)
                        {
                            transferBeltMotorPower = 1.0;
                        }
                        else
                        {
                            shooterThresholdCount++;
                        }
                    }
                    else
                    {
                        shooterThresholdCount = 0;
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
            if (shotInProgress)
            {
                shotInProgress = false;
                reallyShoot = false;
                waitForSpinUp = false;
                shooterThresholdCount = 0;
            }
            shooterMotor.set(0.0);
            //shooterMotorPIDController.setReference(0, ControlType.kVelocity, 0);                
        }

        /*if (controlInputs.runTransferBelt)
        {
            transferBeltMotorPower = 1.0;
        }
        else
        {
            transferBeltMotorPower = reallyShoot ? 1.0 : 0.0;
        }*/
        intakeArmControl.set(controlInputs.deployIntake);

        intakeRollerMotor.set(intakeRollerMotorPower);
        intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);

        if (controlInputs.testShooter)
        {
            shooterMotor.set(.60);
        }
    }
}
