package com.hugoltsp.spring.boot.starter.jwt.filter;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

class SimpleFifoHttpRequestCache extends ConcurrentHashMap<HttpRequest, Boolean> {

    private final int maxEntries;

    private final Deque<HttpRequest> httpRequestLinkedList = new ConcurrentLinkedDeque<>();

    SimpleFifoHttpRequestCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    @Override
    public Boolean computeIfAbsent(HttpRequest key, Function<? super HttpRequest, ? extends Boolean> mappingFunction) {

        removeLastEntries();

        return super.computeIfAbsent(key, mappingFunction.compose(this::push));
    }

    private boolean hasExceededMaxEntries() {
        return size() >= maxEntries;
    }

    private void removeLastEntries() {
        while (hasExceededMaxEntries()) {
            pollLast().ifPresent(super::remove);
        }
    }

    private Optional<HttpRequest> pollLast() {
        return Optional.ofNullable(httpRequestLinkedList.pollLast());
    }

    private HttpRequest push(HttpRequest key) {
        httpRequestLinkedList.push(key);
        return key;
    }

}
