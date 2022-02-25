package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Components {

    public final CANSparkMax intakeRollerMotor = new CANSparkMax(6, MotorType.kBrushless);
    public final TalonSRX intakeBeltMotor = new TalonSRX(7);
    public final TalonSRX transferBeltMotor = new TalonSRX(8);

    public final CANSparkMax shooterMotor = new CANSparkMax(5, MotorType.kBrushless);
    public final RelativeEncoder shooterMotorEncoder = shooterMotor.getEncoder();
    public final SparkMaxPIDController shooterMotorPIDController = shooterMotor.getPIDController();

    public final Solenoid intakeArmControl = new Solenoid(PneumaticsModuleType.CTREPCM, 0); 
}
