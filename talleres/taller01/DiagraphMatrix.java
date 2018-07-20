import java.util.ArrayList;
/**
 * 
 * @author ditrefftzr, fjaram18
 */

public class DiagraphMatrix {
    private int[][] matrix ; 
        /** Class constructor 
         */
	public DiagraphMatrix(int size) {
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
        
        public Integer getWeight(int source, int destination){
            return matrix [source][destination] ;
        }
}
