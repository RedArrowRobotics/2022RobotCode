package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;
import frc.robot.Components;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ComponentsControlV6 extends ComponentsControl {

    private Integer lowShotDelayCycleCount;
    @Override
    public void runComponents(Components components, ControlInputs controlInputs, SensorInputs sensorInputs) 
    {    
        Double intakeRollerMotorPower = 0.0;
        Double intakeBeltMotorPower = 0.0;
        Double transferBeltMotorPower = 0.0;
        Double climbBarRotationMotorPower = 0.0;
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

        
        if ( controlInputs.shootLow)
        {
            if (!shotInProgress)
            {
                components.shooterMotor.set(.30);
                shotInProgress = true;
                lowShotDelayCycleCount = 0;
            }
            else
            {
                if (lowShotDelayCycleCount < 30)
                {
                    lowShotDelayCycleCount++;
                }
                else
                {
                    intakeBeltMotorPower = 1.0;
                    transferBeltMotorPower = 0.7;    
                }
            }
        }
        else if ( controlInputs.shootHigh)
        {
            //final double lowShotTargetVelocity = 2300;
            final double highShotTargetVelocity = 5800;
            double targetVelocity2 = 0;
            targetVelocity = highShotTargetVelocity+speedCalculator.differenceInSpeed(highShotTargetVelocity);
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

                double targetVelocityTolerance = 70;
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
            components.compressor.disable();
        }
        else if ( controlInputs.shootAdaptiveHigh )
        {
            if (selectedShot == 0)
            {
                if (sensorInputs.alternateDistanceToTarget>2)
                {
                    if (sensorInputs.alternateDistanceToTarget<=4)
                    {
                        selectedShot = 1;
                        SmartDashboard.putString("Selected Shot Type", "Low Close");
                    }
                    else if (sensorInputs.alternateDistanceToTarget<=5.8)
                    {
                        selectedShot = 2;
                        SmartDashboard.putString("Selected Shot Type", "Low Far");
                    }
                    else if (sensorInputs.alternateDistanceToTarget>5.8)
                    {
                        selectedShot = 3;
                        SmartDashboard.putString("Selected Shot Type", "Adaptive High");
                    }
                }
                else
                {
                    SmartDashboard.putString("Selected Shot Type", "To Close");
                }
            }
            if (selectedShot==1 || selectedShot==2)
            {
                if (!shotInProgress)
                {
                    if (selectedShot==1)
                    {
                        components.shooterMotor.set(.30);
                    }
                    else if (selectedShot==2)
                    {
                        components.shooterMotor.set(.36);
                    }
                    shotInProgress = true;
                    lowShotDelayCycleCount = 0;
                }
                else
                {
                    if (lowShotDelayCycleCount < 30)
                    {
                        lowShotDelayCycleCount++;
                    }
                    else
                    {
                        intakeBeltMotorPower = 1.0;
                        transferBeltMotorPower = 0.7;    
                    }
                }
            }
            else if (selectedShot==3)
            {
                if (!shotInProgress)
                {
                    TargetVelocity = speedCalculator.variableTarget(sensorInputs.alternateDistanceToTarget);
                    targetVelocity = TargetVelocity+speedCalculator.differenceInSpeed(TargetVelocity);
                    SmartDashboard.putNumber("Distance at calculation", sensorInputs.alternateDistanceToTarget);

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
                    SmartDashboard.putNumber("Calculated Shooter RPM", TargetVelocity);

                    double targetVelocityTolerance = 70;
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
            }
            if (!sensorInputs.upperBallPresent)
            {
                intakeBeltMotorPower = 1.0;
                transferBeltMotorPower = 0.7;
            }
            components.compressor.disable();
        }
        else
        {
            SmartDashboard.putBoolean("DB/LED 0", false);
            SmartDashboard.putString("Selected Shot Type", "None");
            if (shotInProgress)
            {
                shotInProgress = false;
                firstShooterSpinupCompleted = false;
                shooterVelWithinToleranceCycleCount = 0;
                upperBeltPowerAccum = 0.0;
                selectedShot = 0;
            }
            components.shooterMotor.set(0.0);
            components.compressor.enableDigital();
        }
        if (controlInputs.overRideShooter)
        {
            components.shooterMotor.set(0.30);
            components.compressor.disable();
        }
        if (controlInputs.overRideBelts)
        {
            intakeBeltMotorPower = 1.0;
            transferBeltMotorPower = 0.7;
        }
        if (controlInputs.dumpBalls)
        {
            intakeBeltMotorPower = -1.0;
            transferBeltMotorPower = -1.0;            
        }
        //Climb
        if (components.climbEnabled)
        {
            //  1
            if (controlInputs.claws1Button_Open)
            {
                components.claws1Grab = false;
            }
            else if (controlInputs.claws1Button_Close || sensorInputs.firstClawObjectPresent)
            {
                components.claws1Grab = true;
            }
            //  2
            if (controlInputs.claws2Button_Open)
            {
                components.claws2Grab = false;
            }
            else if (controlInputs.claws2Button_Close || sensorInputs.secondClawObjectPresent)
            {
                components.claws2Grab = true;
            }
            //  Motor
            /*if (controlInputs.climbRotation > 0.5)
            {
                climbBarRotationMotorPower = -1.0;
            }
            else if (controlInputs.climbRotation < -0.5)
            {
                climbBarRotationMotorPower = -1.0;
            }*/
            if (controlInputs.climbRotation2)
            {
                climbBarRotationMotorPower = -1.0;
            }
        }
        else if (!components.climbEnabled && controlInputs.climbEnableButton)
        {
            components.climbEnabled = true;
        }

        components.climbBarRotationMotor.set(climbBarRotationMotorPower);
        components.intakeArmControl.set(controlInputs.deployIntake);
        components.intakeRollerMotor.set(intakeRollerMotorPower);
        components.intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        components.transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
        components.claws1.set(!components.claws1Grab);
        components.claws2.set(!components.claws2Grab);
    }
}