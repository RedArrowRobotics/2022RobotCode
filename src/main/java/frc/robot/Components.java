package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;

public class Components {

    public final CANSparkMax intakeRollerMotor = new CANSparkMax(6, MotorType.kBrushless);
    public final TalonSRX intakeBeltMotor = new TalonSRX(7);
    public final TalonSRX transferBeltMotor = new TalonSRX(8);

    public final CANSparkMax shooterMotor = new CANSparkMax(5, MotorType.kBrushless);
    public final RelativeEncoder shooterMotorEncoder = shooterMotor.getEncoder();
    public final SparkMaxPIDController shooterMotorPIDController = shooterMotor.getPIDController();

    public final Solenoid intakeArmControl = new Solenoid(PneumaticsModuleType.CTREPCM, 0); 
    public final Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
    public final Solenoid climbFlipControl = new Solenoid(PneumaticsModuleType.CTREPCM, 1);

    public final Solenoid claws1 = new Solenoid(PneumaticsModuleType.CTREPCM,2);
    public final Solenoid claws2 = new Solenoid(PneumaticsModuleType.CTREPCM,3);
    public final CANSparkMax climbMotor = new CANSparkMax(9, MotorType.kBrushless);
}