/**
 * Problema de n-reinas con fuerza bruta. Laboratorio 02.
 * 
 * @author Mauricio Toro, Federico Jaramillo, David Trefftz
 */
public class fuerzaBruta {

    public static boolean puedoPonerReina(int r, int c, int[] tablero) {
        
        for (int i = 1; i <= c; i++) {
            if (tablero[c - i] == r + i || tablero[c - i] == r - i || tablero[c - i] == r) {
                return false;
            }
        }
        
        return true;
    }

    public static int queens(int n) {
        int[] tablero = new int[n];
        int cont = 0;
        //Primera columna
        int col = 0;
        //Primera fila
        tablero[col] = -1;
        int fila;

        //Ciclo para recorrer las columnas
        while (col >= 0){
            //recorre las filas hasta que encuentra una posicion segura ó llega al final de la fila
            do {
                tablero[col]++;
                fila = tablero[col];
            } while(fila < n && !puedoPonerReina(fila, col, tablero));

            if (fila < n) {
                if (col < n -1) { 
                    col++;
                    fila = -1;
                    tablero[col] = fila;
                } else {
                    cont++; //Solucion aceptada
                }
            } else {
                col--; //Ninguna fila en la columna funciono
            }

        }

        return cont;
    }
}
