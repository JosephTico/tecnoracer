package sockets;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class SocketCliente
 {
     /** Programa principal, crea el socket cliente */
     public static void main (String [] args)
     {
         ObjectMapper mapper = new ObjectMapper();//Para poder hacer operaciones JSON
         Estudiante estudiante = new Estudiante(20, "Sebastián");
         new SocketCliente(); //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Aquí se inicia la conexion

         /*try {
             //Pasando de objeto a string en formato json
             String jsonInString = mapper.writeValueAsString(estudiante);
             System.out.println(jsonInString);//aquí se ve impreso, es lo que eventualmente se envia por medio del DatoSocket



             //OJO LA CLASE QUE QUERES PARSEAR DEBE DE TENER CONSTRUCTOR POR DEFECTO (VACIO SIN PARAMETROS)!!!!!!!!!!!!!!!
             //De string en formato JSON a objeto.
             Estudiante obj = mapper.readValue(jsonInString, Estudiante.class);
             System.out.println(obj.getNombre());

         } catch (JsonProcessingException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }*/

     }
     
     /**
      * Crea el socket cliente y lee los datos
      */
     public SocketCliente()
     {
         try
         {
             /* Se crea el socket cliente */
             Socket socket = new Socket("localhost", 15557);
             System.out.println ("conectado");

             /* Se hace que el cierre espere a la recogida de todos los datos desde
             * el otro lado */
             socket.setSoLinger (true, 10);
             
             /* Se obtiene un stream de lectura para leer objetos */
             DataInputStream bufferEntrada =
                new DataInputStream (socket.getInputStream());
             
             /* Se lee un Datosocket que nos env�a el servidor y se muestra 
              * en pantalla */
             DatoSocket dato = new DatoSocket("");
             dato.readObject(bufferEntrada);
             System.out.println ("Cliente Java: Recibido " + dato.toString());

             /* Se obtiene un flujo de envio de datos para enviar un dato al servidor */
             DataOutputStream bufferSalida =
               new DataOutputStream (socket.getOutputStream());

             /* Se crea el dato y se escribe en el flujo de salida */
             DatoSocket aux = new DatoSocket ("Zx");
             aux.writeObject (bufferSalida);

             System.out.println ("Cliente Java: Enviado " + aux.toString());
           
             /* La llamada a setSoLinger() har� que el cierre espere a que el otro
             lado retire los datos que hemos enviado */
             socket.close();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
     }
}
