package eric.zeng.elasticsearch.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.zeng.elasticsearch.model.DateFacet;
import eric.zeng.elasticsearch.model.Facet;
import eric.zeng.elasticsearch.model.Filter;
import eric.zeng.elasticsearch.model.RangeFilter;
import eric.zeng.elasticsearch.model.SearchQuery;
import eric.zeng.elasticsearch.model.Sort;
import eric.zeng.elasticsearch.model.TermFacet;
import eric.zeng.elasticsearch.model.ValueFilter;

public final class SearchQueryAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchQueryAdapter.class);

    private SearchQueryAdapter() {
        super();
    }

    /**
     * @param searchQuery
     * @param client
     * @param index
     * @param type
     * @return
     */
    public static final SearchRequestBuilder searchQueryToSearchRequestBuilder(SearchQuery searchQuery,
            Client client, String index, String type) {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        LOGGER.debug("Adapting: {}", searchQuery.toString());

        // Pagination
        Integer from = searchQuery.getPageStart();
        Integer size = searchQuery.getPageSize();
        if (from != null && size != null) {
            builder.setFrom(from);
            builder.setSize(size);
        }

        // Text query
        String queryText = searchQuery.getQueryText();
        if (queryText != null && !"".equals(queryText)) {
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(queryText);
            for (String queryField : searchQuery.getQueryFields()) {
                queryStringQueryBuilder.field(queryField);
            }
            builder.setQuery(queryStringQueryBuilder);
        }

        // Filters
        List<FilterBuilder> filters = new ArrayList<FilterBuilder>();

        for (Filter filter : searchQuery.getFilters()) {
            FilterBuilder filterBuilder;

            if (filter instanceof ValueFilter<?>) {
                // Value filters
                ValueFilter<?> valueFilter = (ValueFilter<?>) filter;
                filterBuilder = FilterBuilders.termFilter(valueFilter.getFieldName(), valueFilter.getValue());

            } else if (filter instanceof RangeFilter<?>) {
                //Range filters
                RangeFilter<?> rangeFilter = (RangeFilter<?>) filter;
                filterBuilder = FilterBuilders.rangeFilter(rangeFilter.getFieldName());

                Object start = rangeFilter.getStart();
                Object end = rangeFilter.getEnd();
                if (start != null && end != null) {
                    ((RangeFilterBuilder) filterBuilder).from(start).to(end);
                    ((RangeFilterBuilder) filterBuilder).includeLower(rangeFilter.isIncludeLower()).includeUpper(
                            rangeFilter.isIncludeUpper());

                } else if (start != null) {
                    if (rangeFilter.isIncludeLower()) {
                        ((RangeFilterBuilder) filterBuilder).gte(start);
                    } else {
                        ((RangeFilterBuilder) filterBuilder).gt(start);
                    }

                } else if (end != null) {
                    if (rangeFilter.isIncludeUpper()) {
                        ((RangeFilterBuilder) filterBuilder).lte(end);
                    } else {
                        ((RangeFilterBuilder) filterBuilder).lt(end);
                    }

                } else {
                    throw new IllegalArgumentException("At least start or end should be specified.");
                }

            } else {
                throw new UnsupportedOperationException(String.format("Suport for %d not implemented.",
                        filter.getClass().getSimpleName()));
            }

            filters.add(filterBuilder);
            builder.setPostFilter(filterBuilder);
        }

        // Facets
        for (Facet facet : searchQuery.getFacets()) {
            FacetBuilder facetBuilder = null;
            if (facet instanceof TermFacet) {
                TermFacet termFacet = (TermFacet) facet;
                String name = termFacet.getFieldName();
                TermsFacetBuilder fb = FacetBuilders.termsFacet(name);
                fb.field(name).size(termFacet.getSize());
                facetBuilder = fb;

            } else if (facet instanceof DateFacet) {
                DateFacet dateFacet = (DateFacet) facet;
                DateHistogramFacetBuilder fb = FacetBuilders.dateHistogramFacet(dateFacet.getFieldName());
                fb.field(dateFacet.getFieldName()).interval(dateFacet.getInterval().getIntervalString());
                facetBuilder = fb;

            } else {
                throw new IllegalArgumentException(String.format("%s not suported.", facet.getClass().getSimpleName()));
            }

            for (FilterBuilder filterBuilder : filters) {
                facetBuilder.facetFilter(filterBuilder);
            }
            builder.addFacet(facetBuilder);
        }

        // Sorting
        for (Sort sortFilter : searchQuery.getSortFilters()) {
            if (Sort.OrderBy.FIELD.equals(sortFilter.getOrderBy())) {
                builder.addSort(SortBuilders.fieldSort(sortFilter.getFieldName()).order(
                        SortOrder.valueOf(sortFilter.getSortOrder())));
            } else {
                builder.addSort(SortBuilders.scoreSort().order(SortOrder.valueOf(sortFilter.getSortOrder())));
            }
        }

        LOGGER.debug("To SearchRequestBuilder: {}", builder.toString());
        return builder;
    }
}
