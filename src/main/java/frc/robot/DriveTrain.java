package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
  private final CANSparkMax driveFrontLeft = new CANSparkMax(1, MotorType.kBrushless);
  private final CANSparkMax driveRearLeft = new CANSparkMax(2, MotorType.kBrushless);
  //private final MotorControllerGroup driveLeft = new MotorControllerGroup(driveFrontLeft, driveRearLeft);

  private final CANSparkMax driveFrontRight = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax driveRearRight = new CANSparkMax(4, MotorType.kBrushless);
  //private final MotorControllerGroup driveRight = new MotorControllerGroup(driveFrontRight, driveRearRight);

  //private final DifferentialDrive robotDrive = new DifferentialDrive(driveLeft, driveRight);
  private final DifferentialDrive robotDrive = new DifferentialDrive(driveFrontLeft, driveFrontRight);

  public DriveTrain()
  {
    driveRearRight.follow(driveFrontRight, false);
    driveRearLeft.follow(driveFrontLeft, false);
  }

  public void arcadeDrive(double xSpeed, double zRotation )
  {
    robotDrive.arcadeDrive(xSpeed, zRotation, true);
  }
}
