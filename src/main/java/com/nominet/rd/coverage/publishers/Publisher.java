package com.nominet.rd.coverage.publishers;

import com.nominet.rd.coverage.models.ModelResponse;

@FunctionalInterface
public interface Publisher {
    void publish(ModelResponse response);
}
