package com.mydomain;

import java.awt.Point;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;

/**
 * The ButtonCounter class allows you to input a certain coordinate into the robot.
 * You use the buttons on the robot to enter a coordinate; the robot will retrieve the
 * information you entered in and will navigate the robot to your desired location in 
 * Milestone3.java.
 */
public class ButtonCounter 
{
	Point myPoint; //Set Point variable myPoint

	/**
	 * The count method sets up the entire button counting and drawing system.
	 * @param msg
	 */
	public void count(String msg) 
	{
		myPoint = new Point(); //Set Point variable myPoint
		int x = 0; //Initialize integer variable x=0
		int y = 0; //Initialize integer variable y=0
		myPoint.setLocation(x, y); //Set the location of myPoint to be at (x,y)
		LCD.clear(); //Clear screen
		LCD.drawString(msg, 0, 5); //Display message
		boolean counting = true; //Set counting as a boolean = true.
		//Display messages "English?" and "Espanol?"
		LCD.drawString("English? (LEFT)", 0, 0);
		LCD.drawString("Espanol? (RIGHT)", 0, 1);
		//set integer variable languageID as a button press - an integer represents a button press combo.
		int languageID = Button.waitForAnyPress();
		LCD.clear(); //clear screen
		
		/*
		If the left arrow button is pressed, display "Welcome!", wait 100 milliseconds,
		and clear screen.
		*/
		if (languageID == 2) 
		{	
			LCD.drawString("Welcome!", 0, 0);
			Delay.msDelay(100);
			LCD.clear();
		}
		/*
		If the right arrow button is pressed, display "Bienvenidos!", wait 100 milliseconds,
		and clear screen.
		 */
		else if (languageID == 4)
		{
			LCD.drawString("Bienvenidos!", 0, 1);
			Delay.msDelay(100);
			LCD.clear();
		}

		/**
		 * This runs the actual button pressing for the new coordinates; you press certain buttons
		 * and the location will be determined.
		 */
		do 
		{
			//Display strings "X=" and "Y=", as well as the x and y values of myPoint
			LCD.drawString("X=",10,0);
			LCD.drawString("Y=",10,1);
			LCD.drawInt(myPoint.x,12,0);
			LCD.drawInt(myPoint.y,12,1);
			
			int buttonID = Button.waitForAnyPress(); //initialize button press integer
			if (buttonID > 0) //if a button is pressed
			{
				//if the left arrow button is pressed, increase myPoint.x by 1
				if (buttonID == 2) 
				{
					myPoint.x++;
				}
				/*
				Else if the left arrow button & the orange button are pressed together,
				decrease myPoint.x by 1 
				 */
				else if (buttonID == 3) 
				{
					myPoint.x--;
				}
				//if the right arrow button is pressed, increase myPoint.y by 1
				else if (buttonID == 4) 
				{
					myPoint.y++;
				}
				/*
				Else if the right arrow button & the orange button are pressed together,
				decrease myPoint.y by 1 
				 */
				else if (buttonID == 5) 
				{
					myPoint.y--;
				}
				//Else if the bottom button is pressed, depending on the situation do this:
				else if (buttonID == 8) 
				{
					//If before, you pressed the left arrow button for "English", display "Exit!":
					if (languageID == 2) 
					{
						LCD.drawString("EXIT!", 1, 5);
						Delay.msDelay(100);
						LCD.clear();
						counting = false; //End "do" method
					}
					//Else if you before chose the right arrow button for "Espanol", display "Salida!"
					else if (languageID == 4) 
					{
						LCD.drawString("SALIDA!", 1, 6);
						Delay.msDelay(100);
						LCD.clear();
						counting = false; //End "do" method
					}
					//If before, you didn't select anything, display "Exit!"
					LCD.drawString("EXIT!", 1, 5);
					Delay.msDelay(100);
					LCD.clear();
					counting = false; //End "do" method
				}
				//Else make a beeping sound, display "try again!"
				else 
				{
					LCD.drawString("Try again!", 0, 6);
					Sound.beep();
				}
			}
		}
		//The boolean "counting" is set as true. Continue running "do" method until counting = false
		while(counting);
	}
	
	/**
	 * This is an integer method getX(), which allows Milestone3.java to obtain the x-value of myPoint
	 */
	public int getX()
	{
		return myPoint.x;
	}
	/**
	 * This is an integer method getY(), which lets Milestone3.java to get the y-value of myPoint
	 * @return
	 */
	public int getY()
	{
		return myPoint.y;
	}
}
