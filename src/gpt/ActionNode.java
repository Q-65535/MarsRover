package gpt;

import java.util.*;

public class ActionNode extends TreeNode {
    private List<Literal> prec;
    private List<Literal> postc;

    public ActionNode(String name) {
        super(name);
        this.prec = new ArrayList<>();
        this.postc = new ArrayList<>();
    }

    public ActionNode(String name, List<Literal> prec, List<Literal> postc) {
        super(name);
        if (prec == null) {
            throw new RuntimeException("precondition is null!");
        }
        if (postc == null) {
            throw new RuntimeException("postcondition is null!");
        }

        this.prec = prec;
        this.postc = postc;
    }
}
