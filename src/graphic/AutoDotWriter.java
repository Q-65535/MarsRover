package graphic;

import machine.*;
import gpt.*;

import java.util.*;
import java.io.*;

import static running.Utils.*;

public class AutoDotWriter {
    public static String digraphStart = "digraph {\n";
    public static String subgraphStart = "subgraph {\n";

    public void genDotFile(File outputDir, List<Automaton> autos) {
        String dotStr = genDotStr(autos);
        File outputFile = join(outputDir, 1 + ".dot");
        writeFile(outputFile, dotStr);
    }

    public String genDotStr(List<Automaton> autos) {
        StringBuilder sb = new StringBuilder();
        sb.append(digraphStart);
        for (Automaton auto : autos) {
            sb.append(genDotStr(auto));
        }
        sb.append("}\n");
        return sb.toString();
    }

    public void genDotFile(File outputDir, Automaton auto) {
        String dotStr = genDotStr(auto);
        File outputFile = join(outputDir, auto.getName() + ".dot");
        writeFile(outputFile, dotStr);

    }


    public String genDotStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        sb.append(subgraphStart);
        sb.append("label=\"" + auto.getName() + "\"\n");

        sb.append(genNodesStr(auto));
        sb.append(genEdgesStr(auto));
        sb.append(genCurNodeStr(auto));

        sb.append("}\n");
        return sb.toString();
    }

    public void genTransDotFile(File outputDir, List<Automaton> autos) {
        String dotStr = genTransDotStr(autos);
        File outputFile = join(outputDir, 1 + ".dot");
        writeFile(outputFile, dotStr);
    }

    public String genTransDotStr(List<Automaton> autos) {
        StringBuilder sb = new StringBuilder();
        sb.append(digraphStart);
        for (Automaton auto : autos) {
            sb.append(genTransDotStr(auto));
        }
        sb.append("}\n");
        return sb.toString();
    }

    public void genTransDotFile(File outputDir, Automaton auto) {
        String dotStr = genTransDotStr(auto);
        File outputFile = join(outputDir, auto.getName() + ".dot");
        writeFile(outputFile, dotStr);

    }

    public String genTransDotStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        sb.append(subgraphStart);
        sb.append("label=\"" + auto.getName() + "\"\n");

        sb.append(genNodesStr(auto));
        sb.append(genEdgesStr(auto));
        sb.append(genCurNodeStr(auto));
        sb.append(genTransitionEdgeStr(auto));

        sb.append("}\n");
        return sb.toString();
    }

    // Wrap the ugly try-catch write file.
    public void writeFile(File outputFile, String str) {
        try {
            outputFile.createNewFile(); // if file already exists, this does nothing.
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(str);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException("dot file write error");
        }
    }


    private StringBuilder genNodesStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        Set<State> states = auto.getStates();
        for (State s : states) {
            sb.append(genNodeStr(s));
        }
        return sb;
    }

    private StringBuilder genNodeStr(State s) {
        return new StringBuilder(s.getName() + "\n");
    }

    private StringBuilder genCurNodeStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        State curState = auto.getCurState();
        String color = "orange";
        sb.append(curState.getName()).append(" [color =" + "\"" + color + "\"" + "]").append("\n");
        return sb;
    }

    private StringBuilder genEdgesStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        Set<State> states = auto.getStates();
        for (State s : states) {
            sb.append(genNodeEdgesStr(s));
        }
        return sb;
    }

    private StringBuilder genNodeEdgesStr(State from) {
        StringBuilder sb = new StringBuilder();
        for (State to : from.getTransFunc().values()) {
            sb.append(genEdgeStr(from, to));
        }
        return sb;
    }

    private StringBuilder genEdgeStr(State from, State to) {
        StringBuilder sb = new StringBuilder();
        Literal transLiteral = null;
        // Find the transition literal.
        for (Map.Entry<Literal, State> pair : from.getTransFunc().entrySet()) {
            // @Robustness: Rewrite equals method?
            if (pair.getValue().equals(to)) {
                transLiteral = pair.getKey();
                break;
            }
        }
        // Add edge.
        sb.append(from.getName()).append(" -> ").append(to.getName());
        // Add dege label.
        sb.append(" [label =" + "\"" + transLiteral + "\"" + "]").append("\n");
        return sb;
    }

    private StringBuilder genTransitionEdgeStr(Automaton auto) {
        StringBuilder sb = new StringBuilder();
        State from = auto.getPreState();
        State to = auto.getCurState();
        // If transition succeed, transition color is green, red otherwise.
        String color = auto.isTransitionSucceed() ? "green" : "red";
        // Add edge.
        sb.append(from.getName()).append(" -> ").append(to.getName());
        // Add dege label.
        sb.append(" [color =" + "\"" + color + "\"" + "]").append("\n");
        return sb;
    }
}
