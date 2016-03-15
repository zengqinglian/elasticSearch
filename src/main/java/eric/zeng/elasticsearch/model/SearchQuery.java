package eric.zeng.elasticsearch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchQuery implements Query {
    private String queryText;
    private final List<String> queryFields;

    private final List<Filter> filters;
    private final List<Sort> sorts;
    private final List<Facet> facets;

    private final Integer pageSize;
    private final Integer pageStart;

    /**
     * Creates an empty search query.
     */
    public SearchQuery() {
        this(null, null, null);
    }

    /**
     * Creates an empty search query.
     * @param queryText the query text.
     */
    public SearchQuery(String queryText) {
        this(queryText, null, null);
    }

    /**
     * Creates an empty search query.
     * @param pageSize the number of items per page.
     * @param pageStart the index of the first item.
     */
    public SearchQuery(Integer pageSize, Integer pageStart) {
        this(null, pageSize, pageStart);
    }

    /**
     * Creates an empty search query.
     * @param queryText the query text.
     * @param pageSize the number of items per page.
     * @param pageStart the index of the first item.
     */
    public SearchQuery(String queryText, Integer pageSize, Integer pageStart) {
        super();
        queryFields = new ArrayList<String>();
        filters = new ArrayList<Filter>();
        sorts = new ArrayList<Sort>();
        facets = new ArrayList<Facet>();
        this.queryText = queryText;
        this.pageSize = pageSize;
        this.pageStart = pageStart;
    }

    /**
     * @param queryText the queryText to set.
     * @return the updated search query.
     */
    public SearchQuery setQueryText(String queryText) {
        this.queryText = queryText;
        return this;
    }

    /**
     * @return the queryText.
     */
    public String getQueryText() {
        return queryText;
    }

    /**
     * @param fieldName the field name to add to the query.
     * @return the updated search query.
     */
    public SearchQuery addQueryField(String fieldName) {
        queryFields.add(fieldName);
        return this;
    }

    /**
     * @return the queryFields.
     */
    public List<String> getQueryFields() {
        return Collections.unmodifiableList(queryFields);
    }

    /**
     * @param filter the filter.
     * @return the updated search query.
     */
    public SearchQuery addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    /**
     * @return the filters.
     */
    public List<Filter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * @param sort the sort.
     * @return the updated search query.
     */
    public SearchQuery addSort(Sort sort) {
        sorts.add(sort);
        return this;
    }

    /**
     * @return the sortFilters.
     */
    public List<Sort> getSortFilters() {
        return Collections.unmodifiableList(sorts);
    }

    /**
     * @return the facets.
     */
    public List<Facet> getFacets() {
        return Collections.unmodifiableList(facets);
    }

    /**
     * @param facet the facet.
     * @return the updated search query.
     */
    public SearchQuery addFacet(Facet facet) {
        facets.add(facet);
        return this;
    }

    /**
     * @return the pageSize.
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @return the pageStart.
     */
    public Integer getPageStart() {
        return pageStart;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((facets == null) ? 0 : facets.hashCode());
        result = prime * result + ((filters == null) ? 0 : filters.hashCode());
        result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
        result = prime * result + ((pageStart == null) ? 0 : pageStart.hashCode());
        result = prime * result + ((queryFields == null) ? 0 : queryFields.hashCode());
        result = prime * result + ((queryText == null) ? 0 : queryText.hashCode());
        result = prime * result + ((sorts == null) ? 0 : sorts.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SearchQuery other = (SearchQuery) obj;
        if (facets == null) {
            if (other.facets != null) {
                return false;
            }
        } else if (!facets.equals(other.facets)) {
            return false;
        }
        if (filters == null) {
            if (other.filters != null) {
                return false;
            }
        } else if (!filters.equals(other.filters)) {
            return false;
        }
        if (pageSize == null) {
            if (other.pageSize != null) {
                return false;
            }
        } else if (!pageSize.equals(other.pageSize)) {
            return false;
        }
        if (pageStart == null) {
            if (other.pageStart != null) {
                return false;
            }
        } else if (!pageStart.equals(other.pageStart)) {
            return false;
        }
        if (queryFields == null) {
            if (other.queryFields != null) {
                return false;
            }
        } else if (!queryFields.equals(other.queryFields)) {
            return false;
        }
        if (queryText == null) {
            if (other.queryText != null) {
                return false;
            }
        } else if (!queryText.equals(other.queryText)) {
            return false;
        }
        if (sorts == null) {
            if (other.sorts != null) {
                return false;
            }
        } else if (!sorts.equals(other.sorts)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SearchQuery [queryText=" + queryText + ", queryFields=" + queryFields + ", filters=" + filters
                + ", sorts=" + sorts + ", facets=" + facets + ", pageSize=" + pageSize + ", pageStart=" + pageStart
                + "]";
    }
}
