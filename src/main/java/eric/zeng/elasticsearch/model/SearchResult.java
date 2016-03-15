package eric.zeng.elasticsearch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchResult implements Result {
    private final long totalDocumentsCount;
    private final List<ResultDocument> resultDocuments;
    private final List<ResultFacet<?>> resultFacets;

    /**
     * Creates an empty search result.
     * @param totalDocumentsCount total documents count.
     */
    public SearchResult(long totalDocumentsCount) {
        super();
        this.totalDocumentsCount = totalDocumentsCount;
        resultDocuments = new ArrayList<ResultDocument>();
        resultFacets = new ArrayList<ResultFacet<?>>();
    }

    /**
     * @param resultDocument the result document to add.
     * @return the updated search response.
     */
    public SearchResult addResultDocument(ResultDocument resultDocument) {
        resultDocuments.add(resultDocument);
        return this;
    }

    /**
     * @return the totalDocumentsCount.
     */
    public long getTotalDocumentsCount() {
        return totalDocumentsCount;
    }

    /**
     * @return the count of documents.
     */
    public int getDocumentsCount() {
        return resultDocuments.size();
    }

    /**
     * @return the result documents.
     */
    public List<ResultDocument> getResultDocuments() {
        return Collections.unmodifiableList(resultDocuments);
    }

    /**
     * @return the resultFacets.
     */
    public List<ResultFacet<?>> getResultFacets() {
        return resultFacets;
    }

    /**
     * @param resultFacet the resultFacet.
     * @return the updated search response.
     */
    public SearchResult addResultFacet(ResultFacet<?> resultFacet) {
        resultFacets.add(resultFacet);
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resultDocuments == null) ? 0 : resultDocuments.hashCode());
        result = prime * result + ((resultFacets == null) ? 0 : resultFacets.hashCode());
        result = prime * result + (int) (totalDocumentsCount ^ (totalDocumentsCount >>> 32));
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
        SearchResult other = (SearchResult) obj;
        if (resultDocuments == null) {
            if (other.resultDocuments != null)
                return false;
        } else if (!resultDocuments.equals(other.resultDocuments))
            return false;
        if (resultFacets == null) {
            if (other.resultFacets != null)
                return false;
        } else if (!resultFacets.equals(other.resultFacets))
            return false;
        if (totalDocumentsCount != other.totalDocumentsCount)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SearchResult [totalDocumentsCount=" + totalDocumentsCount + ", resultDocuments=" + resultDocuments
                + ", resultFacets=" + resultFacets + "]";
    }
}
