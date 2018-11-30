package org.usfirst.frc.team3245.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;



import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;



import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 

* functions corresponding to each mode, as described in the IterativeRobot
 

* documentation. If you change the name of this class or the package after
 

* creating this project, you must also update the manifest file in the resource
 

* directory.
 */

public class Robot extends IterativeRobot {

	
Accelerometer accel = new BuiltInAccelerometer();

double xVal,maxX,yVal,maxY,zVal,maxZ;

AnalogGyro testgyro = new AnalogGyro(0);
	

//two controllers, one for driver, one for operator


Joystick drivercontrol = new Joystick (0);

Joystick operatorcontrol = new Joystick (1);

//three motor controllers for driving and climbing

Talon leftdrive = new Talon (1);

Talon rightdrive = new Talon (3);

Talon IntakeLeftMotor1 = new Talon (7);

Talon IntakeRightMotor1 = new Talon(6);

Talon WristMotor1 = new Talon(0);

//speeds for the motors: fast and slow speeds for left and right drive sides, a changeable slow speed value,

//a climb motor speed, and a door motor speed

double fastleft = 1, fastright = 1, slowleft = .5, slowright = .5, topslowspeed =.6, WristMotor =.5, IntakeRightMotor =.5, IntakeLeftMotor,
			
//slowfactor is the number you want the top speed divided by
//if slowfactor is 2 top speed is 50%
//if slowfactor is 3 top speed will be 33%

slowfactor = 2;

//variable to identify which auto routine is selected

int dbValue = -1;

//servo motor to control gear loading door

Servo door = new Servo (9);

PowerDistributionPanel PDP = new PowerDistributionPanel (0);

boolean firstiter = true;

double heading = 0;

/**

* This function is run when the robot is first started up and should be
	 * used for any initialization code.
 */

@Override
	
public void robotInit() {

//Provides labels for the auto routine buttons
		
SmartDashboard.putString("DB/String 0", "Auto Routines;");
SmartDashboard.putString("DB/String 1", "0: hang left gear");
SmartDashboard.putString("DB/String 2", "1: hang center gear");
SmartDashboard.putString("DB/String 3", "2: hang right gear");
SmartDashboard.putString("DB/String 4", "3: just cross the line");

//Provides labels for the dashboard sliders

SmartDashboard.putString("DB/String 5", "Top slider controls");
SmartDashboard.putString("DB/String 6", "maximum slow speed");
SmartDashboard.putString("DB/String 8", "Bottom slide controls");
SmartDashboard.putString("DB/String 9", "climb speed");
}

/**
* This autonomous (along with the chooser code above) shows how to select
* between different autonomous modes using the dashboard. The sendable
* chooser code works with the Java SmartDashboard. If you prefer the
* LabVIEW Dashboard, remove all of the chooser code and uncomment the
* getString line to get the auto name from the text box below the Gyro.
*
* You can add additional auto modes by adding additional comparisons to the
* switch structure below with additional strings. If using the
* SendableChooser make sure to add them to the chooser code above as well.
 */
@Override
public void autonomousInit() {
maxX = 0; maxY = 0; maxZ = 0;      //clears previous high values

testgyro.reset();
dbValue = -1;       // clears previous auto routine selection

//Check to see which dashboard button is selected.

//If multiple buttons are selected, it will prioritize the lowest number

//If no buttons are selected, it defaults in the later switch statement
if (SmartDashboard.getBoolean("DB/Button 0", false)) {
dbValue = 0;
} else if (SmartDashboard.getBoolean("DB/Button 1", false)) {
dbValue = 1;
} else if (SmartDashboard.getBoolean("DB/Button 2", false)) {
dbValue = 2;
} else if (SmartDashboard.getBoolean("DB/Button 3", false)) {
dbValue = 3;
}
}
/**
* This function is called periodically during autonomous
*/

@Override

public void autonomousPeriodic() {
xVal = accel.getX();
if (maxX < xVal) {maxX = xVal;};
if (maxX < -xVal){maxX = -xVal;};      //captures new high values
yVal = accel.getY();
if (maxY < yVal) {maxY = yVal;};
if (maxY < -yVal){maxY = -yVal;};        //captures new high values 
zVal = accel.getZ();
if (maxZ < zVal) {maxZ = zVal;};
if (maxZ < -xVal){maxZ = -zVal;};
//captures new high values

SmartDashboard.putNumber("xValue is", accel.getX());
SmartDashboard.putNumber("max X is", maxX);
SmartDashboard.putNumber("yValue is", accel.getY());
SmartDashboard.putNumber("max Y is", maxY);
SmartDashboard.putNumber("zValue is", accel.getZ());
SmartDashboard.putNumber("max Z is", maxZ);
heading = testgyro.getAngle();
SmartDashboard.putNumber("Heading is", heading );
//chooses auto routine based on the value of dbValue, set in autonomousInit

switch (dbValue) {
// robot hangs gear on left lift
case 0:							
//INCOMPLETE
leftdrive.set(fastleft/2);

rightdrive.set(fastright/2);
Timer.delay(2);
leftdrive.set(fastleft/2);
rightdrive.set(fastright/6);
Timer.delay(1);
leftdrive.set(fastleft/2);
rightdrive.set(fastright/2);
Timer.delay(1);
dbValue = -1;
break;
// robot hangs gear on center lift
case 1:							
driveStraight(1.0);
dbValue = -1;
break;
// robot hangs gear on right lift

case 2:
//INCOMPLETE
driveStraight(1.0);
leftdrive.set(-fastleft/2);
rightdrive.set(-fastright/2);
Timer.delay(.5);
driveStraight(1.0);
dbValue = -1;
break;


// robot drives straight across line for 5 points
case 3:								
driveStraight(3.5);
dbValue = -1;
break;
// default case: drive motors do nothing

default:								
rightdrive.set(0);
leftdrive.set(0);
break;
}
}

//a helper function to simplify autonomous driving straight
private void driveStraight(double time) {
leftdrive.set(fastleft/2);
rightdrive.set(-fastright/2);
Timer.delay(time);
}

/**
	 
* This function is called periodically during operator control
	 

*/
	


@Override
public void teleopInit(){	


maxX = 0; maxY = 0; maxZ = 0;


}
public void teleopPeriodic() {
//if (firstiter == true) {
//	
maxX = 0; maxY = 0; maxZ = 0;
//
firstiter = false;

//}
	
		


fastleft = drivercontrol.getRawAxis(1);					


// read the left joystick on the driver control to get full speed

fastright = -drivercontrol.getRawAxis(3);					

// read the right joystick on the driver control to get full speed

if (drivercontrol.getRawButton(6)) {						
	
// if the driver is holding down the slow button send slow speed

leftdrive.set(fastleft * .3);								

// to the motor controllers otherwise send full speed to motor

rightdrive.set(fastright * .3);								

// controllers

} else {
		
leftdrive.set(fastleft * .7);

rightdrive.set(fastright * .7);
}

xVal = accel.getX();

if (maxX < xVal) {maxX = xVal;};

if (maxX < -xVal){maxX = -xVal;};      //captures new high values

yVal = accel.getY();

if (maxY < yVal) {maxY = yVal;};

if (maxY < -yVal){maxY = -yVal;};    //captures new high values

zVal = accel.getZ();

if (maxZ < zVal) {maxZ = zVal;};

if (maxZ < -xVal){maxZ = -zVal;};//captures new high valuesSmartDashboard.putNumber("xValue is", accel.getX());
		


SmartDashboard.putNumber("max X is", maxX);
		


SmartDashboard.putNumber("yValue is", accel.getY());
		


SmartDashboard.putNumber("max Y is", maxY);
		


SmartDashboard.putNumber("zValue is", accel.getZ());
		


SmartDashboard.putNumber("max Z is", maxZ);
		
		


//SmartDashboard.putNumber("fastleft", fastleft);
		


//SmartDashboard.putNumber("fastright", fastright);
		



//SmartDashboard.putNumber("topslowspeed", topslowspeed); 
		



//SmartDashboard.putNumber("Left Motor Current", PDP.getCurrent(2));
		


//SmartDashboard.putNumber("Left Motor Current", PDP.getCurrent(3));
		



//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(12));
		



//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(13));
		



//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(14));
		
	


}
	


/** Function practice calls autonomous periodically for 15 seconds
	 
*  before switching to operator control for 2 minutes. That should
	

*  be changed to 2 & 1/2 minutes to fit 2017 gameplay description times.
	 


*/

	


/**
	 


* This function is called periodically during test mode
	 */
	


@Override
	


public void testInit() {
		
		



maxX = 0; maxY = 0; maxZ = 0; //clears previous high values
		
	


}
	


public void testPeriodic() {
		


/**This function is apparently for testing all of the controls and motors
		 


* to see if they are ready. Good for rooting out flawed commands,
		


 * disfunctional components, etc.
		 


*/
		
		



fastleft = -drivercontrol.getRawAxis(1);					



// read the left joystick on the driver control to get full speed						
		



fastright = drivercontrol.getRawAxis(3);					



// read the right joystick on the driver control to get full speed
				
		



if (drivercontrol.getRawButton(6)) {						


// see if the driver is holding down the slow button
			


topslowspeed = SmartDashboard.getNumber("DB/Slider 0",3.0); 	


// Reads the top slider on the dashboard


															
			


if(topslowspeed <1) {topslowspeed = 1;}					


// if the top slow speed returned was less than 1, make it 1
			


topslowspeed = topslowspeed/5;							


// Normalizes the value to a percent
			

slowleft = fastleft * topslowspeed;	 					

// multiply full speed by percent of top slider to get slow speed
			


slowright = fastright * topslowspeed;					


// multiply full speed by percent of top slider to get slow speed
			


leftdrive.set(slowleft);								


// send slow speed to left & right drive
			

rightdrive.set(slowright);								
		

} else {													

//if slow button not held down send fast speed to left & right drive
			

leftdrive.set(fastleft/slowfactor);
			


rightdrive.set(fastright/slowfactor);
		


}
		
		


//SmartDashboard.putNumber("fastleft", fastleft);
		


//SmartDashboard.putNumber("fastright", fastright);
		


//SmartDashboard.putNumber("topslowspeed", topslowspeed);
		


SmartDashboard.putNumber("PDP temp", PDP.getTemperature());
		


//SmartDashboard.putNumber("Left Motor Current", PDP.getCurrent(0));
		


//SmartDashboard.putNumber("Left Motor Current", PDP.getCurrent(2));
		



//SmartDashboard.putNumber("Left Motor Current", PDP.getCurrent(3));
		


//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(12));
		


//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(13));
		


//SmartDashboard.putNumber("Right Motor Current", PDP.getCurrent(15));
		
		


xVal = accel.getX();
		


if (maxX < xVal) {maxX = xVal;};
		


if (maxX < -xVal){maxX = -xVal;};  //captures new high values
		
		


yVal = accel.getY();
		


if (maxY < yVal) {maxY = yVal;};
		

if (maxY < -yVal){maxY = -yVal;};//captures new high values
		
		

zVal = accel.getZ();
		

if (maxZ < zVal) {maxZ = zVal;};
		

if (maxZ < -xVal){maxZ = -zVal;};  //captures new high values
		
		


SmartDashboard.putNumber("xValue is", accel.getX());
		


SmartDashboard.putNumber("max X is", maxX);
		


SmartDashboard.putNumber("yValue is", accel.getY());
		


SmartDashboard.putNumber("max Y is", maxY);
		


SmartDashboard.putNumber("zValue is", accel.getZ());
		

SmartDashboard.putNumber("max Z is", maxZ);
		
	


}



}

