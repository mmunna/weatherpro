package com.amunna.weatherpro.datastore.config;

import com.google.inject.Singleton;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

@Singleton
public final class DataStoreConfiguration {
    public static Configuration configuration = createConfig();

    private static Configuration createConfig() {
        Configuration config = HBaseConfiguration.create();
        config.clear();
        config.set("hbase.zookeeper.quorum", "localhost:2181");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.dns.nameserver", "localhost");
        config.set("hbase.regionserver.port", "60020");
        config.set("hbase.master", "localhost:9000");
        return config;
    }

}
