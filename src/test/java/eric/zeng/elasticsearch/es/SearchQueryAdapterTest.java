package eric.zeng.elasticsearch.es;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eric.zeng.elasticsearch.es.SearchQueryAdapter;
import eric.zeng.elasticsearch.model.SearchQuery;

public class SearchQueryAdapterTest {
    @Mock
    private Client client;
    @Mock
    private SearchRequestBuilder searchRequestBuilder;

    private static final String INDEX = "test";
    private static final String TYPE = "tests";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(client.prepareSearch(INDEX)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setTypes(TYPE)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.toString()).thenReturn("I'm just a mock");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void adaptSearchQueryToSearchRequestBuilder() {
        SearchQuery provided = new SearchQuery();

        SearchRequestBuilder expected = client.prepareSearch(INDEX).setTypes(TYPE);

        assertEquals(expected, SearchQueryAdapter.searchQueryToSearchRequestBuilder(provided, client, INDEX, TYPE));
    }
}
