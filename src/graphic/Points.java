package graphic;

import agent.AbstractAgent;
import gpt.At;
import gpt.Formula;
import gpt.Position;
import gpt.Tree;
import world.Environment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
     * This strange function do all the things to draw the environment
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(18, 18);

        AbstractAgent agent = env.getAgent();

        // draw goals
        g2d.setColor(Color.red);
        List<Position> goalPositions = new ArrayList<>();
	// @Incomplete: For now, we draw the goal position according to the agent's current
	// intention.
        for (Tree intention : agent.getIntentions()) {
            Formula positionFormula = intention.getTlg().getGoalConds().get(0).getFormula();
            if (positionFormula instanceof At) {
                At at = (At) positionFormula;
                goalPositions.add(at.insTerm1(agent.getBB()));
            }
        }
        drawCellTo2D(g2d, goalPositions);

        //draw current goal
        g2d.setColor(Color.MAGENTA);
//        drawCellTo2D(g2d, agent.getCurrentGoal());

        //draw agent
        g2d.setColor(Color.BLUE);
        drawCellTo2D(g2d, agent.getCurrentPosition());

        //draw depot
        g2d.setColor(Color.green);
        drawCellTo2D(g2d, env.getRechargePosition());

        // draw norm positions
        g2d.setColor(Color.DARK_GRAY);
        drawCellTo2D(g2d, agent.getNormPositions());

        //draw status string
        g2d.setColor(Color.BLACK);
        g2d.scale(0.07, 0.07);
        String recordStr = recordToStr(agent.getCurrentFuel(), agent.getAchievedGoalCount(), agent.getTotalFuelConsumption(), agent.getRechargeFuelConsumption(), agent.getCurrentPosition());
        g2d.drawString(recordStr, 10, 430);
    }

    private void drawCellTo2D(Graphics2D graphic, Set<Position> positions) {
        for (Position position : positions) {
            drawCellTo2D(graphic, position);
        }
    }

    private void drawCellTo2D(Graphics2D graphic, List<Position> positions) {
        drawCellTo2D(graphic, new HashSet<>(positions));
    }

    private void drawCellTo2D(Graphics2D graphic, Position position) {
        if (position == null) {
            return;
        }
        int x = position.getX();
        int y = position.getY();
        graphic.drawLine(x, y, x, y);
    }

    /**
     * TODO This method will be refactored
     */
    private String recordToStr(int currentFuel, int achievedGoalCount, int totalFuelConsumption, int rechargeConsumption, Position position) {
        StringBuilder sb = new StringBuilder();
        sb.append("goals: ").append(achievedGoalCount).append("\n");
        sb.append(" cur battery: ").append(currentFuel).append("\n");
        sb.append(" total consumption: ").append(totalFuelConsumption).append("\n");
        // sb.append(" recharge consumption: ").append(rechargeConsumption).append("\n");
        sb.append("cur position: ").append(position).append("\n");
        return sb.toString();
    }
}
