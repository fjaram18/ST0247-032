import java.util.ArrayList;
import javafx.util.Pair;

/**
 * Implementacion de un grafo dirigido usando listas de adyacencia
 *
 * @author Mauricio Toro, Federico Jaramillo, David Trefftz 
 */
public class DigraphAL extends Digraph {
    ArrayList<ArrayList<Pair<Integer, Integer>>> vertices;
    
    public DigraphAL(int size) {
        super(size);
        vertices = new ArrayList<ArrayList<Pair<Integer, Integer>>>(size);
        
        for (int i = 0; i < size; i++) {
            ArrayList<Pair<Integer, Integer>> verVacio = new ArrayList<Pair<Integer, Integer>>();
            vertices.add(verVacio);
        }
    }

    public void addArc(int source, int destination, int weight) {
        vertices.get(source).add( new Pair<>(destination, weight) );
    }

    public ArrayList<Integer> getSuccessors(int vertex) {
        ArrayList<Pair<Integer, Integer>> vertx = vertices.get(vertex);
        ArrayList<Integer> solucion = new ArrayList<Integer>();
        
        for (int i=0; i<vertx.size(); i++) {
            int vecino = vertx.get(i).getKey();
            solucion.add(vecino);
        }
        
        
        return solucion;
    }

    public int getWeight(int source, int destination) {
        ArrayList<Pair<Integer, Integer>> vertx  = vertices.get(source);
        
        for (int i=0; i<vertx.size(); i++){
            int adj = vertx.get(i).getKey();
            if (adj == destination) {
                return vertx.get(i).getValue();
            }
        }
        return 0;
    }

}