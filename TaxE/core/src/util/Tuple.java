package util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Tuple<T,U> {
    private T one;
    private U two;

    public Tuple(T one, U two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(one).
                append(two).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple))
            return false;
        if (obj == this)
            return true;

        @SuppressWarnings("unchecked")
		Tuple<T,U> rhs = (Tuple<T,U>) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                        append(one, rhs.one).
                append(two, rhs.two).
                isEquals();
    }
}
