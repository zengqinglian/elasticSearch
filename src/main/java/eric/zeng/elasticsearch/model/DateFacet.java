package eric.zeng.elasticsearch.model;

public class DateFacet extends Facet {
    /**
     *
     */
    public enum Interval {
        YEAR("year"),
        HALF("26w"),
        QUARTER("quarter"),
        MONTH("month"),
        WEEK("week"),
        DAY("day"),
        HOUR("hour"),
        MINUTE("minute");

        private final String intervalString;

        /**
         * @param intervalString
         */
        private Interval(String intervalString) {
            this.intervalString = intervalString;
        }

        /**
         * @return the intervalString.
         */
        public String getIntervalString() {
            return intervalString;
        }
    }

    private final DateFacet.Interval interval;

    /**
     * @param fieldName
     */
    public DateFacet(String fieldName, DateFacet.Interval interval) {
        super(fieldName);
        this.interval = interval;
    }

    /**
     * @return the interval.
     */
    public DateFacet.Interval getInterval() {
        return interval;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((interval == null) ? 0 : interval.hashCode());
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
        DateFacet other = (DateFacet) obj;
        if (interval != other.interval)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DateFacet [interval=" + interval + ", getFieldName()=" + getFieldName() + "]";
    }
}
