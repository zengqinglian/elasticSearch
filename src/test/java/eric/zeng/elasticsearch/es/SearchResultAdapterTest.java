package eric.zeng.elasticsearch.es;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eric.zeng.elasticsearch.es.SearchResultAdapter;

public class SearchResultAdapterTest {
    @Mock
    private SearchResponse searchResponse;
    @Mock
    private SearchHits searchHits;
    @Mock
    private SearchHit searchHit;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(searchResponse.getHits()).thenReturn(searchHits);
        when(searchHits.getHits()).thenReturn(new SearchHit[] { searchHit });
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void adaptSearchResponseToSearchResult() throws Exception {
        assertNotNull(SearchResultAdapter.searchResponseToSearchResult(searchResponse));
    }
}
