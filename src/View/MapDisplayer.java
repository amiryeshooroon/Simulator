package View;

import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.Math.ColoredRange;
import Utilities.ServersUtilities.SimulatorServer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDisplayer extends Canvas {
    private List<List<Double>> map;
    private List<String> colors;
    private double maxHighet, minHighet;
    private double canvasWidth;
    private double canvasHighet;
    private double cellWidth;
    private double cellHighet;
    private Image xPhoto;
    private Image plane;
    private double prevPlaneX, prevPlaneY;
    private double prevXX, prevXY;
    private double longitude, latitude, area;
    private volatile boolean stop;
    GraphicsContext gc;
    public void displayMap(List<List<Double>> newMap, double longitude, double latitude, double area){
        map = newMap;
        gc = null;
        prevXX = 0;
        prevXY = 0;
        xPhoto = new Image(getClass().getResource("Xphoto.png").toString());
        plane = new Image(getClass().getResource("plane.png").toString());
        this.longitude = longitude;
        this.latitude = latitude;
        this.area = area;
        calculateMinMax(); //
        redraw();
        prevPlaneX = 0;
        prevPlaneY = 0;
        stop = false;
        new Thread(()->{
            while(!stop) {
                try {
                    drawPlane(SimulatorServer.getServer().getVariable("/position/longitude-deg"), SimulatorServer.getServer().getVariable("/position/latitude-deg"));
                } catch (NotConnectedToServerException e) {
                }
            }
        }).start();
    }

    private void calculateMinMax(){
        minHighet = Double.MAX_VALUE; maxHighet = Double.MIN_VALUE;
        for (List<Double> doubles : map) {
            for (Double d : doubles) {
                if (d > maxHighet) maxHighet = d;
                if (d < minHighet) minHighet = d;
            }
        }
    }

    public void redraw() {
        if(map == null){
            //draw enpty
            return;
        }
        canvasWidth = getWidth();
        canvasHighet = getHeight();
        cellWidth = canvasWidth / map.get(0).size();
        cellHighet = canvasHighet / map.size();
        gc = getGraphicsContext2D();
        for(int i=0;i<map.size(); i++)
            for(int j=0;j<map.get(0).size(); j++)
                redrawAt(i,j);
    }
    public void redrawAt(int i, int j){
        gc.setFill(Color.color( 1 - (map.get(i).get(j)-minHighet)/(maxHighet-minHighet), (map.get(i).get(j)-minHighet)/(maxHighet-minHighet),0));
        gc.fillRect(j*cellWidth, i*cellHighet, cellWidth, cellHighet);
    }
    public void drawX(double x, double y){
        if(gc != null) {
            redrawAt((int)(prevXY/cellHighet), (int)(prevXX / cellWidth));
            gc.drawImage(xPhoto, ((int)(x / cellWidth))*cellWidth, ((int)(y/cellHighet))*cellHighet, cellWidth, cellHighet);
            prevXX = x;
            prevXY = y;
        }
    }
    public void drawPlane(double x, double y){
        if(gc != null) {
            redrawAt((int)(prevPlaneY/cellHighet), (int)(prevPlaneX / cellWidth));
            gc.drawImage(plane, ((int)((x-longitude) / area))*cellWidth, ((int)((y-latitude)/area))*cellHighet, cellWidth, cellHighet);
            prevPlaneX = x;
            prevPlaneY = y;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        stop = true;
    }
}
