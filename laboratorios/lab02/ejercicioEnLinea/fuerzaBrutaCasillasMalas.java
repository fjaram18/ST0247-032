import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javafx.util.Pair;

/**
 * Problema de n-reinas con fuerza bruta con casillas "muertas" para el Laboratorio 02, código basado en la solución de rossetacode.org
 * @author Mauricio Toro, Federico Jaramillo, David Trefftz
 */
public class fuerzaBrutaCasillasMalas {

    public static boolean puedoPonerReina(int r, int c, int[] tablero) {

        for (int i = 1; i <= c; i++) {
            if (tablero[c - i] == r + i || tablero[c - i] == r - i || tablero[c - i] == r) {
                return false;
            }
        }

        return true;
    }

    public static int queens(int n, ArrayList<Pair<Integer, Integer>> badSquares) {
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
                    cont++;
                    for (int i = 0; i < badSquares.size(); i++) {
                        int x = badSquares.get(i).getKey();
                        int y = badSquares.get(i).getValue();
                        if (tablero[y] == x) {
                            cont--;//Espacio muerto, solución invalida
                        }
                    }
                }
            } else {
                col--; //Ninguna fila en la columna funciono
            }

        }

        return cont;
    }

    public static void main(String [] args) {
        File file = new File("ejemplo.txt");
        try {
            Scanner scanner = new Scanner(file);
            int size = Integer.parseInt(scanner.nextLine());
            int n = size;
            ArrayList<Pair<Integer, Integer>> badSquares = new ArrayList<Pair<Integer, Integer>>();
            int caso = 1;

            do {
                String line = scanner.nextLine();
                for (int i = 0; i < size; i++) {
                    if (line.charAt(i) == '*') {
                        badSquares.add(new Pair<>(size - n, i));
                    }
                }
                n--;
                if (n == 0) {
                    int respuesta = queens(size, badSquares);
                    n = Integer.parseInt(scanner.nextLine());
                    size = n;
                    badSquares.clear();
                    System.out.println("Case " + caso + ": " + respuesta);
                    caso++;
                }
            } while (n > 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
