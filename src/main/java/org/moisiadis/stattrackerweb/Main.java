package org.moisiadis.stattrackerweb;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.moisiadis.stattrackercore.database.MarioKartDB;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * Main class.
 *
 */
public class Main {
    public static MarioKartDB db;
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";
    private static final String DB_PATH = "stat-tracker-core/src/sqlite.db";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.moisiadis package
        final ResourceConfig rc = new ResourceConfig().packages("org.moisiadis");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, SQLException {
        String path = System.getenv("statdbpath");
        System.out.println(path);

        db = (path == null) ? new MarioKartDB(DB_PATH) : new MarioKartDB(path);

        if (args.length > 0) {
            switch (args[0]) {
                case "init":
                    db.initialize();
                case "delete":
                    db.delete();
                    System.exit(0);
                default:
                    break;
            }
        }

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();
        db.close();
    }
}

