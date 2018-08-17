import java.util.ArrayList;
import java.util.Stack;

public class Taller4 {
    /**
     * Determina si hay un camino desde el vértice v hasta el vértice w
     */
    public static boolean hayCaminoDFS(Digraph g, int v, int w) {
        boolean[] visitados = new boolean[g.size()];
        return hayCaminoDFS(g, v, w, visitados);
    }

    public static boolean hayCaminoDFS(Digraph g, int v, int w, boolean[] visitados) {
        // Ya llegué al nodo final
        if(v == w) return true;
        // Ver si ya se visitó este nodo
        if(visitados[v]) return false;
        // Marcarlo como visitado
        visitados[v] = true;
        // Tomar los vértices con los que v se conecta
        ArrayList<Integer> successors = g.getSuccessors(v);
        // Si el nodo v no tiene sucesores, retornar false;
        if(successors == null) return false;
        // Barrer los sucesores de v
        for(Integer i: successors) {
            if(hayCaminoDFS(g, i, w, visitados)) return true;
        }
        return false;
    }


    public static int costoMinimo(Digraph g, int inicio, int fin) {
        // Definir las estructuras de datos que se van a usar
        int numVertices = g.size();
        // q es el conjunto de los nodos no visitados
        // la entrada para el nodo "n" está en true si "n" no ha sido visitado
        boolean [] q = new boolean[numVertices];
        // distancia del nodo inicio a cada vértice
        int [] dist = new int[numVertices];
        // nodo previo en la ruta óptima
        int [] prev = new int[numVertices];
        
        // Inicializar los arreglos
        for(int i = 0; i < numVertices; i++) {
            dist[i] = Integer.MAX_VALUE;        // infinito
            prev[i] = -1;                       // no definido
            q[i] = true;                        // no visitado
        }
        // distancia del nodo inicial a si mismo es 0
        dist[inicio] = 0;
        
        // Todavía hay nodos por visitar?
        while(qNotEmpty(q)) {
            int u = vertexWithMinDistance(q, dist);
            // eliminar el vértice u del conjunto q (ya está visitado)
            q[u] = false;
            // Ya se miraron todos los posibles caminos hacia el nodo fin?
            if(u == fin) {
                imprimirRecorrido(prev, fin);
                break;
            }
            // Encontrar los sucesores de u
            ArrayList<Integer> successors = g.getSuccessors(u);
            if(successors == null) continue;
            // Visitar cada sucesor de V
            for(Integer v: successors) {
                // Ver si hay que actualizar la distancia óptima al nodo v
                int alt = dist[u] + g.getWeight(u, v);
                if(alt < dist[v]){
                    dist[v] = alt;
                    prev[v] = u;
                }
            }
        }
        
        return dist[fin];
    }
    
    private static boolean qNotEmpty(boolean [] q) {
        for(int i = 0; i < q.length; i++) {
            if(q[i]) return true;
        }
        return false;
    }
    
    private static int vertexWithMinDistance(boolean [] q, int [] dist) {
        int vert = -1;
        int minDist = Integer.MAX_VALUE;
        // Barrer todos los vértices
        for(int i = 0; i < q.length; i++) {
            // El vértice está en q y su distancia es menor
            if(q[i] && dist[i] <= minDist) {
                vert = i;
                minDist = dist[i];
            }
        }
        return vert;
    }
    
    public static void imprimirRecorrido(int [] prev, int fin) {
        System.out.println("Recorrido: ");
        Stack s = new Stack();
        s.push(fin);
        int prevNode = prev[fin];
        while(prevNode != -1) {
            s.push(prevNode);
            prevNode = prev[prevNode];
        }
        while(!s.empty()) {
            System.out.println(s.pop());
        }
    }

/**
     * Recorre todo un grafo completo desde el nodo v, 
     * visitando todos los nodos una vez y volviendo a v.
     * Se escoge el costo mínimo
     */
    static int costoMinimo = Integer.MAX_VALUE;
    static int [] path;
    
    public static void recorridoCompleto(Digraph g, int v) {
        int numVertices = g.size();
        // visited[n] == true iff n has been visited
        boolean [] visited = new boolean[numVertices];
        // next stores the best path
        int [] next = new int[numVertices + 1];
        // best path
        path = new int[numVertices + 1];
        // depth of current level
        int depth = 0;
        // first node in the path is v
        next[0] = v;
        visited[v] = false;         // para que se "visite" al final
        // call the recursive method
        recorridoCompleto(g, v, depth + 1, next, visited);
        // print cost of best path and best path
        System.out.println(costoMinimo);
        System.out.println(Arrays.toString(path));
    }
    
    /**
     * depth: la profundidad que se va a llenar
     */
    public static void recorridoCompleto(Digraph g, int v, int depth, 
        int [] next, boolean [] visited) {
        int numVertices = g.size();
        // A full path has been detected, comming back to v
        if(depth == numVertices) {
            next[numVertices] = v;
            // compute the cost
            int nuevoCosto = computeCost(g, next);
            // does this path have a lower cost?
            if (nuevoCosto < costoMinimo) {
                costoMinimo = nuevoCosto;
                savePath(next);
            }
            return;
        }
        // Try the unvisited neighbors that succed the node in the previous level
        ArrayList<Integer> successors = g.getSuccessors(next[depth - 1]);
        for(Integer u: successors) {
            // Visit the non-visited successors
            if(!visited[u]) {
                // Visit u at this depth
                next[depth] = u;       
                // Mark u as visited
                visited[u] = true;
                // Recursive call for next level
                recorridoCompleto(g, v, depth + 1, next, visited);
                // Mark u as not visited, for possible next visits
                visited[u] = false;
            }
        }
    }
    
    /**
     * Computes the cost of the path stored in array next
     */
    public static int computeCost(Digraph g, int [] next) {
        int numVertices = g.size();
        int cost = 0 ;
        for(int i = 0; i < numVertices; i++) {
            cost += g.getWeight(next[i], next[i+1]);
        }
        return cost;
    }
    
    /**
     * Saves the current path in array "path"
     */
    public static void savePath(int [] next) {
        for(int i = 0; i < next.length;i++) {
            path[i] = next[i];
        }
    }

    public static void probarHayCaminoDFS() {
        DigraphAM matriz = new DigraphAM(7);
        matriz.addArc(0, 1, 1);
        matriz.addArc(1, 2, 1);
        matriz.addArc(1, 3, 1);
        matriz.addArc(2, 1, 1);
        matriz.addArc(2, 4, 1);
        matriz.addArc(3, 6, 1);
        matriz.addArc(4, 5, 1);
        matriz.addArc(4, 6, 1);
        System.out.println(hayCaminoDFS(matriz, 1, 6));
    }
    
    public static void probarCostoMinimo() {
        DigraphAM matriz = new DigraphAM(7);
        matriz.addArc(0, 1, 10);
        matriz.addArc(1, 2, 10);
        matriz.addArc(1, 3, 20);
        matriz.addArc(2, 1, 30);
        matriz.addArc(2, 4, 10);
        matriz.addArc(3, 6, 40);
        matriz.addArc(4, 5, 30);
        matriz.addArc(4, 6, 10);
        System.out.println("Costo es: " + costoMinimo(matriz, 1, 6));
    }
}
