package com.mydomain;

import java.awt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import lejos.nxt.*;

/** 
 * IEOR 140 Team 6
 * Date: October 11, 2012
 * Class Name: GridNavigator
 * Authors: Moonsoo Choi and Sherman Siu
 * Program description: The GridNavigator class calls upon the Tracker, ButtonCounter, and Grid classes to map and navigate the robot. 
 * From the ButtonCounter class, the robot will retrieve the desired destination. Then the robot will navigate via ShortesetPath
 * method, in which Grid will calculate the distances of each node and the robot will navigate towards the neighboring node with the
 * smallest distance value (and is not blocked). The robot will continue navigating neighbor node by neighbor node until it reaches
 * its desired destination. If along the way, the neighboring node the robot wants to navigate towards has an obstacle, the
 * ultrasonic sensor will detect the obstacle and the robot will then recalculate ShortestPath again, using the new information 
 * acquired that there is an obstacle at that node. The robot moves via tracker.trackLine(), which allows the robot to follow the 
 * blue tape and identifying the black marker intersections. 
 */
public class GridNavigator 
{
	//Set up DifferentialPilot on the robot with light sensor and ultrasonic sensor. Uses tracker to track.
	DifferentialPilot myPilot = new DifferentialPilot((float)(56/25.4),5.5f,Motor.A,Motor.B,false);
	LightSensor myLeftLightSensor = new LightSensor(SensorPort.S1);
	LightSensor myRightLightSensor = new LightSensor(SensorPort.S4);
	UltrasonicSensor UltraSonic = new UltrasonicSensor(SensorPort.S2);
	Tracker tracker = new Tracker(myPilot, myLeftLightSensor, myRightLightSensor);
	Grid grid = new Grid(6,8);
	int currentDirection = 0;
	boolean nav_success;
	
	//Robot keeps track of two points: current location and new location.
	Point currentPoint = new Point();
	Point newPoint = new Point();

	/**
	 * Calibrate the robot.
	 */
	public void calibrate()
  	{
  		tracker.calibrate();
  	}
	
	/**
	 * Given the acquired direction integer value, the robot will change its current coordinates' x or y values to the new value.
	 * This method updates the current node location of the robot once the robot navigates from one node to another.
	 * The robot's direction is determined via ShortestPath method and the Grid class; the direction integer is the number in which
	 * the robot needs to head because the distance value of that neighboring node is the smallest. The numbers for direction are
	 * neutral-based; in a x-y plane direction 0 is towards +x, direction 1 is towards +y, direction 2 is towards -x, and any other
	 * directions (direction 3) is towards -y.
	 * @param direction
	 */
	public void coordinate_adj(int direction)
	{
		currentDirection = direction;
		if(direction%4 == 0)
		{
			currentPoint.x = currentPoint.x + 1;	
		}
		else if(direction%4 == 1)
		{
			currentPoint.y = currentPoint.y + 1;
		}
		else if(direction%4 == 2)
		{
			currentPoint.x = currentPoint.x - 1;
		}
		else
		{
			currentPoint.y = currentPoint.y - 1;
		}
	}
	
	/**
	 * The Navigate class takes the optimal direction value among the current node's neighbors and navigates the robot to that 
	 * neighboring node.
	 * @param direction
	 * @param _currentNode
	 */
	public void Navigate(int direction, Node _currentNode)
	{
		/*
		 * If the optimal neighboring node's distance value is not null and that neighboring node's distance value less than the
		 * value of that of the current node's, call upon tracker.trackLine() to navigate to the neighboring node. Then update the
		 * robot's current location.
		 */
		if (grid.neighbor(_currentNode, direction) != null 
				&&	_currentNode.getDistance() > grid.neighbor(_currentNode, direction).getDistance())
		{
			tracker.trackLine(); //Steer to new node location.
			coordinate_adj(currentDirection); //Update current location coordinates.
		}
		/*
		 * Else,  the robot can only turn left or right (or backwards if there are obstacles on both sides). 
		 * The robot in this scenario only has to check left, right, and back.  
		 */
		else 
		{
			//Initialize integer variable isDirection_zero
			int isDirection_Zero = 0;	
			//If the currentDirection is 0, then set isDirection_zero to be 1. 
			if(currentDirection == 0)
				isDirection_Zero = 1;
			/*
			 * Based from isDirection_zero, if the value of the robot's neighboring node at direction 3 is not null and the 
			 * distance of such neighboring node is less than the distance value of the current node, then turn right and steer
			 * to that neighboring node. Update the currentDirection based on the if statements, and update the current location.
			 */
			if (grid.neighbor(_currentNode, isDirection_Zero*4 + currentDirection - 1) != null 
					&& _currentNode.getDistance() > grid.neighbor(_currentNode, isDirection_Zero*4 + currentDirection - 1).getDistance())
			{
				tracker.rotate(-1); //Turn right
				tracker.trackLine(); //Steer along blue tape
				//If currentDirection is greater than 0, subtract 1 to that value (because the robot turned right)
				if(currentDirection > 0)
					currentDirection = currentDirection - 1;
				// Else if currentDirection = 0, then make it 3 (because 0-1=-1, which is the same direction as 3).
				else if(currentDirection == 0)
				{	
					currentDirection = 3;
				}
				coordinate_adj(currentDirection); //Update current location
			}
			/*
			 * If not, the robot turns left, and updates the direction coordinates.
			 */
			else
			{
				tracker.rotate(1); //Turn left
				//If currentDirection is less than 3, then add 1 (because robot turned left)
				if(currentDirection < 3)
					currentDirection = currentDirection + 1;	
				//Else if the currentDirection is 3, make it 0 (because 3+1=4, which is equivalent to 0).
				else if(currentDirection == 3)
					currentDirection = 0;
			}
		}
	}
	
