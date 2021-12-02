package Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected Graph g;
	private float bestSolCost;
	private int timeLimit;
	private long startTime;
	private int lastVertex;
	private float lastCost;
	private Collection<Integer> unvisited;
	private Collection<Integer> visited;

	public int searchSolution(int timeLimit, Graph g) {
		if (timeLimit <= 0)
			return -1;
		if (bestSol == null) {
			lastVertex = 0;
			lastCost = 0;
			this.g = g;
			bestSol = new Integer[g.getNbVertices()];
			unvisited = new ArrayList<Integer>(g.getNbVertices() - 1);
			for (int i = 1; i < g.getNbVertices(); i++)
				unvisited.add(i);
			visited = new ArrayList<Integer>(g.getNbVertices());
			visited.add(0); // The first visited vertex is 0
			bestSolCost = Integer.MAX_VALUE;
		}
		this.timeLimit = timeLimit;
		startTime = System.currentTimeMillis();
		return branchAndBound(lastVertex, unvisited, visited, lastCost);

	}
	
	
	public int continueTSP(int timeLimit) {
		if (timeLimit <= 0)
			return -1;
		this.timeLimit = timeLimit;
		startTime = System.currentTimeMillis();
		return branchAndBound(lastVertex, unvisited, visited, lastCost);
	}

	public Integer getSolution(int i) {
		if (g != null && i >= 0 && i < g.getNbVertices())
			return bestSol[i];
		return -1;
	}

	public float getSolutionCost() {
		if (g != null)
			return bestSolCost;
		return -1;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * 
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from
	 *         <code>currentVertex</code>, visiting every vertex in
	 *         <code>unvisited</code> exactly once, and returning back to vertex
	 *         <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * 
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which
	 *         are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);

	/**
	 * Template method of a branch and bound algorithm for solving the TSP in
	 * <code>g</code>.
	 * 
	 * @param currentVertex the last visited vertex
	 * @param unvisited     the set of vertex that have not yet been visited
	 * @param visited       the sequence of vertices that have been already visited
	 *                      (including currentVertex)
	 * @param currentCost   the cost of the path corresponding to
	 *                      <code>visited</code>
	 */
	private int branchAndBound(int currentVertex, Collection<Integer> unvisited, Collection<Integer> visited,
			float currentCost) {
		if (System.currentTimeMillis() - startTime > timeLimit) {
			this.lastVertex = currentVertex;
			this.lastCost = currentCost;
			this.unvisited = new ArrayList<Integer>(unvisited);
			this.visited = new ArrayList<Integer>(visited);
			return 1;
		}
		int timeout = 0;
		if (unvisited.size() == 0) {
			if (g.isArc(currentVertex, 0)) {
				if (currentCost + g.getCost(currentVertex, 0) < bestSolCost) {
					visited.toArray(bestSol);
					bestSolCost = currentCost + g.getCost(currentVertex, 0);
				}
			}
		} else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
			Iterator<Integer> it = iterator(currentVertex, unvisited, g);
			while (it.hasNext()) {
				Integer nextVertex = it.next();
				visited.add(nextVertex);
				unvisited.remove(nextVertex);
				timeout = branchAndBound(nextVertex, unvisited, visited,
						currentCost + g.getCost(currentVertex, nextVertex));
				visited.remove(nextVertex);
				unvisited.add(nextVertex);
			}
		}

		return timeout;
	}

}
