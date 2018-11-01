/**
 *
 * @author ljpalaciom, Mauricio Toro
 */

import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
public class RuteoVehiculosElectricos {
    int n, m, u, breaks;
    double r, speed, Tmax, Smax, st_customer, Q;
    Digraph mapa;
    short tipoEstacion[];
    float pendienteFuncionCarga[];
    String filename;
    Pair<Float, Float>[] coordenadas;
    ArrayList<ArrayList<Pair<Integer, Float>>> rutas;
    float tiempoSolucion;
    
    //Arreglo y entero para ordenar y manejar los diferentes carros
    Peugeot[] flota; 
    int carros;
    

    public RuteoVehiculosElectricos(String filename) {
        this.filename = filename;
        BufferedReader lector;
        String linea;
        String lineaPartida[];
        try {
            lector = new BufferedReader(new FileReader("../DataSets/"+filename));
            double[] valores = new double[10];
            for (int i = 0; i < 10; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                valores[i] = Float.parseFloat(lineaPartida[2]);
            }

            n = (int) valores[0];
            m = (int) valores[1];
            u = (int) valores[2];
            breaks = (int) valores[3];
            r = valores[4];
            speed = valores[5];
            Tmax = valores[6];
            Smax = valores[7];
            st_customer = valores[8];
            Q = valores[9];
            
            flota = new Peugeot[m];
            carros = 0;
            
            lector.readLine();
            lector.readLine();
            lector.readLine();

            coordenadas = new Pair[n];
            tipoEstacion = new short[n];
            mapa = new DigraphAM(n);
            for (int i = 0; i <= m; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
                tipoEstacion[Integer.parseInt(lineaPartida[0])] = -1; //-1 significa cliente
            }

            for (int i = 0; i < u; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                //System.out.println(Integer.parseInt(lineaPartida[0]));
                coordenadas[Integer.parseInt(lineaPartida[0])] = new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3]));
                tipoEstacion[Integer.parseInt(lineaPartida[0])] = Short.parseShort(lineaPartida[5]);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mapa.addArc(i, j, Math.sqrt(
                            Math.pow(coordenadas[i].first - coordenadas[j].first,
                                2)
                            + Math.pow(coordenadas[i].second - coordenadas[j].second, 2)
                        )
                    );
                }
            }

            pendienteFuncionCarga = new float[3];
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]);
            }
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]) / pendienteFuncionCarga[i];
            }

            resolverRuteo();

            // Leer respuesta
            rutas = new  ArrayList();
            lector = new BufferedReader(new FileReader("../DataSets/respuesta-"+filename));
            String respuesta =  lector.lines().collect(Collectors.joining());
            respuesta = respuesta.substring(1,respuesta.length()-1);
            String[] rutasRespuesta = respuesta.split("],");
            for (String ruta : rutasRespuesta){
                ArrayList<Pair<Integer, Float>> rutaArrayList = new ArrayList();
                ruta = ruta.substring(1);
                String parejas[] = ruta.split("}, ");
                for (String pareja : parejas){                    
                    pareja = pareja.substring(1);
                    String[] elementos = pareja.split(",");
                    elementos[0] = elementos[0].replace("{","");
                    elementos[1] = elementos[1].replace("}","");
                    elementos[1] = elementos[1].replace("]","");
                    Pair<Integer, Float> parejaPair = new Pair(Integer.parseInt(elementos[0]), Float.parseFloat(elementos[1]) );
                    rutaArrayList.add(parejaPair);                  
                }
                rutas.add(rutaArrayList);                    
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public String toString() {
        return "RuteoVehiculosElectricos{" + "r=" + r + ", speed=" + speed + ", Tmax=" + Tmax + ", Smax=" + Smax + ", st_customer=" + st_customer + ", Q=" + Q + ", tiempoSolucion=" + tiempoSolucion + '}';
    }

    public void validar(){
        int i = 0;
        for (ArrayList<Pair<Integer, Float>> ruta : rutas){                    
            float tiempo = 0;
            double carga = (double)Q;
            for (int j =0; j< ruta.size() - 1; j++){
                Pair<Integer, Float> parejaJ = ruta.get(j);
                Pair<Integer, Float> parejaJPlus1 = ruta.get(j+1);
                carga = carga - mapa.getWeight(parejaJ.first,parejaJPlus1.first)*r;
                if (tipoEstacion[parejaJ.first] >= 0)
                    carga = carga + pendienteFuncionCarga[tipoEstacion[parejaJ.first]]*parejaJ.second;
                tiempo += parejaJ.second;   
            }
            if (tiempo > Tmax)
                System.out.println("La ruta "+i+" supera el Tmax");
            ++i;
            //System.out.println(i + " " + carga);
            if (carga < 0)
                System.out.println("La ruta "+i+" no respeta la restricción de carga");
        }

    }


    public void resolverRuteo() {
        boolean calculandoResp = true;
        boolean terminado = true;
        
        boolean[] visitados = new boolean[m + 1];
        visitados[0] = true;
        
        //Distancia maxima recorrida posible con la bateria completa;
        double maxDis = Q / r;

        //Calcula rutas de vehiculos
        while(calculandoResp) {
            //Calcula nodo mas cercano a depot no visitado alcanzable
            int nodoDepo = -1; // -1 indica que no hay nodo alcanzable no visitado
            double minDis = Double.MAX_VALUE;
            for (int i = 1; i <= m; ++i) {
                double dis = mapa.getWeight(0, i);
                if ( dis < minDis && visitados[i] == false) {
                    for (int k = m + 1; k < n; ++k) {
                        double disEstacion = mapa.getWeight(i, k);
                        
                        boolean alcanzable = false;
                        double consumoBateria = (disEstacion + dis) * r;
                        if (consumoBateria < Q) {
                            alcanzable = true;
                        }
                        if (alcanzable || dis < maxDis / 2) {
                            nodoDepo = i;
                            minDis = dis;
                            break;
                        }
                    }
                }
            }

            //Calcula nodo mas cercano a cada vehiculo teniendo en cuenta que una estacion de carga sea alcanzable desde este y no este visitado
            int[] nodoCercano = new int[m];
            int carroPaMover = -1; //-1 implica que ningun carro se movera
            double absMinDis = Double.MAX_VALUE;
            for (int i = 0; i < carros; ++i) { //Ciclo de vehiculos
                
                nodoCercano[i] = -1; //No alcanza ningun nodo
                
                int nodoActual = flota[i].getNodo();
                
                double disMinNodos = Double.MAX_VALUE; 
                
                double nivelBateria = flota[i].getBateria();
                
                double tiempoTrayecto = flota[i].getTiempo();
                
                for (int j = 1; j <= m; ++j) { //Ciclo de nodos 
                    
                    double disNodos = mapa.getWeight(nodoActual, j);
                    if (disNodos < disMinNodos && visitados[j] == false && nodoActual != j) { //nodo mas cercano
                        
                        //Mira si desde nodoActual se puede llegar a una estacion de carga pasando por j 
                        for (int k = m + 1; k < n; ++k) {
                            double disEstacion = mapa.getWeight(j, k);
                            
                            boolean alcanzable = false;
                            double consumoBateria = (disEstacion + disNodos) * r;
                            if (nivelBateria - consumoBateria > 0) {
                                alcanzable = true;
                            }
                            
                            //Mira si tiene el tiempo y bateria para llegar desde j al depot o si tiene el tiempo para llegar al depot parando en una estacion de carga
                            boolean llegaAlDepo = false;
                            double disSinParar = mapa.getWeight(j,0);
                            double tiempoSinParar = (disNodos + disSinParar / speed) + st_customer + tiempoTrayecto;
                            double bateriaSinParar = nivelBateria - (disNodos + disSinParar) * r;
                            //Mira si tiene tiempo de llegar al depot parando en una estacion de carga 
                            double disEstacionDepo = mapa.getWeight(k, 0);
                            double tiempoParando = tiempoTrayecto + (disNodos + disEstacion + disEstacionDepo / speed) + st_customer + Smax;
                            
                            if (tiempoParando < Tmax) {
                                llegaAlDepo = true;
                            }
                            
                            if (tiempoSinParar < Tmax && bateriaSinParar > 0) {
                                llegaAlDepo = true;
                            }
                            
                            if (alcanzable && llegaAlDepo) {
                                disMinNodos = disNodos;
                                nodoCercano[i] = j;
                                if (disNodos < absMinDis) {
                                    absMinDis = disNodos;
                                    carroPaMover = i;
                                }
                            }
                        }
                    }
                }
            }
            
            
            //Decide siguiente movimiento basado en el movimiento con distancia mas corta
            int decision = 0;
            
            if (nodoDepo != -1 && carroPaMover == -1) { //Unico mov. valido es del depo
                if (carros == 0) { //Primer carro
                    decision = 1;
                } else {
                    decision = 4; //Recargar carros
                    if (flota[0].getBateria() == Q) { //Si ya estan cargados
                        decision = 1;
                    }
                }
            }
            
            if (nodoDepo == -1 && carroPaMover != -1) {
                decision = 2;
            }
            
            if (nodoDepo != -1 && carroPaMover != -1) {
                double disDepo = mapa.getWeight(0, nodoDepo);
                double disCarro = mapa.getWeight(flota[carroPaMover].getNodo(), nodoCercano[carroPaMover]);
                if (disCarro < disDepo) {
                    decision = 2;
                } else {
                    decision = 1;
                }
            }
            
            if (nodoDepo == -1 && carroPaMover == -1) {
                decision = 4;
                if (carros == 0) {
                    decision = 3;
                }
                if (carros > 0 ) {
                    if (flota[0].getBateria() == Q) {
                        decision = 5;
                    }
                }
            }
            
            switch(decision) {
                case 0:  //No deberia entrar a este caso
                    break;
                    
                case 1: //"Crear" carro en deposito y mandarlo 
                    flota[carros] = new Peugeot(Q);
                    moverCarro(carros, nodoDepo);
                    flota[carros].adicionarTiempo(st_customer);
                    visitados[nodoDepo] = true;
                    flota[carros].adicionarTrayecto(nodoDepo, st_customer);
                    carros++;
                    break;
                    
                case 2: //Mover carro a el cliente mas cercano
                    moverCarro(carroPaMover, nodoCercano[carroPaMover]);
                    visitados[nodoCercano[carroPaMover]] = true;
                    flota[carroPaMover].adicionarTiempo(st_customer);
                    flota[carroPaMover].adicionarTrayecto(nodoCercano[carroPaMover], st_customer);
                    break;
                    
                case 3: //el primer cliente no es alcanzable desde el depo sin parar en estacion de carga
                    flota[carros] = new Peugeot(Q);
                    int estacionCarga = -1;
                    for (int i = m + 1; i < n; ++i) { //ciclo de estaciones de carga
                        if (mapa.getWeight(0, i) < maxDis) {
                            for (int j = 1; j <= m; ++j) { //ciclo de clientes
                                if ( mapa.getWeight(i, j) < maxDis / 2 ) {
                                    estacionCarga = i;
                                    break;
                                }
                            }
                        }
                    }
                    moverCarro(carros, estacionCarga);
                    double tiempoCargando = (Q - flota[carros].getBateria()) / pendienteFuncionCarga[tipoEstacion[estacionCarga]];
                    flota[carros].recargarBateria(Q);
                    flota[carros].adicionarTiempo(tiempoCargando);
                    flota[carros].adicionarTrayecto(estacionCarga, tiempoCargando);
                    carros++;
                    break;
                    
                case 4: //recargar todos los carros
                    for (int i = 0; i < carros; ++i) {
                        recargarCarro(i);
                    }
                    break;
                    
                case 5: //hay algun cliente inalcanzable   
                    terminado = false;
                    calculandoResp = false;
                    break;
                    
                default:
                    break;
            }
            
            boolean visitadosTodos = true;
            for (int i = 0; i <= m; ++i) {
                if (visitados[i] == false) {
                    visitadosTodos = false;
                    break;
                }
            }
            
            if (visitadosTodos) {
                calculandoResp = false;
            }
        }
        
        if (!terminado) {
            for (int i = 0; i <= m; ++i) {
                if (visitados[i] == false) {
                    int estacionIntermedia = -1;
                    double tiempoMinimo = Double.MAX_VALUE;
                    double tiempoCarga = 0;
                    for (int j = m + 1; j < n; ++j) {
                        double distanciaDepoEstacion = mapa.getWeight(0, j);
                        double distanciaEstacionCliente = mapa.getWeight(j, i);
                        
                        if (distanciaDepoEstacion < maxDis && distanciaEstacionCliente < maxDis / 2) {
                            double usoBateria = distanciaDepoEstacion * r;
                            float pendienteDeCarga = pendienteFuncionCarga[tipoEstacion[j]];
                            double cuantaEnergia = Q - usoBateria;
                            double tiempoRuta = (distanciaEstacionCliente + distanciaDepoEstacion / speed) + (cuantaEnergia / pendienteDeCarga);
                            if (tiempoRuta < tiempoMinimo) {
                                tiempoMinimo = tiempoRuta;
                                estacionIntermedia = j;
                                tiempoCarga = (Q - cuantaEnergia) / pendienteDeCarga;
                            }
                        }
                    }
                    
                    flota[carros] = new Peugeot(Q);
                    moverCarro(carros, estacionIntermedia);
                    flota[carros].adicionarTiempo(tiempoCarga);
                    flota[carros].adicionarTrayecto(estacionIntermedia, tiempoCarga);
                    flota[carros].recargarBateria(Q);
                    
                    moverCarro(carros, i);
                    flota[carros].adicionarTiempo(st_customer);
                    flota[carros].adicionarTrayecto(i, st_customer);
                    visitados[i] = true;
                    carros++;
                    
                }
            }
            
            letsGoBackHome();
            
        }else {
            letsGoBackHome();
        }
        
        System.out.println(writeAnswer());
        
    }
    
    public void moverCarro(int carro, int nodoDestino) {
        double distancia = mapa.getWeight(flota[carro].getNodo(), nodoDestino);
        double usoBateria = distancia * r;
        double tiempoRecorrido = distancia / speed;
        flota[carro].usarBateria(usoBateria);
        flota[carro].adicionarTiempo(tiempoRecorrido);
        flota[carro].cambiarNodoActual(nodoDestino);
    }
    
    public void recargarCarro(int carro) {
        Peugeot carroActual = flota[carro];
        int nodoActual = carroActual.getNodo();
        double bateria = carroActual.getBateria();
        
        int estacionSelecta = -1;
        double minTiempo = Double.MAX_VALUE;
        
        for (int i = m + 1; i < n; ++i) {
            
            double disAEstacion = mapa.getWeight(nodoActual, i);
            boolean llega = false;
            double bateriaDespues = bateria - disAEstacion * r;
            
            if ( bateriaDespues > 0) {
                llega = true;
            }
            
            float rateOfCharge = pendienteFuncionCarga[tipoEstacion[i]];
            double energyNeeded = Q - bateriaDespues;
            double tiempoReq = (disAEstacion / speed) + (energyNeeded / rateOfCharge);

            if ( tiempoReq < minTiempo) {
                minTiempo = tiempoReq;
                estacionSelecta = i;
            }
        }
        
        moverCarro(carro, estacionSelecta);
        
        double tiempoEnCarga = (Q - flota[carro].getBateria()) / pendienteFuncionCarga[tipoEstacion[estacionSelecta]];
        
        flota[carro].recargarBateria(Q);
        flota[carro].adicionarTiempo(tiempoEnCarga);
        flota[carro].adicionarTrayecto(estacionSelecta, tiempoEnCarga);
        
    }
    
    public void letsGoBackHome() {  //Tal vez falte agregar restricion de tiempo aqui o at
        for (int i = 0; i < carros; ++i) {
            int nodo = flota[i].getNodo();
            
            
            
            double bateria = flota[i].getBateria();
            
            
            
            double distancia = mapa.getWeight(nodo, 0);
            if (bateria - distancia * r > 0) { 
                moverCarro(i, 0);
                flota[i].adicionarTrayecto(0, 0);
            } else {
                int chargeStation = -1;
                double minDisCarga = Double.MAX_VALUE;
                for (int j = m + 1; j < n; ++j) {
                    double nodoACarga = mapa.getWeight(nodo, j);
                    if (bateria - nodoACarga * r > 0) {
                        double cargaAEstacion = mapa.getWeight(j, 0);
                        if (cargaAEstacion < Q / r) {
                            if(nodoACarga + cargaAEstacion < minDisCarga) {
                                minDisCarga = nodoACarga + cargaAEstacion;
                                chargeStation = j;
                            }
                        }
                    }
                }
                
                moverCarro(i, chargeStation);
                double chargingTime = (Q - flota[i].getBateria()) / pendienteFuncionCarga[tipoEstacion[chargeStation]];
                flota[i].recargarBateria(Q);
                flota[i].adicionarTiempo(chargingTime);
                flota[i].adicionarTrayecto(chargeStation, chargingTime);
                
               
                moverCarro(i, 0);
                
            
                flota[i].adicionarTrayecto(0, 0);
            }
        }
        
    }
    
    public String writeAnswer() {
        String ans = "[";
        for (int i = 0; i < carros; ++i) {
            ans += "[" + flota[i].recordarTrayecto() + "], ";
        }
        ans = ans.substring(0, ans.length() - 2);
        ans += "]";
        
        String ansFilename = "respuesta-" + filename;
        
        try (PrintWriter out = new PrintWriter( new File ("../DataSets/" + ansFilename) )) {
            out.println(ans);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return ans;
    }
    
    public static void main(String[] args) {
        String datosTexto = "";
        if (args.length != 1) {
         System.out.println("Write <filename>");
      } else {
         datosTexto = args[0] + ".txt";
      }
        RuteoVehiculosElectricos problema1 = new RuteoVehiculosElectricos(datosTexto);     
        
        
        problema1.validar();

    }

        
}
