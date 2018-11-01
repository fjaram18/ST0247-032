import java.lang.System;
/**
 * Write a description of class carro here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Peugeot
{
    double bateria;
    double tiempoTrayecto;
    int nodoActual;
    String trayecto;

    public Peugeot(double Q) {
        bateria = Q;
        tiempoTrayecto = 0;
        nodoActual = 0;
        trayecto = "{0,0.00}";
    }

    public int getNodo() {
        return nodoActual;
    }

    public void cambiarNodoActual(int nodoDestino) {
        nodoActual = nodoDestino;
    }

    public double getBateria() {
        
        return bateria;
       
    }

    public void usarBateria(double uso) {
        bateria -= uso;
    }

    public void recargarBateria(double Q) {
        bateria = Q;
    }

    public double getTiempo() {
        return tiempoTrayecto;
    }

    public void adicionarTiempo(double tiempo) {
        tiempoTrayecto += tiempo;
    }
    
    public String recordarTrayecto(){
        return trayecto;
    }

    public void adicionarTrayecto(int nodo, double tiempo) {
        double tiempoRounded = round(tiempo);
        trayecto += ", {" + nodo + "," + tiempoRounded + "}";
    }

    //Codigo tomado de internet
    public static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
