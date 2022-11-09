package gpt;

public class Literal {
    private String name;
    private boolean state;

    public Literal(String name, boolean state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return this.name;
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
        boolean isNameEqual = this.name.equals(l.name);
        boolean isStateEqual = this.state == l.state;
        return isNameEqual && isStateEqual;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (state ? 1 : 0);
        return result;
    }

    @Override
    public Literal clone() {
        return new Literal(this.name, this.state);
    }

    @Override
    public String toString() {
        if (this.state == true) {
            return this.name + "+";
        } else {
            return this.name + "-";
        }
    }
}
