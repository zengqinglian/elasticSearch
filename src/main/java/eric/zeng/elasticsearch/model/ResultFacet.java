
package eric.zeng.elasticsearch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultFacet<T extends ResultFacetEntry> {
    private final String facetName;
    private final List<T> entries;

    /**
     * @param facetName
     */
    public ResultFacet(String facetName) {
        super();
        this.facetName = facetName;
        this.entries = new ArrayList<T>();
    }

    /**
     * @return the facetName.
     */
    public String getFacetName() {
        return facetName;
    }

    /**
     * @return the entries.
     */
    public List<T> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * @param entry the entry.
     */
    public void addEntry(T entry) {
        entries.add(entry);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
        result = prime * result + ((facetName == null) ? 0 : facetName.hashCode());
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
        ResultFacet<?> other = (ResultFacet<?>) obj;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        if (facetName == null) {
            if (other.facetName != null)
                return false;
        } else if (!facetName.equals(other.facetName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ResultFacet [facetName=" + facetName + ", entries=" + entries + "]";
    }
}
