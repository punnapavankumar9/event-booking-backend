package com.punna.eventcatalog;

import org.apache.commons.lang3.tuple.Pair;

public class TestConfigSettings {
    public static final Pair<String, String> MONGO_CONTAINER_USER = Pair.of("MONGO_INITDB_ROOT_USERNAME",
            "eventcatalog"
                                                                           );
    public static final Pair<String, String> MONGO_CONTAINER_PASSWORD = Pair.of("MONGO_INITDB_ROOT_PASSWORD",
            "mongodb"
                                                                               );

    public static final String dbName = "event-catalog";
}
