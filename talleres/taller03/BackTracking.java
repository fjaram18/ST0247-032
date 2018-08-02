
/**
 * Write a description of class BackTracking here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BackTracking
{
    public static boolean puedoPonerReina(int r, int c, int[] tablero) {
       
        //Check hacia la izquierda
        for (int i = 1; i <= c; i++) {
            if (r == tablero[c - i] || r - i == tablero[c - i] || r + i == tablero[c - i]) return false;
        }
        
        return true;
    }
    
    
    private static boolean nReinas(int r, int n, int[] tablero) {

        
        if(r == tablero.length ) {
            
            for (int i = 0; i < r; i++) {
                System.out.println(tablero[i]);
            }
            
        } else {
            for (int c = 0; c < n; c++)
             if(puedoPonerReina(r, c, tablero)) {
                tablero[c] = r;
                nReinas(r + 1, c, tablero);
           // } else {
                
            //    if (r >= tablero.length - 1) {
              //      nReinas(tablero[c-1] + 1, c-1, tablero);
              //  } else {
               //     nReinas(r + 1, c, tablero);
              //  }
            }
        }   
        

    }
    
    public static int nReinas(int n) {
        int[] tablero = new int[n];
        return nReinas(0, 0, tablero);
    }
    
    
}
