package Model;

public interface TSP {
	/**
	 * Search for a shortest cost hamiltonian circuit in <code>g</code> within
	 * <code>timeLimit</code> milliseconds (returns the best found tour whenever the
	 * time limit is reached) Warning: The computed tour always start from vertex 0
	 * 
	 * @param timeLimit
	 * @param g
	 */
	public int searchSolution(int timeLimit, Graph g);

	/**
	 * @param i
	 * @return the ith visited vertex in the solution computed by
	 *         <code>searchSolution</code> (-1 if <code>searcheSolution</code> has
	 *         not been called yet, or <code>if i &lt; 0 or i &gt;= g.getNbSommets())</code>
	 */
	public Integer getSolution(int i);

	/**
	 * @return the total cost of the solution computed by
	 *         <code>searchSolution</code> (-1 if <code>searcheSolution</code> has
	 *         not been called yet).
	 */
	public float getSolutionCost();

	/**
	 * 
	 * @param timeLimit indicates the execution duration allowed for the TSP (if the
	 *                  duration exceeds this time, TSP stops
	 * @return 1 if the execution duration of the method has reached timeLimit, 0
	 *         otherwise (TSP is done computing)
	 */
	public int continueTSP(int timeLimit);

}
