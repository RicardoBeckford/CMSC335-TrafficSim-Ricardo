import javax.swing.*;
import java.awt.*;

public class TrafficLightPanel extends JPanel {

    private final TrafficLight light;

    public TrafficLightPanel(TrafficLight light) {
        this.light = light;
        setPreferredSize(new Dimension(80, 180));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width - 20, (height - 40) / 3);

        int x = (width - diameter) / 2;
        int yRed = 10;
        int yYellow = yRed + diameter + 10;
        int yGreen = yYellow + diameter + 10;

        g.drawRect(x - 10, yRed - 10, diameter + 20, 3 * diameter + 40);

        g.setColor(light.getState() == LightState.RED ? Color.RED : Color.LIGHT_GRAY);
        g.fillOval(x, yRed, diameter, diameter);

        g.setColor(light.getState() == LightState.YELLOW ? Color.YELLOW : Color.LIGHT_GRAY);
        g.fillOval(x, yYellow, diameter, diameter);

        g.setColor(light.getState() == LightState.GREEN ? Color.GREEN : Color.LIGHT_GRAY);
        g.fillOval(x, yGreen, diameter, diameter);
    }

    public void refresh() {
        repaint();
    }
}
