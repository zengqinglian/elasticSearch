package eric.zeng.elasticsearch.model;


public abstract class Facet {
    private final String fieldName;

    /**
     * @param fieldName
     */
    public Facet(String fieldName) {
        super();
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Facet other = (Facet) obj;
        if (fieldName == null) {
            if (other.fieldName != null)
                return false;
        } else if (!fieldName.equals(other.fieldName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Facet [fieldName=" + fieldName + "]";
    }
}
