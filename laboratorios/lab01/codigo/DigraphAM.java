import java.util.ArrayList;
/**
 * 
 * @author ditrefftzr, fjaram18
 */

public class DigraphAM extends Digraph {
    private int[][] matrix ; 
        /** Class constructor 
         */
	public DigraphAM(int size) {
        super(size);    
	    matrix = new int [size][size] ;
		
	}
	
        /** creates an arc between a source and a destination. the arc is given
         * a certain value
         */
	public void addArc(int source, int destination, int weight) {
                matrix[source][destination]= weight ;
	}
	
        /** Selects a vertix and returns every successor it has. 
         */
	public ArrayList<Integer> getSuccessors(int vertex) {
            ArrayList <Integer> lista = new ArrayList<>() ;      
                 for (int i=0 ; i< matrix[vertex].length ; i++) {
                     if (matrix[vertex][i]==1){
                         lista.add(i);
                     }
                }
                 return lista ;
	}
        
        public int getWeight(int source, int destination){
            return matrix [source][destination] ;
        }
}