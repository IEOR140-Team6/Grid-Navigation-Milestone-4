package com.mydomain;

import java.awt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import lejos.nxt.*;

/** Date: September 13, 2012
 * Class Name: Milestone3
 * Authors: Moonsoo Choi and Sherman Siu
 * Program description: Program navigates a 6x8 coordinate grid, with the user inputting the coordinate
 * values into the robot using ButtonCounter.java. Tracker is used to steer the robot.
 * Note: Tracker has not changed since Milestone 2.
 */

public class Milestone4 
{
	//Set up DifferentialPilot on the robot with light sensor. Uses tracker to track.
	DifferentialPilot myPilot = new DifferentialPilot((float)(56/25.4),5.5f,Motor.A,Motor.C,false);
	LightSensor myLeftLightSensor = new LightSensor(SensorPort.S1);
	LightSensor myRightLightSensor = new LightSensor(SensorPort.S4);
	UltrasonicSensor UltraSonic = new UltrasonicSensor(SensorPort.S3);
	Tracker tracker = new Tracker(myPilot, myLeftLightSensor, myRightLightSensor);
	Grid grid = new Grid(6,8);
	
	//Robot keeps track of three points: old location, current location, and new location.
	Point currentPoint = new Point();
	Point newPoint = new Point();
	Point oldPoint = new Point();
	int TotalAngle;
	
	public int TotalAngle()
	{
		return TotalAngle;
	}
	/**
	 * Calibrate the robot.
	 */
	public void calibrate()
  	{
  		tracker.calibrate();
  	}
	
	public void Turn(int d)
	{
		myPilot.rotate(90*d);
		TotalAngle = TotalAngle + 90*d;
	}
	
	public int Direction()
	{
		int Direction = TotalAngle/90;
		if (Direction<0)
		{
			Direction = Direction + 4;
		}
		else if (Direction > 3)
		{
			Direction = Direction%4;
		}
		return Direction;
	}
	
	public void Navigate(int x, int y)
	{
		int newDirection = 0;
		if (x<0)
		{
			newDirection = 2;
			Turn(newDirection-Direction());
		}
		
	}
	
	public void ShortestPath()
	{
		grid.setDestination(newPoint.x,newPoint.y);
		grid.recalc();
		int v = grid.nodes[newPoint.x][currentPoint.y].getDistance(); //move X
		int w = grid.nodes[currentPoint.x][newPoint.y].getDistance(); //move Y
		if (v<w)
		{
			Navigate(newPoint.x-currentPoint.x,0);
		}
	}
	
	
	/**
	The robot has coordinates being inputed into it via ButtonCounter.java. The method 
	storePoints() stores these inputed points so these points can be run through moveXdistance
	and moveYdistance. The robot will also store the previous point such that if you want to run
	multiple points, you do not have to reset from the beginning to run another point.
	In short: it stores all the point values (it's where you draw oldPoint, currentPoint, newPoint). 
	 */
	public void StorePoints()
	{
		//Set & define all the integer values of the points inputed.
		int oldX = oldPoint.x;
		int oldY = oldPoint.y;
		int currentX = currentPoint.x;
		int currentY = currentPoint.y;
		int newX = newPoint.x;
		int newY = newPoint.y;
		/*
		When the robot is finished running, set the location of currentPoint to oldPoint,
		and move newPoint's location to currentPoint. The user will then input a new newPoint. 
		 */
		oldPoint.setLocation(currentX, currentY);
		currentPoint.setLocation(newX, newY);
		//Clear screen
		LCD.clear();
		//Display oldPoint
		LCD.drawString("Old X", 9, 0);
		LCD.drawString("Old Y", 9, 1);
		LCD.drawInt(oldPoint.x, 11, 0);
		LCD.drawInt(oldPoint.y, 11, 1);
		//Display currentPoint
		LCD.drawString("Current X", 9, 3);
		LCD.drawString("Current Y", 9, 4);
		LCD.drawInt(currentPoint.x, 11, 3);
		LCD.drawInt(currentPoint.y, 11, 4);
		//Display newPoint
		LCD.drawString("New X", 9, 6);
		LCD.drawString("New Y", 9, 7);
		LCD.drawInt(newPoint.x, 11, 6);
		LCD.drawInt(newPoint.y, 11, 7);
		//Wait 5 seconds.
		Delay.msDelay(5000);
	}	

	/**
	 * The main method puts everything together. It takes the methods from Milestone3 as well as
	 * from ButtonCounter.java to navigate the robot.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//Set instance variable myMS4 and ButtonCounter myButtCount to operate robot
		Milestone4 myMS4 = new Milestone4();
		ButtonCounter myButtCount = new ButtonCounter();		
		//Calibrate the robot
		myMS4.calibrate();
		//Display "loading" string and wait 1 second.
		LCD.drawString("Loading...", 0, 0);
		Delay.msDelay(1000);
		//Initialize currentPoint and oldPoint at (0,0) to start things off.
		myMS4.oldPoint.setLocation(0,0);
		myMS4.currentPoint.setLocation(0,0);
		
		//Run this loop until someone manually ends the program on the robot.
		while(true)
		{
			//Display "Let's Begin!"
			myButtCount.count("Let's Begin!");
			//Draw from ButtonCounter the coordinates of the desired/new location.
			myMS4.newPoint.x = myButtCount.getX();
			myMS4.newPoint.y = myButtCount.getY();
			//Display on screen strings "X=" and Y=" as labels.
			LCD.drawString("X=",10,0);
			LCD.drawString("Y=",10,1);
			//Display x and y values of newPoint, the desired location.
			LCD.drawInt(myMS4.newPoint.x,12,3);
			LCD.drawInt(myMS4.newPoint.y,12,4);	
			//Wait 1 second.
			Delay.msDelay(1000);
			
			myMS4.StorePoints(); //Run StorePoints()
		}
	}

}
