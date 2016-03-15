package eric.zeng.elasticsearch.model;

public class ValueFilter<T> implements Filter {
    private final String fieldName;
    private T value;

    /**
     * Creates a value filter.
     * @param fieldName the field name to set.
     * @param value the value to set.
     */
    public ValueFilter(String fieldName, T value) {
        super();
        this.fieldName = fieldName;
        this.value = value;
    }

    /**
     * @return the field name.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param value the value to set.
     */
    public void setValue(final T value) {
        this.value = value;
    }

    /**
     * @return the value.
     */
    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ValueFilter<?> other = (ValueFilter<?>) obj;
        if (fieldName == null) {
            if (other.fieldName != null) {
                return false;
            }
        } else if (!fieldName.equals(other.fieldName)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ValueFilter [fieldName=" + fieldName + ", value=" + value + "]";
    }
}
