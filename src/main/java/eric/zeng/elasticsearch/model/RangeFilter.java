package eric.zeng.elasticsearch.model;

public class RangeFilter<T> implements Filter {
    private final String fieldName;
    private final T start;
    private final T end;
    private final boolean includeLower;
    private final boolean includeUpper;

    /**
     * @param fieldName
     * @param start
     * @param includeLower
     * @param includeUpper
     * @param end
     */
    public RangeFilter(String fieldName, T start, boolean includeLower, boolean includeUpper, T end) {
        super();
        this.fieldName = fieldName;
        this.start = start;
        this.includeLower = includeLower;
        this.includeUpper = includeUpper;
        this.end = end;
    }

    /**
     * @param fieldName
     * @param start
     * @param includeLower
     */
    public RangeFilter(String fieldName, T start, boolean includeLower) {
        this(fieldName, start, includeLower, false, null);
    }

    /**
     * @param fieldName
     * @param includeUpper
     * @param end
     */
    public RangeFilter(String fieldName, boolean includeUpper, T end) {
        this(fieldName, null, false, includeUpper, end);
    }

    /**
     * @return the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the start.
     */
    public T getStart() {
        return start;
    }

    /**
     * @return the end.
     */
    public T getEnd() {
        return end;
    }

    /**
     * @return the includeLower.
     */
    public boolean isIncludeLower() {
        return includeLower;
    }

    /**
     * @return the includeUpper.
     */
    public boolean isIncludeUpper() {
        return includeUpper;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = prime * result + (includeLower ? 1231 : 1237);
        result = prime * result + (includeUpper ? 1231 : 1237);
        result = prime * result + ((start == null) ? 0 : start.hashCode());
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
        RangeFilter<?> other = (RangeFilter<?>) obj;
        if (end == null) {
            if (other.end != null) {
                return false;
            }
        } else if (!end.equals(other.end)) {
            return false;
        }
        if (fieldName == null) {
            if (other.fieldName != null) {
                return false;
            }
        } else if (!fieldName.equals(other.fieldName)) {
            return false;
        }
        if (includeLower != other.includeLower) {
            return false;
        }
        if (includeUpper != other.includeUpper) {
            return false;
        }
        if (start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!start.equals(other.start)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RangeFilter [fieldName=" + fieldName + ", start=" + start + ", end=" + end + ", includeLower="
                + includeLower + ", includeUpper=" + includeUpper + "]";
    }
}
