package View;

import Utilities.ColoredRange;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDisplayer extends Canvas {
    private List<List<Double>> map;
    private List<ColoredRange> ranges;
    private List<String> colors;

    public void displayMap(List<List<Double>> newMap){
        map = newMap;
        ranges = new ArrayList<>(10);
        colors = Arrays.asList("#E83B47", "#FF9933", "#F7B26D", "#F6F66C", "D6FD87", "#CDF778", "#B5F23C", "#7FF23C", "#74E533", "#339900");
        redraw();
    }

    private String getValueColor(double d){
        for(ColoredRange coloredRange : ranges) if(coloredRange.inRange(d)) return coloredRange.getColor();
        return null;
    }

    private void calculateColors(){
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (List<Double> doubles : map) {
            for (Double d : doubles) {
                if (d > max) max = d;
                if (d < min) min = d;
            }
        }
        double jmp = (max-min)/10.0;
        for(int i=0;i<10; ranges.add(new ColoredRange(min, min+jmp, colors.get(i))), i++, min+=jmp);
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
                gc.setFill(Color.WHITE);
                gc.fillRect(j*cellWidth, i*cellHighet, cellWidth, cellHighet);
                gc.strokeText(String.valueOf(map.get(i).get(j).intValue()), j*cellWidth, i*cellHighet, cellWidth);
            }
        }
    }
}
