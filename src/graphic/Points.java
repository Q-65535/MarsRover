package graphic;

import agent.AbstractAgent;
import world.Cell;
import world.Environment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import javax.swing.JPanel;

public class Points extends JPanel {
    Environment env;

    public Points(Environment env) {
        this.env = env;
    }

    /**
     * this strange function do all the things to draw environment
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(8, 8);

        AbstractAgent agent = env.getAgent();

        // draw targets
        g2d.setColor(Color.red);
        Set<Cell> targetPositions = agent.getTargetPositions();
        drawCellTo2D(g2d, targetPositions);

        //draw current target
        g2d.setColor(Color.MAGENTA);
        drawCellTo2D(g2d, agent.getCurrentTarget());

        //draw agent
        g2d.setColor(Color.BLUE);
        drawCellTo2D(g2d, agent.getCurrentPosition());

        //draw depot
        g2d.setColor(Color.green);
        drawCellTo2D(g2d, env.getRechargePosition());

        //draw info
        g2d.setColor(Color.BLACK);
        g2d.scale(0.1, 0.1);
        String recordStr = recordToStr(agent.getCurrentFuel(), agent.getNumOfAchieved(), agent.getTotalFuelConsumption(), agent.getRechargeFuelConsumption());
        g2d.drawString(recordStr, 300, 800);
    }

    private void drawCellTo2D(Graphics2D graphic, Cell cell) {
        if (cell == null) {
            return;
        }
        int x = cell.getX();
        int y = cell.getY();
        graphic.drawLine(x, y, x, y);
    }

    private void drawCellTo2D(Graphics2D graphic, Set<Cell> cells) {
        for (Cell cell : cells) {
            drawCellTo2D(graphic, cell);
        }
    }

    /**
     * Ugly code to transform environment information to string
     */
    private String recordToStr(int currentFuel, int numOfAchievedGoals, int totalFuelConsumption, int rechargeConsumption) {
        StringBuilder sb = new StringBuilder();
        sb.append("goals: ").append(numOfAchievedGoals).append("\n");
        sb.append(" cur fuel: ").append(currentFuel).append("\n");
        sb.append(" total consumption: ").append(totalFuelConsumption).append("\n");
        sb.append(" recharge consumption: ").append(rechargeConsumption).append("\n");
        return sb.toString();
    }
}
