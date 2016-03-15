package eric.zeng.elasticsearch.es.server;

import org.elasticsearch.client.Client;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractElasticsearchIntegration {
    private static EmbeddedElasticsearchServer embeddedElasticsearchServer;
    private static Client esClient;

    @BeforeClass
    public static void startEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer = new EmbeddedElasticsearchServer();
        esClient = embeddedElasticsearchServer.getClient();
    }

    @AfterClass
    public static void shutdownEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer.shutdown();
    }

    protected Client getClient() {
        return esClient;
    }
}
