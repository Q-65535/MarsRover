package gpt;

public class PlanNode extends TreeNode {
    final private Literal[] prec;
    private final TreeNode[] body;

    public PlanNode(String name) {
        super(name);
        this.prec = new Literal[0];
        this.body = new TreeNode[0];
    }

    public PlanNode(String name, Literal[] prec, TreeNode[] body) {
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
        for (int i = 0; i < body.length; i++) {
            body[i].setParent(this);
        }
    }

    // Connect steps in the plan body sequentially.
    private void connectNext() {
        for (int i = 0; i < body.length - 1; i++) {
            body[i].next = body[i + 1];
        }
    }

    public Literal[] getPrec() {
        return this.prec;
    }

    public TreeNode[] getBody() {
        return this.body;
    }
}
