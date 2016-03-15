
package eric.zeng.elasticsearch;

import eric.zeng.elasticsearch.model.Document;
import eric.zeng.elasticsearch.model.SearchQuery;
import eric.zeng.elasticsearch.model.SearchResult;


public interface SearchConnector {
    /**
     * Search for documents based on the search query object.
     * @param searchQuery the search query parameters.
     * @return the search result.
     * @throws SearchException if an error occurs.
     */
    SearchResult search(final SearchQuery searchQuery) throws SearchException;

    /**
     * Index a new document.
     * @param document the document to be indexed.
     * @return the indexed document.
     * @throws SearchException if an error occurs.
     */
    Document index(final Document document) throws SearchException;

    /**
     * Set the template for the index.
     * @param json the settings.
     * @throws SearchException if an error occurs.
     */
    void setTemplate(String json) throws SearchException;

    //void bulkIndex(List<Document> documents) throws SearchException;

    //Document replace(Document document) throws SearchException;
}
