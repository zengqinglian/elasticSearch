package eric.zeng.elasticsearch.es;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.zeng.elasticsearch.SearchException;
import eric.zeng.elasticsearch.model.DateResultFacetEntry;
import eric.zeng.elasticsearch.model.Document;
import eric.zeng.elasticsearch.model.ResultDocument;
import eric.zeng.elasticsearch.model.ResultFacet;
import eric.zeng.elasticsearch.model.SearchResult;
import eric.zeng.elasticsearch.model.TermResultFacetEntry;

public final class SearchResultAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultAdapter.class);

    private SearchResultAdapter() {
        super();
    }

    /**
     * @param searchResponse
     * @return
     * @throws SearchException
     */
    public static final SearchResult searchResponseToSearchResult(final SearchResponse searchResponse)
            throws SearchException {
        SearchHits searchHits = searchResponse.getHits();
        SearchResult result = new SearchResult(searchHits.getTotalHits());
        LOGGER.debug("Adapting: SearchResponse {}", searchResponse.toString());

        // Hits
        SearchHit[] hits = searchHits.getHits();
        for (int i = 0; i < hits.length; i++) {
            Document document = new Document(hits[i].getId());
            for (Map.Entry<String, Object> entry : hits[i].getSource().entrySet()) {
                document.addField(entry.getKey(), entry.getValue());
            }
            result.addResultDocument(new ResultDocument(hits[i].getScore(), document));
        }

        // Facets
        Facets facets = searchResponse.getFacets();
        if (facets != null) {
            for (Facet facet : facets) {
                if (facet instanceof TermsFacet) {
                    ResultFacet<TermResultFacetEntry> rf = new ResultFacet<TermResultFacetEntry>(facet.getName());
                    for (org.elasticsearch.search.facet.terms.TermsFacet.Entry entry : ((TermsFacet) facet)
                            .getEntries()) {
                        rf.addEntry(new TermResultFacetEntry(entry.getCount(), entry.getTerm().toString()));
                    }
                    result.addResultFacet(rf);

                } else if (facet instanceof DateHistogramFacet) {
                    ResultFacet<DateResultFacetEntry> rf = new ResultFacet<DateResultFacetEntry>(facet.getName());
                    for (org.elasticsearch.search.facet.datehistogram.DateHistogramFacet.Entry entry : ((DateHistogramFacet) facet)
                            .getEntries()) {
                        rf.addEntry(new DateResultFacetEntry(entry.getCount(), entry.getTime()));
                    }
                    result.addResultFacet(rf);

                } else {
                    throw new IllegalArgumentException(String.format("%s not suported.", facet.getClass()
                            .getSimpleName()));
                }
            }
        }

        LOGGER.debug("To: {}", result.toString());
        return result;
    }
}
