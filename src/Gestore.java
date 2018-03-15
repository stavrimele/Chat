
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
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
public class Gestore {
    Socket conn;
    DataOutputStream out=null;
    DataInputStream in= null;
    Client c;
    Server s;
    String clientUser="Client";
    String serverUser="Server";
    String user="";
    String user2="";
    GestoreServer g;
    Gestore (Socket connection, String user) 
    {   
        conn=connection;
        this.user=user;
        
        if(user.equalsIgnoreCase("Server"))
        {
            user2="Client";
            while(true){
                menu();
                lettura();
            }
        }
        else
        {
            user2="Server";
            while(true){
                lettura();
                menu();
            }
            
        }
    }
    public void lettura(){
        try {
            in = new DataInputStream(conn.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println(user2+": " + in.readUTF());
        } catch(SocketException e){
            System.out.println(user+" HA CHIUSO LA CONNESSIONE");
    }catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void scrittura(){
        try {
            out = new DataOutputStream(conn.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print (user+": ");
        BufferedReader tastiera=new BufferedReader (new InputStreamReader(System.in));
        String message=null;
        try {
            message = tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(message);
        } 
        catch(SocketException e){
            System.out.println(user+" HA CHIUSO LA CONNESSIONE");
        }
        catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
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
            if(num==2)
            {
                chiudi();
            }
        }
    }
    public void autore()
    {
        System.out.println("Inserisci il tuo nome");
        BufferedReader tastiera=new BufferedReader (new InputStreamReader(System.in));
        try {
            clientUser=tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        scrittura();
    }
     public void parla()
    {
        while(conn!=null)
        {
            menu();
            //g.scrittura();
            lettura();
        }
    }
    public void chiudi()
    {
        try {
        conn.close();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
