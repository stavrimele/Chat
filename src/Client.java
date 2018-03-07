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
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stavri Mele
 */
public class Client {
    public static final String RED = "\u001B[31m";  //client output
    public static final String BLUE = "\u001B[34m"; //server output
    Socket connection = null;
    String serverAddress = "";
    int port;
    DataOutputStream out=null;
    DataInputStream in,in2,nome;
    String stato;
    String clientUser;
    String serverUser;
    public Client()
    {   
        //nome o IP del server
        serverAddress="localhost";
        //porta
        port=2000;   
        stato="offline";
        clientUser="Client";
        serverUser="Server";
    }
    public void connetti()
    {
        try
        {
        connection = new Socket(serverAddress, port);
        System.out.println(RED+"Connessione aperta"+RED);
        stato="online";
        }
        catch(ConnectException e){
            System.err.println(RED+"Server non disponibile!"+RED);
        }
        catch(UnknownHostException e1){
            System.err.println(RED+"Errore DNS!"+RED);
        }

        catch(IOException e2){
            System.err.println(RED+e2+RED);
            e2.printStackTrace();
        }
    }
    public void parla()
    {
        autore();
        menu();
        try
        {
            
            
            //this.smile();
            //scrittura su out
            //leggere la risposta
            //lettura dal client
            nome=new DataInputStream(connection.getInputStream());
            serverUser=nome.readUTF();
            in = new DataInputStream(connection.getInputStream());
            String serverMess=in.readUTF();
            System.out.println(BLUE+serverUser+" : "+serverMess+BLUE);
            this.end();
            stato="offline";
        }
        catch(IOException e)
        {
            System.out.println(RED+"errore I/O"+RED);
        }       
    }
    public void autore()
    {
        System.out.println(RED+"Inserisci il tuo nome utente"+RED);
        BufferedReader tastiera= new BufferedReader(new InputStreamReader(System.in));
        try {
            clientUser=tastiera.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(clientUser);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stato()
    {
        if(stato.equals("online")==true)
        {
            stato="offline";
            System.out.println(RED+"Sei offline"+RED);
        }
        else
        {
            stato="online";
            System.out.println(RED+"Sei online"+RED);
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(String.valueOf(smile));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void echo()
    {
        String same;
        try {
            same = in.readUTF();
            out = new DataOutputStream(connection.getOutputStream());
            out.writeUTF(same);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void end()
    {
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void mess()
    {
        try {
            out = new DataOutputStream(connection.getOutputStream());
            String mess;
            System.out.println(RED+"Messaggio da digitare al server: "+RED);
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
