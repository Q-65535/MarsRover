package graphic;

import agent.AbstractAgent;
import world.Boundary;
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
     * This strange function do all the things to draw the environment
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(17, 17);

        AbstractAgent agent = env.getAgent();


        //draw current goal
        g2d.setColor(Color.MAGENTA);
        drawCellTo2D(g2d, agent.getCurrentGoal());


        //draw depot
        g2d.setColor(Color.green);
        drawCellTo2D(g2d, env.getRechargePosition());

        // draw norm positions
        g2d.setColor(Color.DARK_GRAY);
        drawCellTo2D(g2d, agent.getNormPositions());

		// draw boundaires
		List<Boundary> brs = agent.getBoundaries();

		for (int i = 0; i < brs.size(); i++) {
			Boundary br = brs.get(i);
			// draw from cells.
			g2d.setColor(new Color(200, 56, 14));
			drawCellTo2D(g2d, br.fromCells);
			// draw to cells.
			g2d.setColor(new Color(230, 150, 83));
			drawCellTo2D(g2d, br.toCells);
		}

        // draw goals
        g2d.setColor(Color.red);
        List<Cell> goalPositions = agent.getGoals();
        drawCellTo2D(g2d, goalPositions);

        //draw agent
        g2d.setColor(Color.BLUE);
        drawCellTo2D(g2d, agent.getCurrentPosition());


        //draw status string
        g2d.setColor(Color.BLACK);
        g2d.scale(0.1, 0.1);
        String recordStr = recordToStr(agent.getCurrentFuel(), agent.getAchievedGoalCount(), agent.getTotalFuelConsumption(), agent.getRechargeFuelConsumption());
        g2d.drawString(recordStr, 30, 350);
    }

    private void drawCellTo2D(Graphics2D graphic, Set<Cell> cells) {
        for (Cell cell : cells) {
            drawCellTo2D(graphic, cell);
        }
    }

    private void drawCellTo2D(Graphics2D graphic, List<Cell> cells) {
        drawCellTo2D(graphic, new HashSet<>(cells));
    }

    private void drawCellTo2D(Graphics2D graphic, Cell cell) {
        if (cell == null) {
            return;
        }
        int x = cell.getX();
        int y = cell.getY();
        graphic.drawLine(x, y, x, y);
    }

    /**
     * TODO This method will be refactored
     */
    private String recordToStr(int currentFuel, int achievedGoalCount, int totalFuelConsumption, int rechargeConsumption) {
        StringBuilder sb = new StringBuilder();
        sb.append("goals: ").append(achievedGoalCount).append(" ");
        sb.append(" cur fuel: ").append(currentFuel).append(" ");
        sb.append(" total cons: ").append(totalFuelConsumption).append(" ");
        sb.append(" recharge cons: ").append(rechargeConsumption).append(" ");
        return sb.toString();
    }
}
