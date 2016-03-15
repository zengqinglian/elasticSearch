package eric.zeng.elasticsearch.model;

public class Sort {
    /**
     */
    public enum OrderBy {
        RELEVACE,
        FIELD;
    }

    /**
     */
    public enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        private final String string;

        private SortOrder(String string) {
            this.string = string;
        }

        /**
         * @return
         */
        public String getString() {
            return string;
        }
    }

    private final OrderBy orderBy;
    private final String fieldName;
    private final SortOrder sortOrder;

    /**
     * Creates a field sorting filter.
     * @param orderBy
     * @param fieldName
     * @param sortOrder
     */
    public Sort(OrderBy orderBy, String fieldName, SortOrder sortOrder) {
        super();
        if (orderBy != OrderBy.FIELD) {
            throw new IllegalArgumentException("This constructor is for create a FIELD sorting.");
        }
        this.orderBy = orderBy;
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
    }

    /**
     * Creates a relevance sorting filter.
     * @param orderBy
     * @param sortOrder
     */
    public Sort(OrderBy orderBy, SortOrder sortOrder) {
        super();
        if (orderBy != OrderBy.RELEVACE) {
            throw new IllegalArgumentException("This constructor is for create a RELEVACY sorting.");
        }
        this.orderBy = orderBy;
        this.fieldName = null;
        this.sortOrder = sortOrder;
    }

    /**
     * @return the orderBy.
     */
    public OrderBy getOrderBy() {
        return orderBy;
    }

    /**
     * @return the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the sortOrder.
     */
    public String getSortOrder() {
        return sortOrder.getString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
        result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
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
        Sort other = (Sort) obj;
        if (fieldName == null) {
            if (other.fieldName != null)
                return false;
        } else if (!fieldName.equals(other.fieldName))
            return false;
        if (orderBy != other.orderBy)
            return false;
        if (sortOrder != other.sortOrder)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SortFilter [orderBy=" + orderBy + ", fieldName=" + fieldName + ", sortOrder=" + sortOrder.getString()
                + "]";
    }
}
