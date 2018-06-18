/*
 * Javier Abell�n. 9 Dic 2003
 *
 * DatoSocket.java
 * Dato que se va a enviar entre servidores y clientes java y c.
 */

package sockets;

import java.io.*;

/**
 * Dato para enviar por el socket. Sus atributos son simples y una Clase Atributo
 */
public class DatoSocket implements Serializable
 {
     public int size = 0;
     public String data = "";

   public DatoSocket (String data)
   {
      // Si data no es null, se guarda la cadena y su longitud
      if (data != null)
      {
         size = data.length();
         this.data = data;
      }
   }

   public String toString ()
   {
       String resultado;
       resultado = Integer.toString(size) + " -- " + data;
       return resultado;
   }

   /**
   * Método para escribir los atributos de esta clase en un DataOutputStream de forma
   * que luego pueda entenderlos un programa en C.
   */
   public void writeObject(java.io.DataOutputStream out)
         throws IOException
     {
         // Se envia la longitud del string + 1 por el \0 necesario en C
         out.writeInt (size +1);

         // Se envia el string como bytes.
         out.writeBytes (data);

         // Se envia el \0 del final
         out.writeByte ('\0');
     }
    
      /**
      * Método que lee los atributos de esta clase de un DataInputStream tal cual nos los
      * envia un programa en C.
      * Este método no contempla el caso de que se envie una cadena "", es decir, un
      * único \0.
      */
     public void readObject(java.io.DataInputStream in)
     throws IOException
     {
         // Se lee la longitud de la cadena y se le resta 1 para eliminar el \0 que
         // nos envia C.
         size = in.readInt() - 1;
         
         // Array de bytes auxiliar para la lectura de la cadena.
         byte [] aux = null;
         
         aux = new byte[size];    // Se le da el tamanho
         in.read(aux, 0, size);   // Se leen los bytes
         data = new String (aux); // Se convierten a String
         in.read(aux,0,1);     // Se lee el \0
     }
}
