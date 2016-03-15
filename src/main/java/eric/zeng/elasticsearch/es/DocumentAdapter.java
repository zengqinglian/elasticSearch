package eric.zeng.elasticsearch.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.zeng.elasticsearch.SearchException;
import eric.zeng.elasticsearch.model.Document;

public final class DocumentAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAdapter.class);

    private DocumentAdapter() {
        super();
    }

    /**
     * @param document the document.
     * @return a map representing the fields of the document.
     * @throws SearchException if some error occurs.
     */
    public static final Map<String, Object> documentToMap(final Document document) throws SearchException {
        Map<String, Object> fields = new HashMap<String, Object>();
        LOGGER.debug("Adapting: {}", document.toString());

        for (Map.Entry<String, Object> field : document.getFields().entrySet()) {
            fields.put(field.getKey(), field.getValue());
        }

        LOGGER.debug("To map: {}", fields.toString());
        return fields;
    }

    /**
     * @param document the document.
     * @return JSON object representing the fields of the document.
     * @throws SearchException if some error occurs.
     */
    public static final String documentToJson(final Document document) throws SearchException {
        LOGGER.debug("Adapting: {}", document.toString());

        try {
            XContentBuilder json = jsonBuilder().startObject();

            for (Map.Entry<String, Object> field : document.getFields().entrySet()) {
                json.field(field.getKey(), field.getValue());
            }
            json.endObject();

            String jsonString = json.string();
            LOGGER.debug("To JSON: {}", jsonString);
            return jsonString;

        } catch (IOException e) {
            String msg = "Error creating JSON string from DK Document.";
            LOGGER.error(msg, e);
            throw new SearchException(msg, e);
        }
    }
}
