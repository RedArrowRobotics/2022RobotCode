package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlPIDTest extends ComponentsControl {

    @Override
    public void runComponents(ControlInputs controlInputs, SensorInputs sensorInputs)
    {
        if (controlInputs.testShooter)
        {
            if (shotInProgress == false)
            {
                //shooterMotor.set(.60);
                shooterMotorPIDController.setReference(6350, ControlType.kVelocity, 0);
                shotInProgress = true;
            }
            else
            {
                if (reallyShoot == false)
                {
                    if (shooterMotorEncoder.getVelocity() > 6000)
                    {
                        SmartDashboard.putBoolean("DB/LED 0", true);
                        reallyShoot = true;
                        shooterMotorPIDController.setReference(6350, ControlType.kVelocity, 2);
                    }
                }
                else
                {
                    
                }
            }
            
        }
        else
        {
            shotInProgress = false;
            reallyShoot = false;
            shooterMotor.set(0.0);
            SmartDashboard.putBoolean("DB/LED 0", false);
                        
        }
    }
}