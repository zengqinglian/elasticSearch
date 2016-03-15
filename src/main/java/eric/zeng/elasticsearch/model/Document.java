
package eric.zeng.elasticsearch.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class Document {
    private final String id;
    private final Map<String, Object> fields;

    /**
     * Creates a document.
     */
    public Document() {
        super();
        id = null;
        fields = new LinkedHashMap<String, Object>();
    }

    /**
     * Creates a document.
     * @param id the id.
     */
    public Document(String id) {
        super();
        this.id = id;
        fields = new LinkedHashMap<String, Object>();
    }

    /**
     * Creates a document.
     * @param fields the fields.
     */
    public Document(Map<String, Object> fields) {
        super();
        id = null;
        this.fields = new LinkedHashMap<String, Object>(fields);
    }

    /**
     * Creates a document.
     * @param id the id.
     * @param fields the fields.
     */
    public Document(String id, Map<String, Object> fields) {
        super();
        this.id = id;
        this.fields = new LinkedHashMap<String, Object>(fields);
    }

    /**
     * Creates a new document from other document.
     * @param id the new id.
     * @param document the old document.
     */
    public Document(String id, Document document) {
        super();
        this.id = id;
        fields = new LinkedHashMap<String, Object>(document.getFields());
    }

    /**
     * @return the id.
     */
    public String getId() {
        return id;
    }

    public Object getField(String name) {
        return fields.get(name);
    }

    /**
     * @return the fields.
     */
    public Map<String, Object> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    /**
     * Adds a new field and returns the current document.
     * @param field the new field.
     * @return the current document.
     */
    public Document addField(String name, Object value) {
        fields.put(name, value);
        return this;
    }

    /**
     * Removes a field by its field name.
     * @param fieldName the name of the field to be removed.
     * @return <true> if removed or <false> if was not found.
     */
    public boolean removeField(String name) {
        if (fields.containsKey(name)) {
            fields.remove(name);
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fields == null) ? 0 : fields.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Document other = (Document) obj;
        if (fields == null) {
            if (other.fields != null)
                return false;
        } else if (!fields.equals(other.fields))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Document [id=" + id + ", fields=" + fields + "]";
    }
}
