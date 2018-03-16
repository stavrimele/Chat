
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.EOFException;
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
    public static final String RED = "\u001B[31m";  //client output
    public static final String BLUE = "\u001B[34m"; //server output
    Socket conn;
    DataOutputStream out=null;
    DataInputStream in= null;
    String user="";
    String user2="";
    String echo="Questo è il messaggio che dovrebbe essere come quello ricevuto";
    boolean end=false;
    Gestore (Socket connection, String user) 
    {   
        conn=connection;
        this.user=user;
        try {
            //stream di input
            in = new DataInputStream(conn.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //stream di output
            out = new DataOutputStream(conn.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(user.equalsIgnoreCase("Server"))
        {
            user2="Client";
            while(conn.isConnected()){
                //il server inizia la comunicazione
                menu(); //comunica
                lettura(); //si mette in ascolto verso il client
            }
        }
        else
        {
            user2="Server";
            
            while(conn.isConnected()){
                //il client si mette in ascolto
                lettura(); //ascolta
                menu(); //comunica
            }
            
        }
    }
    public void lettura(){
        
        try {
            /*
                salvo il messaggio ricevuto in una variabile per stamparlo
                in caso dell'evocazione del metodo echo che restituisce 
                l'ultimo messaggio ricevuto
            */
            echo=in.readUTF();
            System.out.println(RED+user2+": " + echo+RED);
        } catch(SocketException e){
            System.out.println(BLUE+user2+" HA CHIUSO LA CONNESSIONE"+BLUE);
            chiudi();
        }
        catch(EOFException e){
            System.out.println(BLUE+user2+" HA CHIUSO LA CONNESSIONE"+BLUE);
            System.exit(0);
        }
        catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void scrittura(){
        
        System.out.print (BLUE+user+": "+BLUE);
        //inserisco il messaggio da inviare
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
            System.out.println(BLUE+user2+" HA CHIUSO LA CONNESSIONE"+BLUE);
            System.exit(0);
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
    public void smile(){
        try {
            out = new DataOutputStream(conn.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        //dichiarazione variabile utilizzata per lo smile
        char smile='\u263A';
        try {
            //scrittura
            out.writeUTF(String.valueOf(smile));
        } 
        catch(SocketException e){
            System.out.println(BLUE+user2+" HA CHIUSO LA CONNESSIONE"+BLUE);
            System.exit(0);
        }
        catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //svuotamento del canale
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void like(){
        
    }
    public void echo()
    {
        try {
            //stream di output
            out = new DataOutputStream(conn.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print (BLUE+user+": "+BLUE);
        try {
            //scrittura
            out.writeUTF(echo);
            System.out.print(BLUE+echo+BLUE);
        } 
        catch(SocketException e){
            System.out.println(BLUE+user2+" HA CHIUSO LA CONNESSIONE"+BLUE);
            System.exit(0);
        }
        catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //svuotamento canale
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void menu()
    {
        System.out.println("\nPremi 2 per chiudere la connessione\n"+
                            "Premi 3 per inviare uno smile\n"+
                            "Premi 4 per inviare un like (beta)\n"+
                            "Premi 5 per inviare lo stesso messaggio ricevuto\n"+
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
            if(num==2)
            {
                chiudi();
            }
            if(num==3)
            {
                smile();
            }
            if(num==4)
            {
                like();
            }
            if (num==5)
            {
                echo();
            }
        }
    }
    public void chiudi()
    {
        try {
            if(out!=null)
                out.close();
            if(in!=null)
                in.close();
            conn.close();
            System.out.println(RED+"HAI CHIUSO LA CONNESSIONE"+RED);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
