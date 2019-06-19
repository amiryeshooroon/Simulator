package View;

import Utilities.Math.ColoredRange;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDisplayer extends Canvas {
    private List<List<Double>> map;
    private List<String> colors;
    private double maxHighet, minHighet;
    public void displayMap(List<List<Double>> newMap){
        map = newMap;
        calculateMinMax();
        redraw();
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
        double canvasWidth = getWidth();
        double canvasHighet = getHeight();
        double cellWidth = canvasWidth / map.get(0).size();
        double cellHighet = canvasHighet / map.size();

        GraphicsContext gc = getGraphicsContext2D();
        for(int i=0;i<map.size(); i++){
            for(int j=0;j<map.get(0).size(); j++){
                gc.setFill(Color.color( 1 - (map.get(i).get(j)-minHighet)/(maxHighet-minHighet), (map.get(i).get(j)-minHighet)/(maxHighet-minHighet),0));
                gc.fillRect(j*cellWidth, i*cellHighet, cellWidth, cellHighet);
//                gc.setFill(Color.color( 0, 0,0));
//                gc.fillOval(j*cellWidth, i*cellHighet,cellWidth,cellHighet);
            }
        }
    }

}
