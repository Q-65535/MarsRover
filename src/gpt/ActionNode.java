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

    public List<Literal> getPrec() {
	return prec;
    }

    public List<Literal> getPostc() {
	return postc;
    }


	// @Incomplete: For the current problem domain, we say two actions are
	// equal when they have the same name.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionNode that = (ActionNode) o;

        return this.getName().equals(that.getName());
    }

	// The hash code is determined by its name.
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
