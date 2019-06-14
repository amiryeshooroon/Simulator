package Model;

import Intepeter.Parser;
import Search.BFS;
import Search.Pair;
import Search.SearcherSolver;
import Search.State;
import server_side.FileCacheManager;
import server_side.MyClientHandler;
import server_side.MySerialServer;
import server_side.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.function.BiFunction;

public class MySimulatorModel extends Observable implements SimulatorModel {
    private double airplaneX;
    private double airplaneY;
    private String ip;
    private int port;
    private String path;
    public MySimulatorModel() {
        path = null;
    }

    @Override
    public void autoFly(String code) {
        Parser.getInstance().parse(code);
        //get airplaneX and airplaneY every some sec for the map and update, optional
    }

    @Override
    public void calculatePath(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end) {
        Socket s=null;
        PrintWriter out=null;
        BufferedReader in=null;
        try{
            s=new Socket(ip,port);
            s.setSoTimeout(300000);
            out=new PrintWriter(s.getOutputStream());
            in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            //can maybe announce on label that we calculating
            for(int i=0;i<map.size();i++){
                for(int j=0;j<map.get(i).size()-1;j++){
                    out.print(map.get(i).get(j)+",");
                }
                out.println(map.get(i).get(map.get(i).size() - 1));
            }
            out.println("end");
            out.println(start.getKey().toString() + "," + start.getValue().toString());
            out.println(end.getKey().toString() + "," + end.getValue().toString());
            out.flush();
            String usol=in.readLine();
        }catch(SocketTimeoutException e){
            //can't solve problem
        }catch(IOException e){
        }finally{
            try {
                in.close();
                out.close();
                s.close();
            } catch (IOException e) {
                //error
            }
        }
    }

    @Override
    public void pathFly(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end) {
        if(path == null){
            calculatePath(map, start, end);
        }
        //thread that move according to the string we got
        //thread gets the location every 500ms can be in timer and then updated airplaneX, airplaneY they can be i,j (airplaneX for j and airplaneY for i)
    }

    @Override
    public void joystickFly(double angle, double radius, double maxRadius, double throttle, double rudder) {

    }

    @Override
    public void connect(String connect) {
        String[] ipPort = connect.split(":");
        ip = ipPort[0];
        port = Integer.valueOf(ipPort[1]);
    }
}
