package com.hugoltsp.spring.boot.starter.jwt.filter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class SimpleFifoHttpRequestCache extends ConcurrentHashMap<HttpRequest, Boolean> {

    private final int maxEntries;

    private final Deque<HttpRequest> httpRequestLinkedList = new LinkedList<>();

    SimpleFifoHttpRequestCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    @Override
    public Boolean computeIfAbsent(HttpRequest key, Function<? super HttpRequest, ? extends Boolean> mappingFunction) {

        if (hasExceededMaxEntries()) {
            removeLastEntry();
        }

        push(key);

        return super.computeIfAbsent(key, mappingFunction);
    }

    private boolean hasExceededMaxEntries() {
        return size() >= maxEntries;
    }

    private void removeLastEntry() {
        pollLast().ifPresent(super::remove);
    }

    private Optional<HttpRequest> pollLast() {
        return Optional.ofNullable(httpRequestLinkedList.pollLast());
    }

    private void push(HttpRequest key) {
        httpRequestLinkedList.push(key);
    }

}
