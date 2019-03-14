package helper;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.auth.Authenticator;
import com.couchbase.client.java.auth.PasswordAuthenticator;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.typesafe.config.Config;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Couchbase {


    private Cluster cluster;
    private static Bucket bucket;
    private static Config config;

    private static int TIMEOUT_CACHE_DOC = 200;

    public static final String CB_HOST = "127.0.0.1:8091";
    public static final String CB_USERNAME="Administrator";
    public static final String CB_PWD="couchbase";
    public static final String DPS_BUCKET="dps";

    @Inject
    public Couchbase(ApplicationLifecycle appLifecycle, Config config) {
        this.config = config;
        Logger.info("inside instance");
        connect();
        appLifecycle.addStopHook(() -> {
            Logger.info("Application stopped");
            disconnect();
            return CompletableFuture.completedFuture(null);
        });
    }

    public Couchbase(){
        connect();
    }

    private Authenticator getCBAuthenticator() {
        PasswordAuthenticator auth = new PasswordAuthenticator(CB_USERNAME,
                CB_PWD);
        return auth;
    }


    public boolean connect() {
        connectToCluster();
        connectToBucket();
        return true;
    }

    private boolean connectToCluster() {
        if (cluster == null) {
            synchronized (Couchbase.class) {
                if (cluster == null) {
                    CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                            .connectTimeout(30000) //10000ms = 10s, default is 5s
                            .build();
                    cluster = CouchbaseCluster.create(env, CB_HOST);
                    //Comment this for CB4.
                    cluster.authenticate(getCBAuthenticator());
                    Logger.info("Initialized couchbase cluster.");
                    return true;
                }
            }
        }
        return true;
    }

    private boolean connectToBucket() {
        this.bucket = cluster.openBucket(DPS_BUCKET);
        return true;
    }

    public boolean disconnect() {
        disconnectBucket();
        disconnectCluster();
        return true;
    }


    private boolean disconnectCluster() {
        return cluster.disconnect(5, TimeUnit.SECONDS);
    }


    private boolean disconnectBucket() {
        return this.bucket.close(5, TimeUnit.SECONDS) ;
    }

    public static JsonDocument get(String key)  {
        Logger.debug("GET CB Key : " + key);
        JsonDocument obj = bucket.get(key);
        if (obj == null) {
            Logger.debug("Could not find the key");
            //throw new DocumentDoesNotExistException("Could not find the key");
            return null;
        }
        Logger.debug("Getting Key value : " + obj.content().toString());
        return obj;
    }

    public static boolean exists(String key)  {
        return bucket.exists(key);
    }

    public static boolean update(String key, String val)  {
        Logger.debug("Setting Key "+key + " value " + val);
        try {
            JsonDocument result = bucket.upsert(toJsonDocument(key, val));
            Logger.debug("Done setting : " + result);
            return true;
        }catch(Exception e){
            Logger.error("Could not save the object " + e.getMessage());
            return false;
        }
    }

    public static boolean delete(String key) {
        try {
            Logger.debug("Deleting Key : " + key);
            bucket.remove(key);
            JsonDocument jsonDocument = bucket.get(key);
            if (jsonDocument == null) {
                Logger.debug("Deleting Key Done : " + key);
                return true;
            } else {
                Logger.debug("Deleting Key Done not possible : " + key);
                return false;
            }
        }catch (DocumentDoesNotExistException e){
            Logger.debug("Could not find key for delete :" + key);
            return false;
        }

    }

    //private methods
    private static  JsonDocument toJsonDocument(String key, String val) throws IOException {

        return JsonDocument.create(key, JsonObject.fromJson(val));
    }



}
