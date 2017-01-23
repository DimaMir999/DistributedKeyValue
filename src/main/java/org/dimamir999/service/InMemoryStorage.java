package org.dimamir999.service;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStorage<K,V> {

    private Map<K,V> data = new HashMap<>();
}
