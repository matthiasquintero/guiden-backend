package com.hylicmerit.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.hylicmerit.models.Node;

public class BestFirstSearchHelper {
	private static List<Node> visitedNodesInOrder = null;
	private static Node[][] graph = null;
	
	public static List<Node> bestFirstSearch(int rows, int columns, int[] start, int[] end, int[][] walls) {
		PriorityQueue<Node> minPQ = new PriorityQueue<Node>(rows*columns,(a,b)->
				a.getDistance().compareTo(b.getDistance()));
		visitedNodesInOrder = new ArrayList<Node>();
		// declare start and end nodes
		Node startNode = null, endNode = null;
		graph = new Node[rows][columns];
		// build graph
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				// current node in graph (nodes have an initial distance of infinity)
				Node current = new Node(row, col);
				// add to graph
				graph[row][col] = current;
				// check if node is a wall
				if (checkIfWall(walls, current))
					current.setWall(true);
				if (row == start[0] && col == start[1]) {
					// set start node
					startNode = current;
					// initialize starting node to a distance of 0
					startNode.setDistance(0);
					startNode.setStart(true);
				} else if (row == end[0] && col == end[1]) {
					// set end node
					endNode = current;
					endNode.setFinish(true);
				}
			}
		}
		
		minPQ.add(startNode);
		while(!minPQ.isEmpty()) {
			Node current = minPQ.poll();
			current.setVisited(true);
			if(current.isWall()) continue;
			visitedNodesInOrder.add(current);
			if(current.getRow() == end[0] && current.getColumn() == end[1]) {
				return visitedNodesInOrder; 
			}
			else {
				for(Node neighbor : getUnvisitedNeighbors(current)) {
					if(!neighbor.isVisited()) {
						int x = (int) Math.pow(endNode.getColumn() - current.getColumn(), 2.0);
						int y = (int) Math.pow(endNode.getColumn() - current.getColumn(), 2.0);
						neighbor.setVisited(true);
						neighbor.setDistance(x + y);
						minPQ.add(neighbor);
					}
				}
			}
		}
		return visitedNodesInOrder;
	}
	
	private static List<Node> getUnvisitedNeighbors(Node node) {
		List<Node> unvisitedNeighbors = new ArrayList<Node>();
		Integer row = node.getRow();
		Integer column = node.getColumn();
		// check if node has left neighbor
		if (column != 0)
			unvisitedNeighbors.add(graph[row][column - 1]);
		// check if node has right neighbor
		if (column != graph[0].length - 1)
			unvisitedNeighbors.add(graph[row][column + 1]);
		// check if node has neighbor above
		if (row != 0)
			unvisitedNeighbors.add(graph[row - 1][column]);
		// check if node has neighbor below
		if (row != graph.length - 1)
			unvisitedNeighbors.add(graph[row + 1][column]);
		// iterate through neighbors to remove visited ones
		Iterator<Node> nIterator = unvisitedNeighbors.iterator();
		// remove visited neighbors
		while (nIterator.hasNext()) {
			Node neighbor = nIterator.next();
			if (neighbor.isVisited()) {
				nIterator.remove();
			} else {
				neighbor.setPreviousNode(node);
			}
		}
		return unvisitedNeighbors;
	}

	private static boolean checkIfWall(int[][] walls, Node current) {
		for (int i = 0; i < walls.length; i++) {
			if (current.getRow() == walls[i][0] && current.getColumn() == walls[i][1]) {
				return true;
			}
		}
		return false;
	}

	public static List<Node> getShortestPathAnimation() {
		List<Node> shortestPathAnimations = new ArrayList<Node>();
		// get end node
		Node node = visitedNodesInOrder.get(visitedNodesInOrder.size() - 1);
		// while node has previous node
		while (node != null) {
			Node prevNode = node.getPreviousNode();
			shortestPathAnimations.add(0, node);
			node = prevNode;
		}
		return shortestPathAnimations;
	}

}
