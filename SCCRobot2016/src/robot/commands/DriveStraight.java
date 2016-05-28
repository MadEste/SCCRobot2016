package robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.DriverStation;
import robot.Robot;
import robot.RobotMap;
import robot.subsystems.Drive;

/**
 * Command that drives the robot along a straight line given by a heading angle
 *
 */
public class DriveStraight extends Command {

	private Drive drive = Robot.drive;
	private double heading;
	private double yMotion = 1;

	public DriveStraight() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		if (RobotMap.prefs.getBoolean("Test heading", false)) {
			heading = RobotMap.prefs.getDouble("Heading", 0);
			// make the robot turn on the spot for testing (no forward or
			// backward motion)
			yMotion = 0;
		} else {
			// Don't use getAngle (same but continuous) since the pid implementation
			// of the ahrs returns the same value as getYaw (= angle between -180 and 180)
			heading = RobotMap.ahrs.getYaw();
		}
		drive.driveOnHeadingInit(heading, 1);
	}

	@Override
	protected void execute() {
		// the negative sign is because forward is -y on the joystick
		// yMotion is 0 for testing and 0 otherwise
		double power = -yMotion * DriverStation.joystick.getY();
		drive.driveOnHeading(power);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		drive.driveOnHeadingEnd();
	}

	@Override
	protected void interrupted() {
		SmartDashboard.putString("Interrupted", "true");
		end();
	}

}