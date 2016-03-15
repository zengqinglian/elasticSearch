package eric.zeng.elasticsearch.model;

public class DateResultFacetEntry extends ResultFacetEntry {
    private final long time;

    /**
     * @param count
     */
    public DateResultFacetEntry(long count, long time) {
        super(count);
        this.time = time;
    }

    /**
     * @return the time.
     */
    public long getTime() {
        return time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (time ^ (time >>> 32));
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
        DateResultFacetEntry other = (DateResultFacetEntry) obj;
        if (time != other.time)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DateResultFacetEntry [time=" + time + ", getCount()=" + getCount() + "]";
    }
}
