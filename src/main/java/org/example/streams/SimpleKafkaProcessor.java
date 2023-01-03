package org.example.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class SimpleKafkaProcessor {

    private static String APPLICATION_NAME = "streams-filter-application";
    private final static String BOOTSTRAP_SERVERS = "localhost:29092";
    private static String STREAM_LOG = "stream_log";
    private static String STREAM_LOG_FILTER = "stream_log_copy";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Topology topology = new Topology();
        topology.addSource("Source", STREAM_LOG)
                .addProcessor("Process", () -> new FilterProcessor(), "Source") // 마지막 파라미터는 부모인자이름
                .addSink("Sink", STREAM_LOG_FILTER, "Process"); // 마지막 파라미터는 부모인자이름

        KafkaStreams streaming = new KafkaStreams(topology, props);
        streaming.start();
    }
}