	/**
	 * The ShortestPath method calculates the optimal neighboring node and calls upon the Navigate method to navigate the robot
	 * to that neighboring node. If there is a blocked obstacle, then ShortestPath will recalculate. In other words, ShortestPath
	 * method finds the best neighboring node to go to until it arrives at desired destination.
	 * @param x
	 * @param y
	 */
	public void ShortestPath(int x, int y)
	{	
		//Set _currentNode to know where the current node location is.
		Node _currentNode = grid.nodes[currentPoint.x][currentPoint.y];
		//Set desired destination.
		grid.setDestination(x,y);
		//Based off the the destination set, calculate the grid of distance values.
		grid.recalc();
		
		/*
		 * While the robot has not arrived at its destination, the robot will navigate by finding the smallest distance value
		 * among all the neighboring nodes to its current location. The robot will move from node to node until it arrives at its
		 * desired destination.
		 */
		while(_currentNode.getDistance() > 0)
		{
			/*
			 * In this for loop, the robot will determine the optimal direction and navigate towards that direction determined
			 * (see Navigate method for details).
			 */
			for(int index = currentDirection; index <= 3; index ++)
			{
				Navigate(currentDirection, _currentNode); //Navigate to that currentDirection and _currentNode.
				_currentNode = grid.nodes[currentPoint.x][currentPoint.y];
				//Print the following on the screen
				LCD.drawInt(_currentNode.getX(), 0, 5);
				LCD.drawInt(_currentNode.getY(), 2, 5);
				LCD.drawInt(_currentNode.getDistance(), 4, 5);
				LCD.drawString(":Current Direction", 2, 6);
				LCD.drawInt(currentDirection, 0, 6);
				
				//If currentDirection is equal to index or an object is detected at that node, break.
				if(currentDirection == index || UltraSonic.getDistance() < 25)
				{
					break;
				}				
			}
			//If an obstacle is detected at that optimal neighboring node, then set that node is blocked and recalculate the grid.
			if (UltraSonic.getDistance() < 25 && grid.neighbor(_currentNode, currentDirection) != null)
			{
				_currentNode = grid.nodes[currentPoint.x][currentPoint.y];
				grid.neighbor(_currentNode, currentDirection).blocked();
				grid.recalc();
			}
		}
	}
	
	/**
	The robot has coordinates being inputed into it via ButtonCounter.java. The method 
	storePoints() stores these inputed points so these points can be run through Navigate. 
	The robot will also store the previous point such that if you want to run
	multiple points, you do not have to reset from the beginning to run another point.
	In short: it stores all the point values (it's where you draw currentPoint, newPoint). 
	 */
	public void StorePoints()
	{
		//Set & define all the integer values of the points inputed.
		
		int currentX = currentPoint.x;
		int currentY = currentPoint.y;
		int newX = newPoint.x;
		int newY = newPoint.y;
		currentPoint.setLocation(newX, newY);
		
		//Clear screen
		LCD.clear();
		
		LCD.drawString(": Current X", 11, 3);
		LCD.drawString(": Current Y", 11, 4);
		LCD.drawInt(currentPoint.x, 9, 3);
		LCD.drawInt(currentPoint.y, 9, 4);
		
		//Display newPoint
		LCD.drawString(":New X", 11, 6);
		LCD.drawString(":New Y", 11, 7);
		LCD.drawInt(newPoint.x, 9, 6);
		LCD.drawInt(newPoint.y, 9, 7);
	}	

	/**
	 * The main method puts everything together. It takes the methods from GridNavigator as well as
	 * from ButtonCounter.java to navigate the robot.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//Set instance variable myMS4 and ButtonCounter myButtCount to operate robot
		GridNavigator myMS4 = new GridNavigator();
		ButtonCounter myButtCount = new ButtonCounter();		
		
		//Calibrate the robot
		myMS4.calibrate();
		
		//Display "loading" string and wait 1 second.
		LCD.drawString("Loading...", 0, 0);
		Delay.msDelay(1000);
		myMS4.currentPoint.setLocation(0,0);
		
		//Run this loop until someone manually ends the program on the robot.
		while(true)
		{
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
			myMS4.ShortestPath(myMS4.newPoint.x, myMS4.newPoint.y);
			myMS4.StorePoints(); //Run StorePoints()
		}
	}
}