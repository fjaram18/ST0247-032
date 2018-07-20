import java.util.ArrayList;
/**
 *  NOTA: Se decidió eliminar el atributo "Weight" ya que
    no aporta para la creación y uso de la matriz. Si se necesitara
    hayar un flujo máximo, por ejemplo, se tendría en cuenta.
 * @author ditrefftzr, fjaram18
 */

public class DiagraphMatrix {
    private int[][] matrix ; 

	public DiagraphMatrix(int size) {
            matrix = new int [size][size] ;
		
	}
        
	public void addArc(int source, int destination) {
                matrix[source][destination]= 1 ;
	}

	public ArrayList<Integer> getSuccessors(int vertex) {
            ArrayList <Integer> lista = new ArrayList<>() ;      
                 for (int i=0 ; i< matrix[vertex].length ; i++) {
                     if (matrix[vertex][i]==1){
                         lista.add(i);
                     }
                }
                 return lista ;
	}
}
