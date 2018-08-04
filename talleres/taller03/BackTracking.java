/**
 * Clase en la cual se implementan los metodos del Taller de Clase #3
 * 
 * @author Mauricio Toro, Mateo Agudelo, David Trefftz, Federico Jaramillo
 */
public class BackTracking
{
    static int a = 0;
    
    public static boolean puedoPonerReina(int r, int c, int[] tablero) {
        
        for (int i = 0; i < r; i++) {
            if ((tablero[i] - c) == (r - i) || (c - tablero[i]) == (r - i) || tablero[i] == c) {
                return false;
            }
        }
        
        return true;
    }
    
    public static int nReinas(int r, int n, int[] tablero) {
        
        if(r == n) {
            imprimirTablero(tablero);
            a++;
        } else {
            for (int c = 0; c < n; c++) {
                tablero[r] = c;
                 if(puedoPonerReina(r, c, tablero)) {
                    nReinas(r + 1, n, tablero);
                }
            }
        } 
        
        return a;
    }
    
    public static int nReinas(int n) {
        int[] tablero = new int[n];
        a = 0;
        return nReinas(0, n, tablero);
    }
    
    public static void imprimirTablero(int[] tablero) {
		int n = tablero.length;
		System.out.print("    ");
		for (int i = 0; i < n; ++i)
			System.out.print(i + " ");
		System.out.println("\n");
		for (int i = 0; i < n; ++i) {
			System.out.print(i + "   ");
			for (int j = 0; j < n; ++j)
				System.out.print((tablero[i] == j ? "Q" : "#") + " ");
			System.out.println();
		}
		System.out.println();
	}
}
