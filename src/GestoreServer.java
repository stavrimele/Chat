
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stavri Mele
 */
public class GestoreServer {
    Socket conn;
    DataOutputStream out=null;
    DataInputStream in= null;
    String clientUser="Client";
    String serverUser="Server";
    Gestore g;
    GestoreServer (Socket connection)
    {
        conn=connection;
    }
    GestoreServer (String user)
    {
        clientUser=user;
    }
    public void lettura()
    {
        try {
            in = new DataInputStream(conn.getInputStream());
            System.out.println(clientUser+": " + in.readUTF());
        } catch (IOException ex) {
            Logger.getLogger(GestoreServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void scrittura ()
    {
        try
        {
        out = new DataOutputStream(conn.getOutputStream());
        System.out.print("\n"+serverUser+": ");
        BufferedReader tastiera=new BufferedReader (new InputStreamReader(System.in));
        String message= tastiera.readLine();
        out.writeUTF(message);
        out.flush();
        }
        catch(IOException e)
        {
            System.err.println("Errore di I/O! parla");
        }
    }
    public void menu()
    {
        System.out.println("\nPremi 1 per inserire il tuo nome\n"+
                            "Premi 2 per chiudere la connessione\n"+
                            "Premi invio per digitare un messaggio");
                
        String scelta=null;
        BufferedReader tastiera=new BufferedReader (new InputStreamReader(System.in));
        try {
            scelta=tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(GestoreServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (scelta.length()<1)
        {
            scrittura();
        }
        if (scelta.length()==1)
        {
            int num=Integer.valueOf(scelta);
            if(num==1)
            {
                autore();
            }
        }
        
    }
    public void autore()
    {
        System.out.print("Inserisci il tuo nome");
        BufferedReader tastiera=new BufferedReader (new InputStreamReader(System.in));
        try {
            serverUser=tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        g=new Gestore(serverUser);
        scrittura();
    }
    
}
