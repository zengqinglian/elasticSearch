package eric.zeng.elasticsearch.model;

public class TermFacet extends Facet {
    private final int size;

    /**
     * @param fieldName
     */
    public TermFacet(String fieldName, int size) {
        super(fieldName);
        this.size = size;
    }

    /**
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TermFacet other = (TermFacet) obj;
        if (size != other.size)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TermFacet [size=" + size + ", getFieldName()=" + getFieldName() + "]";
    }
}
