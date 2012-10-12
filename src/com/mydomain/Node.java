package com.mydomain;

/**
 * IEOR 140 Team 6
 * Authors: Moonsoo Choi, Sherman Siu
 * Date: October 11, 2012
 * Class Name: Node
 * Class Description: The Node class contains all the information regarding the individual nodes in 
 * Grid.java. For each node, using the methods in Node.java one can determine whether the node is blocked 
 * or not, the x-y coordinates of the node, and the distance value of the nodes. The Grid class uses the 
 * Node class to keep track of each individual node information in the coordinate grid.
 */
public class Node 
{
	//set variables
	int x;
	int y;
	int distance;
	boolean isBlocked;
	
	/**
	 * Constructor for Node set, taking inputs x and y
	 * @param _x
	 * @param _y
	 */
	public Node(int _x, int _y)
	{
		x = _x;
		y = _y;
	}
	
	/**
	 * Takes input _distance. If true, the shorter distance will be set as a new distance.
	 * Else, return false.
	 * @param _distance
	 * @return
	 */
	public boolean newDistance(int _distance)
	{
		
		if(_distance < distance)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets the distance value for the node.
	 * @param _distance
	 */
	public void setDistance(int _distance)
	{
		distance = _distance;
	}
	
	/**
	 * Retreieves the distance value for the node.
	 * @return
	 */
	public int getDistance()
	{
		return distance;
	}
	
	/**
	 * Blocks the node if there is an obstacle there.
	 */
	public void blocked()
	{
		isBlocked = true;
	}
	
	/**
	 * Unblocks a node (that was previously blocked).
	 */
	public void unblocked()
	{
		isBlocked = false;
	}
	
	/**
	 * Determines if the node is blocked or not.
	 * @return
	 */
	public boolean isBlocked()
	{
		return isBlocked;
	}

	/**
	 * Resets the node's distance value.
	 */
	public void reset()
	{
		//Sets distance as a very large value
		distance = 999999;
	}
	
	/**
	 * Retrieves the x coordinate of the node.
	 * @return
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retrieves the y coordinate of the node.
	 * @return
	 */
	public int getY()
	{
		return y;
	}
}
