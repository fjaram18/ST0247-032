import java.util.ArrayList;

/**
 * Clase en la cual se implementan los metodos del Taller de Clase #6
 * 
 * @author Mauricio Toro, Mateo Agudelo, Federico Jaramillo, David Trefftz
 */
public class Taller6 {

    public static int[] cambioGreedy(int n, int[] denominaciones) {
        int[] respuesta = new int[denominaciones.length];
        boolean esNull = false;
        
        //Algoritmo voraz para encontrar la vuelta minima en monedas 
        for (int i = 0; i < denominaciones.length; i ++) {
            int temp = 0;
            temp = n / denominaciones[i];
            n -= temp * denominaciones[i];
            respuesta[i] = temp;
        }
        
        //Cambio no-exacto
        if (n != 0) {
            respuesta = null;
        }
        
        return respuesta;
    }

    
    public static int recorrido(Digraph g) {
        
        int[] visitados = new int[g.size()];
        int verticeIni = 0;
        visitados[0] = 1;
        int respuesta = 0;
        
        for (int i = 0; i < g.size(); i++) {
            
            ArrayList<Integer> sucesores = g.getSuccessors(verticeIni);
            int max = 0;
            int verticeFin = 0;
            
            for (int j = 0; j < sucesores.size(); j++) {
                int peso = g.getWeight(verticeIni, j);
                if (max < peso && visitados[j] == 0) {
                    max = peso;
                    verticeFin = j;
                }
            }
            
            visitados[verticeFin] = 1;
            verticeIni = verticeFin;
            respuesta += max;
        }
        
        return respuesta;
    }
    
}
