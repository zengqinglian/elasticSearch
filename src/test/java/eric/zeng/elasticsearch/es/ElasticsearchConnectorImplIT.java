package eric.zeng.elasticsearch.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.zeng.elasticsearch.es.DocumentAdapter;
import eric.zeng.elasticsearch.es.ElasticsearchConnectorImpl;
import eric.zeng.elasticsearch.es.server.AbstractElasticsearchIntegration;
import eric.zeng.elasticsearch.model.DateFacet;
import eric.zeng.elasticsearch.model.DateResultFacetEntry;
import eric.zeng.elasticsearch.model.Document;
import eric.zeng.elasticsearch.model.RangeFilter;
import eric.zeng.elasticsearch.model.ResultDocument;
import eric.zeng.elasticsearch.model.ResultFacet;
import eric.zeng.elasticsearch.model.SearchQuery;
import eric.zeng.elasticsearch.model.SearchResult;
import eric.zeng.elasticsearch.model.Sort;
import eric.zeng.elasticsearch.model.TermFacet;
import eric.zeng.elasticsearch.model.ValueFilter;

public class ElasticsearchConnectorImplIT extends AbstractElasticsearchIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConnectorImplIT.class);

    private static ElasticsearchConnectorImpl cut;
    private static boolean set = false;

    private static final String INDEX = "test";
    private static final String TYPE = "tests";

    @Test
    public final void indexSimpleDocument() throws Exception {
        Document document = new Document()
                .addField("title", "This is the title")
                .addField("date", "2013-08-02T14:40:11.225Z")
                .addField("text", "This is my neverending text.");

        Document result = cut.index(document);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(document.getFields(), result.getFields());
        verifyDocumentOnIndex(result);
    }

    @Test
    public final void indexDocumentWithArrays() throws Exception {
        Document document = new Document()
                .addField("title", "This is the title")
                .addField("date", "2013-08-02T14:40:11.225Z")
                .addField("tags", Arrays.asList("tags", "are", "cool"))
                .addField("text", "This is my neverending text.");

        Document result = cut.index(document);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(document.getFields(), result.getFields());
        verifyDocumentOnIndex(result);
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
        int size = loadData();
        SearchQuery query = new SearchQuery();
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(size, result.getDocumentsCount());
    }

    @Test
    public final void textSearchForDocuments() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("content of my document");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
    }

    @Test
    public final void textSearchForDocumentsInSpecificFields() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("content of");
        query.addQueryField("title").addQueryField("type");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(0, result.getDocumentsCount());
    }

    @Test
    public final void textWithQuoteSearchForDocuments() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("\"My Second\"");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(1, result.getDocumentsCount());
    }

    @Test
    public final void textWithPartialQuotesSearchForDocuments() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("\"My First\" Document");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
    }

    @Test
    public final void textWithQuotesAndOperatorSearchForDocuments() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("\"My First\" AND Document");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(1, result.getDocumentsCount());
    }

    @Test
    public final void searchForDocumentsAndCheckRelevancy() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery("\"My First\" Document");
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
        Float first = null;
        Float second = null;
        for (ResultDocument document : result.getResultDocuments()) {
            if ("My First Document".equals(document.getDocument().getField("title"))) {
                first = document.getRelevance();
            } else {
                second = document.getRelevance();
            }
        }
        assertNotNull(first);
        assertNotNull(second);
        assertTrue(first.compareTo(second) > 0);
    }

    @Test
    public final void searchWithPagination() throws Exception {
        int size = loadData();
        SearchQuery query = new SearchQuery(2, 0);
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(size, result.getTotalDocumentsCount());
        assertEquals(2, result.getDocumentsCount());

        query = new SearchQuery(2, 2);
        result = cut.search(query);
        assertNotNull(result);
        assertEquals(size, result.getTotalDocumentsCount());
        assertEquals(1, result.getDocumentsCount());
    }

    @Test
    public final void facetedSearchForDocumentsClickingOnTerm() throws Exception {
        int size = loadData();
        SearchQuery query = new SearchQuery();
        query.addFacet(new TermFacet("type", 10));
        query.addFacet(new DateFacet("date", DateFacet.Interval.YEAR));
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(size, result.getDocumentsCount());

        assertEquals(2, result.getResultFacets().size());
        for (ResultFacet<?> rf : result.getResultFacets()) {
            assertEquals(3, rf.getEntries().size());
        }

        // click on a term
        query.addFilter(new ValueFilter<String>("type", "spread"));
        result = cut.search(query);
        assertNotNull(result);
        assertEquals(1, result.getDocumentsCount());
    }

    @Test
    public final void facetedSearchForDocumentsClickingOnDate() throws Exception {
        int size = loadData();
        SearchQuery query = new SearchQuery();
        query.addFacet(new TermFacet("type", 10));
        query.addFacet(new DateFacet("date", DateFacet.Interval.YEAR));
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(size, result.getDocumentsCount());

        assertEquals(2, result.getResultFacets().size());
        DateResultFacetEntry start = null;
        DateResultFacetEntry end = null;
        for (ResultFacet<?> rf : result.getResultFacets()) {
            assertEquals(3, rf.getEntries().size());
            if (rf.getEntries().get(0) instanceof DateResultFacetEntry) {
                start = (DateResultFacetEntry) rf.getEntries().get(1);
                end = (DateResultFacetEntry) rf.getEntries().get(2);
            }
        }
        assertNotNull(start);
        assertNotNull(end);

        // click on a date
        query.addFilter(new RangeFilter<Long>("date", start.getTime(), true, false, end.getTime()));
        result = cut.search(query);
        assertNotNull(result);
        assertEquals(1, result.getDocumentsCount());
    }

    @Test
    public final void searchForDocumentsWithTermFilter() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery().addFilter(new ValueFilter<String>("department", "travel"));
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
    }

    @Test
    public final void searchForDocumentsWithRangeFilter() throws Exception {
        loadData();
        SearchQuery query = new SearchQuery().addFilter(new RangeFilter<Long>("date", 1325376000000L, true));
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
    }

    @Test
    public final void searchForDocumentsWithSorting() throws Exception {
        loadData();

        // test text field
        SearchQuery query = new SearchQuery("content of my document");
        query.addSort(new Sort(Sort.OrderBy.FIELD, "type", Sort.SortOrder.DESC));
        SearchResult result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
        Document document = result.getResultDocuments().get(0).getDocument();
        assertEquals("spread", document.getField("type"));

        // test date field
        query = new SearchQuery("content of my document");
        query.addSort(new Sort(Sort.OrderBy.FIELD, "date", Sort.SortOrder.ASC));
        result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
        DateTimeFormatter df = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
        Date date1 = df.parseDateTime((String) result.getResultDocuments().get(0).getDocument().getField("date"))
                .toDate();
        Date date2 = df.parseDateTime((String) result.getResultDocuments().get(1).getDocument().getField("date"))
                .toDate();
        assertTrue(date1.before(date2));

        // test relevance
        query = new SearchQuery("\"My First\" Document");
        query.addSort(new Sort(Sort.OrderBy.RELEVACE, Sort.SortOrder.DESC));
        result = cut.search(query);
        assertNotNull(result);
        assertEquals(2, result.getDocumentsCount());
        Float relevancy1 = result.getResultDocuments().get(0).getRelevance();
        Float relevancy2 = result.getResultDocuments().get(1).getRelevance();
        assertTrue(relevancy1 > relevancy2);
    }

    @Test
    @Ignore
    public final void searchForDocumentWithStemmingAndStopWords() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    public final void updateTemplate() throws Exception {
        String json = IOUtils.toString(ElasticsearchConnectorImplIT.class
                .getResourceAsStream("template.json"));
        cut.setTemplate(json);
    }

    @Test
    public final void elasticSearchPlayground() throws Exception {
        //objects
        XContentBuilder object1 = XContentFactory.jsonBuilder().startObject()
                .field("title", "Book One")
                .field("postDate", new Date())
                .field("content", "This is my test.")
                .endObject();
        XContentBuilder object2 = XContentFactory.jsonBuilder().startObject()
                .field("title", "Book One")
                .field("postDate", new Date())
                .field("content", "Trying out Elastic Search with a useless content.")
                .endObject();
        XContentBuilder object3 = XContentFactory.jsonBuilder().startObject()
                .field("title", "Book Two")
                .field("postDate", new Date())
                .field("content", "This asset bellongs to a second book.")
                .endObject();

        //responses
        IndexResponse indexResp;
        SearchResponse searchResp;
        DeleteResponse deleteResp;
        SearchHits hits;

        //index
        LOGGER.info("INDEX");
        indexResp = getClient().prepareIndex(INDEX, TYPE).setSource(object1).execute().actionGet();
        LOGGER.info("Indexed: {}", indexResp.getId());
        indexResp = getClient().prepareIndex(INDEX, TYPE).setSource(object2).execute().actionGet();
        LOGGER.info("Indexed: {}", indexResp.getId());
        indexResp = getClient().prepareIndex(INDEX, TYPE).setSource(object3).execute().actionGet();
        LOGGER.info("Indexed: {}", indexResp.getId());
        Thread.sleep(1000);

        //search
        LOGGER.info("SEARCH");
        searchResp = getClient().prepareSearch(INDEX).setTypes(TYPE)
                .setQuery(QueryBuilders.queryString("useless content").field("content")).execute().actionGet();
        hits = searchResp.getHits();
        LOGGER.info("Found {} result(s).", hits.getTotalHits());
        for (int i = 0; i < hits.getTotalHits(); i++) {
            for (Map.Entry<String, Object> field : hits.getAt(i).getSource().entrySet()) {
                LOGGER.info("{}: {}", field.getKey(), field.getValue());
            }
        }

        //faceted search
        LOGGER.info("FACETED SEARCH 1");
        SearchRequestBuilder request = getClient().prepareSearch(INDEX).setTypes(TYPE).addFacet(FacetBuilders
                .termsFacet("facetTest").field("title"));
        searchResp = request.execute().actionGet();
        hits = searchResp.getHits();
        LOGGER.info("Search request: {}", request.toString());
        LOGGER.info("Search result: {}", searchResp.toString());
        TermsFacet facet = (TermsFacet) searchResp.getFacets().facetsAsMap().get("facetTest");
        for (TermsFacet.Entry entry : facet.getEntries()) {
            LOGGER.info("term: {}", entry.getTerm());
            LOGGER.info("term: {}", entry.getCount());
        }

        FilterBuilder filter = FilterBuilders.termFilter("title", "two");
        request = getClient().prepareSearch(INDEX).setTypes(TYPE).addFacet(FacetBuilders.termsFacet("facetTest")
                .field("title").facetFilter(filter)).setPostFilter(filter);
        searchResp = request.execute().actionGet();
        LOGGER.info("Search request after filter: {}", request.toString());
        LOGGER.info("Search result after filter: {}", searchResp.toString());
        facet = (TermsFacet) searchResp.getFacets().facetsAsMap().get("facetTest");
        for (TermsFacet.Entry entry : facet.getEntries()) {
            LOGGER.info("term: {}", entry.getTerm());
            LOGGER.info("term: {}", entry.getCount());
        }

        //remove
        LOGGER.info("REMOVE");
        searchResp = getClient().prepareSearch(INDEX).execute().actionGet();
        hits = searchResp.getHits();
        for (int i = 0; i < hits.getTotalHits(); i++) {
            deleteResp = getClient().prepareDelete(INDEX, TYPE, hits.getAt(i).getId()).execute().actionGet();
            LOGGER.info("Deleted: {}", deleteResp.getId());
        }

        //set template
        PutIndexTemplateRequest template = new PutIndexTemplateRequest("template_1");
        template.source(IOUtils.toString(ElasticsearchConnectorImplIT.class
                .getResourceAsStream("template.json")));
        LOGGER.info("Template inserted: {}", getClient().admin().indices().putTemplate(template).actionGet()
                .isAcknowledged());
    }

    @Before
    public final void setUp() {
        if (!set) {
            cut = new ElasticsearchConnectorImpl();
            cut.setClient(getClient());
            cut.setIndex(INDEX);
            cut.setType(TYPE);
            set = true;
        }

        ClusterHealthRequestBuilder healthRequest = getClient().admin().cluster().prepareHealth();
        ClusterHealthResponse healthResponse = healthRequest.execute().actionGet();
        IndicesAdminClient admin = getClient().admin().indices();
        admin.create(new CreateIndexRequest(INDEX)).actionGet();
        admin.flush(new FlushRequest(INDEX).force(true)).actionGet();

        LOGGER.info("SetUp: ES status: {}", healthResponse.getStatus());
    }

    @After
    public final void tearDown() {
        DeleteIndexResponse delete = getClient().admin().indices().delete(new DeleteIndexRequest(INDEX)).actionGet();
        if (!delete.isAcknowledged()) {
            LOGGER.warn("TearDown: index was not deleted.");
        } else {
            LOGGER.info("TearDown: index deleted.");
        }
    }

    private int loadData() throws Exception {
        String[] docs = {
                "{\"title\":\"My First Document\",\"type\":\"edition\",\"department\":\"travel\"," +
                        "\"date\":\"2013-08-01T16:41:46.200Z\",\"content\":\"This is the content of my document.\"}",
                "{\"title\":\"My Second Document\",\"type\":\"spread\",\"department\":\"life\"," +
                        "\"date\":\"2011-08-01T16:41:46.200Z\",\"content\":\"This also is content of my document.\"}",
                "{\"title\":\"The Third\",\"type\":\"asset\",\"department\":\"travel\"," +
                        "\"date\":\"2012-08-01T16:41:46.200Z\",\"content\":\"And also this.\"}"
        };
        for (int i = 0; i < docs.length; i++) {
            getClient().prepareIndex(INDEX, TYPE).setSource(docs[i]).setRefresh(true).execute().actionGet();
        }
        getClient().admin().indices().flush(new FlushRequest(INDEX).force(true)).actionGet();
        return docs.length;
    }

    private void verifyDocumentOnIndex(Document document) throws Exception {
        GetResponse response = getClient().prepareGet(INDEX, TYPE, document.getId()).execute().actionGet();
        assertTrue(DocumentAdapter.documentToMap(document).entrySet().containsAll(response.getSourceAsMap().entrySet()));
    }
}
