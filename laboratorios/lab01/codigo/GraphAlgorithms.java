import java.util.ArrayList;
/**
 * Algoritmos aplicables a grafos
 * 
 * @author ditrefftzr, fjaram18
 * 
 */
public class GraphAlgorithms
{
    public static int verticePopular(Digraph digraph) {
        int size = digraph.size();
        int ans = 0;
        for (int i = 0; i < size; i++) {
            ArrayList <Integer> lista = digraph.getSuccessors(i);
            if (ans < lista.size()) {
                ans = lista.size();
            }
        }
        return ans;
    }
}
