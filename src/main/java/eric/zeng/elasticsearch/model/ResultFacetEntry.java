
package eric.zeng.elasticsearch.model;


public abstract class ResultFacetEntry {
    private final long count;

    /**
     * @param count
     */
    public ResultFacetEntry(long count) {
        super();
        this.count = count;
    }

    /**
     * @return the count.
     */
    public long getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (count ^ (count >>> 32));
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
        ResultFacetEntry other = (ResultFacetEntry) obj;
        if (count != other.count)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ResultFacetEntry [count=" + count + "]";
    }
}
