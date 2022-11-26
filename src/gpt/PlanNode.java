package gpt;

import java.util.*;

public class PlanNode extends TreeNode {
    final private List<Literal> prec;
    private final List<TreeNode> body;

    public PlanNode(String name) {
        super(name);
        this.prec = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public PlanNode(String name, List<Literal> prec, List<TreeNode> body) {
        super(name);
        if (prec == null) {
            throw new RuntimeException("precondition is null!");
        }
        if (body == null) {
            throw new RuntimeException("plan body is null!");
        }
        this.prec = prec;
        this.body = body;
        connectParent();
        connectNext();
    }

    // Connect each step in the plan body to this plan node.
    private void connectParent() {
        for (int i = 0; i < body.size(); i++) {
            body.get(i).setParent(this);
        }
    }

    // Connect steps in the plan body sequentially.
    private void connectNext() {
        for (int i = 0; i < body.size() - 1; i++) {
            body.get(i).next = body.get(i + 1);
        }
    }

    public List<Literal> getPrec() {
        return this.prec;
    }

    public List<TreeNode> getBody() {
        return this.body;
    }
}
