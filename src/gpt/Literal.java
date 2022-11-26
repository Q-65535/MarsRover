package gpt;

public class Literal {
    private Formula formula;
    private boolean state;

    public Literal(Formula formula, boolean state) {
        this.formula = formula;
        this.state = state;
    }

    public Formula getFormula() {
        return this.formula;
    }

    public boolean getState() {
        return this.state;
    }

    /**
     * Flip the state of this literal */
    public void flip() {
        this.state = !this.state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Literal l = (Literal) o;
        boolean isFormulaEqual = this.formula.equals(l.formula);
        boolean isStateEqual = this.state == l.state;
        return isFormulaEqual && isStateEqual;
    }

    @Override
    public int hashCode() {
        int result = formula.hashCode();
        result = 31 * result + (state ? 1 : 0);
        return result;
    }

    @Override
    public Literal clone() {
        return new Literal(this.formula, this.state);
    }

    @Override
    public String toString() {
        if (this.state == true) {
            return this.formula + "+";
        } else {
            return this.formula + "-";
        }
    }
}
