public class GrafoBipartido
{
    int [][] matriz;
    /**
     * Lee el archivo y crea la matriz de adyacencia
     */
    public boolean leerArchivo(Scanner archivo) {
        try {
            int numNodos = archivo.nextInt();
            if(numNodos != 0) {
                // Crear la matriz
                matriz = new int[numNodos][numNodos];
                // Leer los arcos
                int numArcos = archivo.nextInt();
                for(int i = 0; i < numArcos; i++) {
                    int desde = archivo.nextInt();
                    int hasta = archivo.nextInt();
                    matriz[desde][hasta] = 1; 
                    matriz[hasta][desde] = 1; 
                }
                return true;
            } 
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void crearScanner(String nombre) {
        try {
            Scanner archivo = new Scanner(new FileReader(nombre));
            while(leerArchivo(archivo)) {
                esBipartido(matriz);
            }
            // El programa termina, se encontro un 0
        } catch (IOException e) {
            System.out.println("Archivo no existe");
        }
    }

    public void esBipartido (int graph[][]){
        int colores[] = new int[graph.length] ;
        for(int i= 0 ; i<graph.length; i++){
            colores[i] = -1 ;
        }

        colores[0] = 1 ;

        LinkedList<Integer> cola = new LinkedList<Integer>() ;
        cola.add(0) ;

        while (cola.size() !=0){
            int x = cola.poll();
            for (int j=0;j<graph.length;j++){
                if (graph[x][j]==1 && colores[j]==-1){
                    colores[j] = 1-colores[x] ;
                    cola.add(j) ;
                }
                else if (graph[x][j]==1 && colores[j]==colores[x]){
                    System.out.println ("No bicolorable") ;
                }
            }
        }
        System.out.println ("Bicolorable") ;

    }
}
