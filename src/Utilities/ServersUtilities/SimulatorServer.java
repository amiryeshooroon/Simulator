package Utilities.ServersUtilities;

import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimulatorServer {
    private Scanner in;
    private PrintWriter out;
    private Socket socket;
    private SimulatorServer() {}

    private static class ServerHolder {
        private static final SimulatorServer server = new SimulatorServer();
    }
    public static SimulatorServer getServer(){
        return ServerHolder.server;
    }

    public void open(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        out = new PrintWriter(socket.getOutputStream());
    }
    public void stop(){
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {}
    }
    public void setVariable(String varPath, double value) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println("set " + varPath + " " + value);
        out.flush();
        in.nextLine();
    }
    public void setVariable(String varPath, boolean value) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println("set " + varPath + " " + value);
        out.flush();
    }
    public double getVariable(String varPath) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println("get " + varPath);
        out.flush();
        double val = 0;
        String str = in.nextLine();
        Scanner in1 = new Scanner(str);
        System.out.println("str: " + str);
        in1.useDelimiter("'| ");
        while(in1.hasNext()){
            if(in1.hasNextDouble()) val = in1.nextDouble();
            if(in1.hasNext()) in1.next();
            else break;
        }
        System.out.println("val: " + val);
        return val;
    }
    public void sendString(String str) throws NotConnectedToServerException{
        if(socket == null) throw new NotConnectedToServerException("You might want to check if there is a connect command");
        out.println(str);
        out.flush();
    }
    public static void main(String... args){
        SimulatorServer server = getServer();
        try {
            server.open("127.0.0.1", 5402);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(server.getVariable("/position/longitude-deg"));
        } catch (NotConnectedToServerException e) {
            e.printStackTrace();
        }
    }
}
