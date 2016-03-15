
package eric.zeng.elasticsearch.model;


public class ResultDocument {
    private final Float relevance;
    private final Document document;

    /**
     * @param relevance the relevance.
     * @param document the document.
     */
    public ResultDocument(Float relevance, Document document) {
        super();
        this.relevance = relevance;
        this.document = document;
    }

    /**
     * @return the relevance.
     */
    public Float getRelevance() {
        return relevance;
    }

    /**
     * @return the document.
     */
    public Document getDocument() {
        return document;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((document == null) ? 0 : document.hashCode());
        result = prime * result + ((relevance == null) ? 0 : relevance.hashCode());
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
        ResultDocument other = (ResultDocument) obj;
        if (document == null) {
            if (other.document != null)
                return false;
        } else if (!document.equals(other.document))
            return false;
        if (relevance == null) {
            if (other.relevance != null)
                return false;
        } else if (!relevance.equals(other.relevance))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ResultDocument [relevance=" + relevance + ", document=" + document + "]";
    }
}
