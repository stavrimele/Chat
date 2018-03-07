/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stavri Mele
 */
public class Server {
    public static final String RED = "\u001B[31m";  //client output
    public static final String BLUE = "\u001B[34m"; //server output
    int port;
    DataInputStream in, nome;
    DataOutputStream out;
    //oggetto ServerSocket necessario per accettare richieste dal client
    ServerSocket sSocket;
    //oggetto da usare per realizzare la connessione TCP
    Socket connection;
    String stato;
    String clientUser;
    String serverUser;
    public Server()
    {
        port=2000;
        in=null;
        out=null;
        sSocket=null;
        stato="online";
        serverUser="Server";
        clientUser="Client";
    }
    public void connetti()
    {
        
        try{
            // il server si mette in ascolto sulla porta voluta
            sSocket = new ServerSocket(port);
            System.out.println(BLUE+"In attesa di connessioni!"+BLUE);
            //si è stabilita la connessione
            connection = sSocket.accept();
            sSocket.close();
            System.out.println(BLUE+"Connessione stabilita!"+BLUE);
            System.out.println(BLUE+"Socket server: " + connection.getLocalSocketAddress()+BLUE);
            System.out.println(RED+"Socket client: " + connection.getRemoteSocketAddress()+RED);
            //lettura dal client
            nome=new DataInputStream(connection.getInputStream());
            clientUser=nome.readUTF();
            in = new DataInputStream(connection.getInputStream());
            System.out.println(RED+clientUser+": " + in.readUTF()+RED);
            
        }
           catch(Exception e){
               System.err.println(BLUE+"Errore di I/O!"+BLUE);
        }
        try
        {
            autore();
            menu();
            /*String mess;
            System.out.println(BLUE+"Messaggio da digitare al client: "+BLUE);
            BufferedReader tastiera= new BufferedReader(new InputStreamReader(System.in));
            mess=tastiera.readLine();
            out = new DataOutputStream(connection.getOutputStream());
            out.writeUTF(mess);
            out.flush();*/
            connection.close();
        }
        catch(IOException e)
        {
            System.err.println(BLUE+"Errore di I/O! parla"+BLUE);
        } 
        
    }
    public void autore()
    {
        System.out.println(BLUE+"Inserisci il tuo nome utente"+BLUE);
        BufferedReader tastiera= new BufferedReader(new InputStreamReader(System.in));
        try {
            clientUser=tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(clientUser);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stato()
    {
        if(stato.equals("online")==true)
        {
            stato="offline";
            System.out.println(BLUE+"Sei offline"+BLUE);
        }
        else
        {
            stato="online";
            System.out.println(BLUE+"Sei online"+BLUE);
        }
        menu();
    }
    public void smile()
    {
        char smile='\u263A'; // '\[unicode character]'
        //['U+1F44D']=like
        //System.out.print(smile);
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(String.valueOf(smile));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void echo()
    {
        String same="";
        try {
            same = in.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(String.valueOf(same));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void end()
    {
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void mess()
    {
        try {
            out = new DataOutputStream(connection.getOutputStream());
            String mess;
            System.out.println(BLUE+"Messaggio da digitare al client: "+BLUE);
            BufferedReader tastiera= new BufferedReader(new InputStreamReader(System.in));
            mess=tastiera.readLine();
            out.writeUTF(mess);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void menu()
    {
        System.out.println(""+
                           "2. Modificare il proprio stato (in linea/non in linea)\n"+
                           "3. Inviare un emoji\n"+
                           "4. Restituire lo stesso messaggio ricevuto\n"+
                           "5. Terminare la conversazione\n"
                          );
        BufferedReader tastiera= new BufferedReader(new InputStreamReader(System.in));
        int scelta=0;
        try {
            scelta=Integer.valueOf(tastiera.readLine());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (scelta==2)
        {
            stato();
        }
        if (scelta==3)
        {
            smile();
        }
        if (scelta==4)
        {
            echo();
        }
        if (scelta==5)
        {
            end();
        }
        if (scelta==6)
        {
            mess();
        }
    }
}
