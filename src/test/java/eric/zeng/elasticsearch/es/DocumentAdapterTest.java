package eric.zeng.elasticsearch.es;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eric.zeng.elasticsearch.es.DocumentAdapter;
import eric.zeng.elasticsearch.model.Document;

public class DocumentAdapterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void adaptDocumentToJson() throws Exception {
        Date now = new Date();
        DateTimeFormatter df = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

        Document provided = new Document()
                .addField("title", "This is the title")
                .addField("date", now)
                .addField("text", "This is my neverending text.");

        String expected = String.format("{\"title\":\"This is the title\",\"date\":\"%s%s",
                df.print(now.getTime()),
                "\",\"text\":\"This is my neverending text.\"}");

        assertEquals(expected, DocumentAdapter.documentToJson(provided));
    }

    @Test
    public void adaptDocumentToMap() throws Exception {
        Date now = new Date();

        Document provided = new Document()
                .addField("title", "This is the title")
                .addField("date", now)
                .addField("text", "This is my neverending text.");

        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("title", "This is the title");
        expected.put("date", now);
        expected.put("text", "This is my neverending text.");

        assertEquals(expected, DocumentAdapter.documentToMap(provided));
    }
}
