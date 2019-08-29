package com.farooq.springboot.cosmos.config;

import com.microsoft.azure.documentdb.*;
import com.microsoft.azure.documentdb.bulkexecutor.BulkImportResponse;
import com.microsoft.azure.documentdb.bulkexecutor.DocumentBulkExecutor;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConsoleItemWriter<T> implements ItemWriter<T> {


    private String serviceEndpoint = "";

    private String masterKey = "";

    private String databaseId = "";

    private String collectionId ="";

    private String operation = "Import";


    private boolean shouldCreateCollection = false;


    private int collectionThroughput = 1000000;


    private String partitionKey = "/partitionKey";

    private int maxConnectionPoolSize = 1000;

    private ConsistencyLevel consistencyLevel = ConsistencyLevel.Session;

    private ConnectionMode connectionMode = ConnectionMode.Gateway;

    private int numberOfDocumentsForEachCheckpoint = 500000;

    private int numberOfCheckpoints = 10;

    private boolean help = false;



    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            System.out.println(item);
        }

        ConnectionPolicy policy = new ConnectionPolicy();
        RetryOptions retryOptions = new RetryOptions();
        retryOptions.setMaxRetryAttemptsOnThrottledRequests(0);
        policy.setRetryOptions(retryOptions);
        policy.setConnectionMode(connectionMode);
        policy.setMaxPoolSize(10);

        DocumentClient client = new DocumentClient(serviceEndpoint, masterKey, policy, ConsistencyLevel.Session) ;
        //Utilities.documentClientFrom(connectionMode ,10 , "service url",  "master" );

        String collectionLink = String.format("/dbs/%s/colls/%s", databaseId, collectionId);
        DocumentCollection collection =  client.readCollection(collectionLink, null).getResource();


        int offerThroughput = Utilities.getOfferThroughput(client, collection);

        DocumentBulkExecutor.Builder bulkExecutorBuilder = DocumentBulkExecutor.builder().from(client, databaseId,
                "collection_name", collection.getPartitionKey(), offerThroughput);
        DocumentBulkExecutor bulkExecutor = bulkExecutorBuilder.build();

        Collection<String> documents = new ArrayList<>(Arrays.asList("{\n" +
                "  \"id\" : \"333333\"\n" +
                "  \"name\": \"Hello World\"\n" +
                "}"));



        BulkImportResponse bulkImportResponse = bulkExecutor.importAll(documents, false, true, null);


    }


}
