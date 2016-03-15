package eric.zeng.elasticsearch.model;


public class TermResultFacetEntry extends ResultFacetEntry {
    private final String term;

    /**
     * @param count
     */
    public TermResultFacetEntry(long count, String term) {
        super(count);
        this.term = term;
    }

    /**
     * @return the term.
     */
    public String getTerm() {
        return term;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((term == null) ? 0 : term.hashCode());
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
        TermResultFacetEntry other = (TermResultFacetEntry) obj;
        if (term == null) {
            if (other.term != null)
                return false;
        } else if (!term.equals(other.term))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TermResultFacetEntry [term=" + term + ", getCount()=" + getCount() + "]";
    }
}
