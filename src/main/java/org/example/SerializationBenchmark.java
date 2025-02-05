package org.example;

import com.example.protobuf.PersonProto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Measure execution time
@OutputTimeUnit(TimeUnit.NANOSECONDS) // Results in nanoseconds
@State(Scope.Thread) // Each thread gets a fresh instance
public class SerializationBenchmark {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PersonJson jsonPerson = new PersonJson();
    private final PersonProto.Person protoPerson;

    public SerializationBenchmark() {
        // Initialize JSON object
        jsonPerson.setName("John Doe");
        jsonPerson.setAge(30);
        jsonPerson.setEmail("john.doe@example.com");

        // Initialize Protobuf object
        protoPerson = PersonProto.Person.newBuilder()
                .setName("John Doe")
                .setAge(30)
                .setEmail("john.doe@example.com")
                .build();
    }

    @Benchmark
    public byte[] jsonSerialization() throws IOException {
        return objectMapper.writeValueAsBytes(jsonPerson);
    }

    @Benchmark
    public byte[] protobufSerialization() {
        return protoPerson.toByteArray();
    }

    @Benchmark
    public PersonJson jsonDeserialization() throws IOException {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(jsonPerson);
        return objectMapper.readValue(jsonBytes, PersonJson.class);
    }

    @Benchmark
    public PersonProto.Person protobufDeserialization() throws InvalidProtocolBufferException {
        byte[] protoBytes = protoPerson.toByteArray();
        return PersonProto.Person.parseFrom(protoBytes);
    }
}
