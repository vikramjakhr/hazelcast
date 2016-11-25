package com.hazelcast.aggregation;

import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.map.impl.MapEntrySimple;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.query.impl.QueryableEntry;
import com.hazelcast.query.impl.getters.Extractors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

final class TestSamples {

    private static final int NUMBER_OF_SAMPLE_VALUES = 10000;

    private TestSamples() {
    }

    static <T> Map.Entry<T, T> createEntryWithValue(T value) {
        return new MapEntrySimple<T, T>(value, value);
    }

    static <T> Map.Entry<T, T> createExtractableEntryWithValue(T value) {
        return new ExtractableEntry<T, T>(value, value);
    }

    static List<Integer> sampleIntegers() {
        return sampleValues(new RandomNumberSupplier<Integer>() {
            @Override
            protected Integer mapFrom(Number value) {
                return value.intValue();
            }
        });
    }

    static List<Long> sampleLongs() {
        return sampleValues(new RandomNumberSupplier<Long>() {
            @Override
            protected Long mapFrom(Number value) {
                return value.longValue();
            }
        });
    }

    static Collection<Float> sampleFloats() {
        return sampleValues(new RandomNumberSupplier<Float>() {
            @Override
            protected Float mapFrom(Number value) {
                return value.floatValue();
            }
        });
    }

    static List<Double> sampleDoubles() {
        return sampleValues(new RandomNumberSupplier<Double>() {
            @Override
            protected Double mapFrom(Number value) {
                return value.doubleValue();
            }
        });
    }

    static List<BigDecimal> sampleBigDecimals() {
        return sampleValues(new RandomNumberSupplier<BigDecimal>() {
            @Override
            protected BigDecimal mapFrom(Number value) {
                return BigDecimal.valueOf(value.doubleValue());
            }
        });
    }

    static List<BigInteger> sampleBigIntegers() {
        return sampleValues(new RandomNumberSupplier<BigInteger>() {
            @Override
            protected BigInteger mapFrom(Number value) {
                return BigInteger.valueOf(value.longValue());
            }
        });
    }

    static List<String> sampleStrings() {
        String loremIpsum = "Lorem ipsum dolor sit amet consectetur adipiscing elit";
        return asList(loremIpsum.split(" "));
    }

    static List<Person> samplePersons() {
        List<Person> personList = new ArrayList<Person>(NUMBER_OF_SAMPLE_VALUES);
        for (Double age : sampleDoubles()) {
            personList.add(new Person(age));
        }
        return personList;
    }

    static List<NumberContainer> sampleNumberContainers(NumberContainer.ValueType valueType) {
        List<NumberContainer> containerList = new ArrayList<NumberContainer>(NUMBER_OF_SAMPLE_VALUES);
        switch (valueType) {
            case INTEGER:
                for (int intValue : sampleIntegers()) {
                    containerList.add(new NumberContainer(intValue));
                }
                break;
            case LONG:
                for (long longValue : sampleLongs()) {
                    containerList.add(new NumberContainer(longValue));
                }
                break;
            case FLOAT:
                for (float floatValue : sampleFloats()) {
                    containerList.add(new NumberContainer(floatValue));
                }
                break;
            case DOUBLE:
                for (double doubleValue : sampleDoubles()) {
                    containerList.add(new NumberContainer(doubleValue));
                }
                break;
            case BIG_DECIMAL:
                for (BigDecimal bigDecimal : sampleBigDecimals()) {
                    containerList.add(new NumberContainer(bigDecimal));
                }
                break;
            case BIG_INTEGER:
                for (BigInteger bigInteger : sampleBigIntegers()) {
                    containerList.add(new NumberContainer(bigInteger));
                }
                break;
            case NUMBER:
                new ArrayList<NumberContainer>();
                for (Long longValue : sampleLongs()) {
                    createNumberContainer(containerList, longValue);
                }
                for (Double doubleValue : sampleDoubles()) {
                    createNumberContainer(containerList, doubleValue);
                }
                for (Integer intValue : sampleIntegers()) {
                    createNumberContainer(containerList, intValue);
                }
                break;
        }
        return containerList;
    }

    private static <T extends Number> List<T> sampleValues(RandomNumberSupplier<T> randomNumberSupplier) {
        List<T> numbers = new ArrayList<T>();
        for (int i = 0; i < NUMBER_OF_SAMPLE_VALUES; i++) {
            numbers.add(randomNumberSupplier.get());
        }
        return numbers;
    }

    private static void createNumberContainer(List<NumberContainer> values, Number value) {
        NumberContainer container = new NumberContainer();
        container.numberValue = value;
        values.add(container);
    }

    private static final class ExtractableEntry<K, V> extends QueryableEntry<K, V> {

        private K key;
        private V value;

        ExtractableEntry(K key, V value) {
            this.extractors = new Extractors(Collections.<MapAttributeConfig>emptyList(), null);
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public Data getKeyData() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Data getValueData() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object getTargetObject(boolean key) {
            return key ? this.key : this.value;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        void setKey(K key) {
            this.key = key;
        }

        void setSerializationService(InternalSerializationService serializationService) {
            this.serializationService = serializationService;
        }
    }
}
