package game;


import java.util.HashMap;
import java.util.ArrayList;

public class PathFinderProduct {
	private HashMap<Point, Point> parents;
	private HashMap<Point, Integer> totalCost;

	public HashMap<Point, Point> getParents() {
		return parents;
	}

	public void setParents(HashMap<Point, Point> parents) {
		this.parents = parents;
	}

	public HashMap<Point, Integer> getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(HashMap<Point, Integer> totalCost) {
		this.totalCost = totalCost;
	}

	public void reParentNeighborIfNecessary(Point closest, Point neighbor, ArrayList<Point> thisOpen) {
		Point originalParent = parents.get(neighbor);
		double currentCost = costToGetTo(neighbor);
		reParent(neighbor, closest);
		double reparentCost = costToGetTo(neighbor);
		if (reparentCost < currentCost)
			thisOpen.remove(neighbor);
		else
			reParent(neighbor, originalParent);
	}

	public int costToGetTo(Point from) {
		return parents.get(from) == null ? 0 : (1 + costToGetTo(parents.get(from)));
	}

	public void reParent(Point child, Point parent) {
		parents.put(child, parent);
		totalCost.remove(child);
	}

	public int totalCost(Point from, Point to, PathFinder pathFinder) {
		if (totalCost.containsKey(from))
			return totalCost.get(from);
		int cost = costToGetTo(from) + pathFinder.heuristicCost(from, to);
		totalCost.put(from, cost);
		return cost;
	}

	public void reParentNeighbor(Point closest, Point neighbor, ArrayList<Point> thisOpen) {
		reParent(neighbor, closest);
		thisOpen.add(neighbor);
	}
}