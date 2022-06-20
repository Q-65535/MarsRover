package graphic;

import agent.AbstractAgent;
import world.Cell;
import world.Environment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.List;
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

        // draw goals
        g2d.setColor(Color.red);
        List<Cell> goalPositions = agent.getGoals();
        drawCellTo2D(g2d, goalPositions);

        //draw current goal
        g2d.setColor(Color.MAGENTA);
        drawCellTo2D(g2d, agent.getCurrentGoal());

        //draw agent
        g2d.setColor(Color.BLUE);
        drawCellTo2D(g2d, agent.getCurrentPosition());

        //draw depot
        g2d.setColor(Color.green);
        drawCellTo2D(g2d, env.getRechargePosition());

        // draw norm positions
        g2d.setColor(Color.DARK_GRAY);
        drawCellTo2D(g2d, agent.getNormPositions());

        //draw info
        g2d.setColor(Color.BLACK);
        g2d.scale(0.1, 0.1);
        String recordStr = recordToStr(agent.getCurrentFuel(), agent.getAchievedGoalCount(), agent.getTotalFuelConsumption(), agent.getRechargeFuelConsumption());
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

    private void drawCellTo2D(Graphics2D graphic, List<Cell> cells) {
        drawCellTo2D(graphic, new HashSet<>(cells));
    }

    /**
     * TODO This method will be refactored
     */
    private String recordToStr(int currentFuel, int achievedGoalCount, int totalFuelConsumption, int rechargeConsumption) {
        StringBuilder sb = new StringBuilder();
        sb.append("goals: ").append(achievedGoalCount).append(" ");
        sb.append(" cur fuel: ").append(currentFuel).append(" ");
        sb.append(" total consumption: ").append(totalFuelConsumption).append(" ");
        sb.append(" recharge consumption: ").append(rechargeConsumption).append(" ");
        return sb.toString();
    }
}
