package eric.zeng.elasticsearch.es;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.zeng.elasticsearch.SearchConnector;
import eric.zeng.elasticsearch.SearchException;
import eric.zeng.elasticsearch.model.Document;
import eric.zeng.elasticsearch.model.SearchQuery;
import eric.zeng.elasticsearch.model.SearchResult;

public class ElasticsearchConnectorImpl implements SearchConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConnectorImpl.class);

    private Client client;
    private String index;
    private String type;

    public SearchResult search(final SearchQuery searchQuery) throws SearchException{
    	if (searchQuery == null) {
            throw new IllegalArgumentException("A SearchQuery must be provided.");
        }

        SearchResponse response = SearchQueryAdapter.searchQueryToSearchRequestBuilder(searchQuery,
                client, index, type).execute().actionGet();
        LOGGER.debug("ES Response: {}", response.toString());

        return SearchResultAdapter.searchResponseToSearchResult(response);
    }
   

    
    public Document index(final Document document) throws SearchException {
        if (document == null) {
            throw new IllegalArgumentException("A Document must be provided.");
        }

        LOGGER.debug("Indexing {}", document.toString());

        String id = document.getId();
        IndexRequestBuilder request = null;
        IndexResponse response = null;

        if (id == null) {
            request = client.prepareIndex(index, type);
        } else {
            request = client.prepareIndex(index, type, id);
        }

        response = request.setSource(DocumentAdapter.documentToMap(document)).setRefresh(true).execute().actionGet();

        //TODO investigate if this is the best acknowledgement approach
        FlushResponse flush = client.admin().indices().flush(new FlushRequest(index).force(true)).actionGet();
        if (flush.getSuccessfulShards() < 1) {
            String msg = String.format("Index fail in %s shards.", flush.getFailedShards());
            LOGGER.error(msg);
            throw new SearchException(msg);
        }

        LOGGER.debug("With id: {}", response.getId());

        return new Document(response.getId(), document);
    }

    
    public void setTemplate(String json) throws SearchException {
        PutIndexTemplateRequest template = new PutIndexTemplateRequest("template_1");
        template.source(json);
        try {
            LOGGER.debug("Template inserted: {}", client.admin().indices().putTemplate(template).actionGet()
                    .isAcknowledged());
        } catch (ElasticsearchException e) {
            String msg = "Error inserting template.";
            LOGGER.error(msg);
            throw new SearchException(msg, e);
        }
    }

    /**
     * @param client the client to set.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @param index the index to set.
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * @param type the type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
