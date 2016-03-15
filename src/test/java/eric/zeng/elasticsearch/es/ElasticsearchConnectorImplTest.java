
package eric.zeng.elasticsearch.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eric.zeng.elasticsearch.es.ElasticsearchConnectorImpl;
import eric.zeng.elasticsearch.model.Document;
import eric.zeng.elasticsearch.model.ResultDocument;
import eric.zeng.elasticsearch.model.SearchQuery;
import eric.zeng.elasticsearch.model.SearchResult;

public class ElasticsearchConnectorImplTest {
    private ElasticsearchConnectorImpl cut;

    @Mock
    private Client client;
    @Mock
    private IndexRequestBuilder indexRequestBuilder;
    @Mock
    private ListenableActionFuture<IndexResponse> listenableActionFuture_IndexResponse;
    @Mock
    private IndexResponse indexResponse;
    @Mock
    private AdminClient admimClient;
    @Mock
    private IndicesAdminClient indicesAdminClient;
    @Mock
    private ActionFuture<FlushResponse> actionFuture_FlushResponse;
    @Mock
    FlushResponse flushResponse;
    @Mock
    private SearchRequestBuilder searchRequestBuilder;
    @Mock
    private ListenableActionFuture<SearchResponse> listenableActionFuture_SearchResponse;
    @Mock
    private SearchResponse searchResponse;
    @Mock
    private SearchHits searchHits;
    @Mock
    private SearchHit searchHit1;
    @Mock
    private SearchHit searchHit2;
    @Mock
    private SearchHit searchHit3;

    private static final String INDEX = "test";
    private static final String TYPE = "tests";
    private static final String FAKE_ID = "1234";
    private static final Date now = new Date();
    private static final Map<String, Object> simpleFields;
    static {
        simpleFields = new HashMap<String, Object>();
        simpleFields.put("title", "This is the title");
        simpleFields.put("date", now);
        simpleFields.put("text", "This is my neverending text.");
    }
    private static final Document simpleDocument = new Document(simpleFields);
    private static final Map<String, Object> fieldsWithArray;
    static {
        fieldsWithArray = new HashMap<String, Object>(simpleFields);
        fieldsWithArray.put("tags", new String[] { "tag1", "tag2", "tag3" });
    }
    private static final Document documentWithArray = new Document(fieldsWithArray);

    @Before
    public final void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cut = new ElasticsearchConnectorImpl();
        cut.setClient(client);
        cut.setIndex(INDEX);
        cut.setType(TYPE);

        // for index tests
        when(client.prepareIndex(INDEX, TYPE)).thenReturn(indexRequestBuilder);
        when(indexRequestBuilder.setSource(simpleFields)).thenReturn(indexRequestBuilder);
        when(indexRequestBuilder.setSource(fieldsWithArray)).thenReturn(indexRequestBuilder);
        when(indexRequestBuilder.setRefresh(true)).thenReturn(indexRequestBuilder);
        when(indexRequestBuilder.execute()).thenReturn(listenableActionFuture_IndexResponse);
        when(listenableActionFuture_IndexResponse.actionGet()).thenReturn(indexResponse);
        when(client.admin()).thenReturn(admimClient);
        when(admimClient.indices()).thenReturn(indicesAdminClient);
        when(indicesAdminClient.flush((FlushRequest) anyObject())).thenReturn(actionFuture_FlushResponse);
        when(actionFuture_FlushResponse.actionGet()).thenReturn(flushResponse);
        when(flushResponse.getSuccessfulShards()).thenReturn(1);
        when(indexResponse.getId()).thenReturn(FAKE_ID);

        // for search tests
        when(client.prepareSearch(INDEX)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setTypes(TYPE)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.execute()).thenReturn(listenableActionFuture_SearchResponse);
        when(listenableActionFuture_SearchResponse.actionGet()).thenReturn(searchResponse);
        when(searchResponse.getHits()).thenReturn(searchHits);
        when(searchHits.getTotalHits()).thenReturn(3L);
        when(searchHits.getHits()).thenReturn(new SearchHit[] { searchHit1, searchHit2, searchHit3 });
        when(searchHit1.getId()).thenReturn("123");
        when(searchHit2.getId()).thenReturn("321");
        when(searchHit3.getId()).thenReturn("666");
        when(searchHit1.getScore()).thenReturn(1.0f);
        when(searchHit2.getScore()).thenReturn(0.77f);
        when(searchHit3.getScore()).thenReturn(0.444f);
        //TODO finish to test the fields...

    }

    @After
    public final void tearDown() throws Exception {
    }

    @Test
    public final void indexSimpleDocument() throws Exception {
        Document expected = new Document(FAKE_ID, simpleDocument);
        Document result = cut.index(simpleDocument);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expected, result);
        assertEquals(FAKE_ID, result.getId());
    }

    @Test
    public final void indexDocumentWithArrays() throws Exception {
        Document expected = new Document(FAKE_ID, documentWithArray);
        Document result = cut.index(documentWithArray);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expected, result);
        assertEquals(FAKE_ID, result.getId());
    }

    @Test
    @Ignore
    public final void bulkIndexDocuments() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void replaceADocument() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    public final void searchAllDocuments() throws Exception {
        SearchQuery query = new SearchQuery();
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getTotalDocumentsCount());
        assertTrue(result.getResultFacets().size() == 0);
    }

    @Test
    public final void textSearchForDocuments() throws Exception {
        SearchQuery query = new SearchQuery("this is a test");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());
    }

    @Test
    public final void textSearchForDocumentsInSpecificFields() throws Exception {
        SearchQuery query = new SearchQuery("this is a test");
        query.addQueryField("title").addQueryField("text");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());
    }

    @Test
    public final void textWithQuoteSearchForDocuments() throws Exception {
        SearchQuery query = new SearchQuery("\"this is a test\"");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());
    }

    @Test
    public final void textWithPartialQuotesSearchForDocuments() throws Exception {
        SearchQuery query = new SearchQuery("\"this is a\" test");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());
    }

    @Test
    public final void textWithQuotesAndOperatorSearchForDocuments() throws Exception {
        SearchQuery query = new SearchQuery("\"this is a test\" AND not a test");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());
    }

    @Test
    public final void searchForDocumentsAndCheckRelevancy() throws Exception {
        SearchQuery query = new SearchQuery("this is a test");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(3, result.getDocumentsCount());

        for (ResultDocument document : result.getResultDocuments()) {
            if ("123".equals(document.getDocument().getId())) {
                assertEquals(new Float(1.0f), document.getRelevance());
            } else if ("321".equals(document.getDocument().getId())) {
                assertEquals(new Float(0.77f), document.getRelevance());
            } else if ("666".equals(document.getDocument().getId())) {
                assertEquals(new Float(0.444f), document.getRelevance());
            } else {
                fail("Document not included.");
            }
        }
    }

    @Test
    @Ignore
    public final void searchWithPagination() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void facetedSearchForDocumentsClickingOnTerm() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void facetedSearchForDocumentsClickingOnDate() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void searchForDocumentsWithTermFilter() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void searchForDocumentsWithRangeFilter() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void searchForDocumentsWithSorting() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public final void searchForDocumentWithStemmingAndStopWords() throws Exception {
        fail("Not yet implemented");
    }
}
