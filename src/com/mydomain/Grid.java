package com.mydomain;

import java.util.ArrayList;

/** 
 * IEOR 140 Team 6
 * Date: October 11, 2012
 * Class Name: Grid
 * Authors: Moonsoo Choi and Sherman Siu
 * Program description: The Grid class is responsible for calculating the distances of each node 
 * on the coordinate grid from your desired destination.  Throughout the grid navigation, the Grid class 
 * will maintain an internal “map” of the node distances and will keep track of which nodes are blocked 
 * (as determined by the robot as it navigates). The robot will navigate based off of the smallest 
 * distance value determined by Grid.java for each neighboring node. 
 */
public class Grid 
{
	//Variables set
	int gridX;
	int gridY;
	int neighborX;
	int neighborY;
	int newDist = 0;
	Node[][] nodes;
	Node destination;
	Node currentNode;
	//Create an array list of nodes.
	ArrayList<Node> myNodes = new ArrayList<Node>();
	
	/**
	 * Sets constructor Grid, taking in values _gridX and _gridY. Then creates a node
	 * for every coordinate in the determined Grid.
	 * @param _gridX
	 * @param _gridY
	 */
	public Grid(int _gridX, int _gridY) 
	{
		//Below, all the nodes are reset
		gridX = _gridX;
		gridY = _gridY;
		System.out.println(gridX);
		
		//Setting all the nodes.
		nodes = new Node[gridX][gridY];
		for(int X_index = 0; X_index < gridX; X_index ++)
		{
			for(int Y_index = 0; Y_index < gridY; Y_index ++)
			{
				nodes[X_index][Y_index] = new Node(X_index, Y_index);
			}
		}
	}
	
	/**
	 * recalc() calculates the distance value for each node in the grid. It first resets all the nodes,
	 * then it sets the distance of the destination to zero. From there, for each valid neighboring node
	 * the distance increases by one from the distance node, then subsequently increasing by one from that
	 * neighboring node, and so forth.
	 */
	public void recalc()
	{
		//Set variables
		int X_index = 0;
		int Y_index = 0;
		int shortestPath = 0;
		int currentNodeIndex = myNodes.indexOf(destination);
		Node currentNode = destination;
		
		//Reset all the nodes in the grid.
		for(X_index = 0; X_index < gridX; X_index ++)
		{
			for(Y_index = 0; Y_index < gridY; Y_index ++)
			{
				nodes[X_index][Y_index].reset();
				myNodes.add(nodes[X_index][Y_index]);
			}
		}
		//Set destination node to have a distance of 0
		destination.setDistance(0);
		/*
		 * In this while loop, the nodes are being set as the distance from the destination node. This
		 * is done by having the unblocked neighboring nodes around the destination to have a distance
		 * value of 1, then from then on, each neighboring node from the previous neighboring nodes have
		 * an increasing distance of one until the entire grid is calculated.
		 */
		while(myNodes.size() > 0)
		{
			//Set values
			shortestPath = 99999;
			currentNodeIndex = 0;
			//Retrieve the nodes in the grid
			for(int index = 0; index < myNodes.size(); index ++)
			{
				if(myNodes.get(index).getDistance() < shortestPath)
				{
					shortestPath = myNodes.get(index).getDistance();
					currentNodeIndex = index;
				}	
				X_index = myNodes.get(currentNodeIndex).getX();
				Y_index = myNodes.get(currentNodeIndex).getY();
				currentNode = nodes[X_index][Y_index];
			}
			//Remove this selected node from the array list of nodes so it won't be reselected.
			myNodes.remove(myNodes.get(currentNodeIndex));
			//For each neighboring node from this selected node, add one to the distance.
			for(int direction = 0; direction <= 3; direction ++)
			{
				if(neighbor(currentNode, direction) != null)
				{
					if(neighbor(currentNode, direction).newDistance(currentNode.getDistance() + 1))
					{
						neighbor(currentNode, direction).setDistance(currentNode.getDistance() + 1);
					}
				}
			}	
		}
	}
	
	/**
	 * Set the destination node in the grid.
	 * @param _X
	 * @param _Y
	 */
	public void setDestination(int _X, int _Y)
	{
		destination = nodes[_X][_Y];
	}
	
	/**
	 * The constructor retrieves the neighboring nodes from the current node. Based on the direction, the constructor
	 * will return a node either to the left, right, front, or back, of the robot. It will return a null if the node is 
	 * either non-existent or is blocked.
	 * @param _nodes
	 * @param direction
	 * @return
	 */
	public Node neighbor(Node _nodes, int direction)
	{
		//This gets the node for (x+1,y) coordinate of the robot
		if(direction%4 == 0)
		{
			if(_nodes.getX() < gridX - 1 && nodes[_nodes.getX()+1][_nodes.getY()].isBlocked == false)
			{
				return nodes[_nodes.getX()+1][_nodes.getY()];
			}
			else
			{
				return null;
			}
		}
		//This gets the node for (x,y+1) coordinate of the robot
		else if(direction%4 == 1)
		{
			if(_nodes.getY() < gridY - 1 && nodes[_nodes.getX()][_nodes.getY()+1].isBlocked == false)
			{
				return nodes[_nodes.getX()][_nodes.getY()+1];
			}	
			else
			{
				return null;
			}
		}
		//This gets the node for (x-1,y) coordinate of the robot 
		else if(direction%4 == 2)
		{
			if(_nodes.getX() > 0 && nodes[_nodes.getX()-1][_nodes.getY()].isBlocked == false)
			{
				return nodes[_nodes.getX()-1][_nodes.getY()];
			}	
			else
			{
				return null;
			}
		}
		/*
		 * If the nodes are null for left, right, and in front of the robot, then the robot can only get the coordinate 
		 * (x,y-1), which is the location it previously was. 
		 */
		else
		{
			if(_nodes.getY() > 0 && nodes[_nodes.getX()][_nodes.getY()-1].isBlocked == false)
			{
				return nodes[_nodes.getX()][_nodes.getY()-1];
			}
			
			else
			{
				return null;
			}
		}
	}
}
