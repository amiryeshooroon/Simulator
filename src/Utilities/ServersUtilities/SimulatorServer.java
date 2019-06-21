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
            in.next();
            in.next();
            String s = in.next();
            s = s.replace("\'","");
            return Double.valueOf(s);
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
