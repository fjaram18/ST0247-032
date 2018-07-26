import java.util.ArrayList;

/**
 * Clase en la cual se implementan los metodos del Taller de Clase #2
 * 
 * @author Mauricio Toro, Mateo Agudelo, Federico Jaramillo, David Trefftz
 */
public class Taller2 {

    public static ArrayList<String> combinations(String s) {
        ArrayList<String> ans = new ArrayList<String>();
        combinations("",s,ans);
        System.out.println("Original: " + s);
        return ans;
    }

    private static void combinations(String pre, String pos, ArrayList<String> list) {
        if (pos.length() == 0) {
            list.add(pre);
            System.out.println("String: \"" + pre + "\" añadida");
        } else {
           combinations(pre,pos.substring(1), list);
           combinations(pre + pos.charAt(0), pos.substring(1), list);
        }
    }

    public static ArrayList<String> permutations(String s) {
        ArrayList<String> ans = new ArrayList<String>();
        permutations("",s,ans);
        System.out.println("Original: " + s);
        return ans;
    }
    
    private static void permutations(String pre, String pos, ArrayList<String> list) {
        if (pos.length() == 0) {
            list.add(pre);
            System.out.println("String: \"" + pre + "\" añadida");
        } else {
            for (int i = 0; i < pos.length(); i++) {
                permutations(pre + pos.charAt(i), pos.substring(0,i) + pos.substring(i + 1), list); 
            }
        }
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

    public static boolean esValido(int[] tablero) {
        return false;
        // complete...
    }

    public static int queens(int n) {
        return 0;
        // complete...
    }
    
    
    public static void main(String [] args) {
	    ArrayList<String> arreglo = permutations("abcd");
	    for(String s : arreglo)
    	    System.out.println(s + " " + AdvancedEncryptionStandard.desencriptarArchivo(s));
	   
    }

}
