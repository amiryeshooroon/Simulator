package Utilities.ServersUtilities;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class SolverCommunicator {
    private Socket solver;
    private BufferedReader br;
    private PrintWriter pr;

    public void connect(String ip, int port) throws IOException {
        solver = new Socket(ip, port);
        solver.setSoTimeout(300000);
        br = new BufferedReader(new InputStreamReader(solver.getInputStream()));
        pr = new PrintWriter(new OutputStreamWriter(solver.getOutputStream()));
    }
    public void flushOutput(){
        pr.flush();
    }
    public void sendLineWithNoLineSeperator(String str){
        pr.print(str);
    }
    public void sendLineWithLineSeperator(String line){
        pr.println(line);
    }
    public String readLine(){
        try {
            return br.readLine();
        } catch (IOException e) { /*nothing to read*/}
        return null;
    }
    public void disconnect(){
        try {
            br.close();
            pr.close();
            solver.close();
        } catch (IOException e) {}
    }
}
