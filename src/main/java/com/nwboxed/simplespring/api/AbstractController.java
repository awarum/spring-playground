package com.nwboxed.simplespring.api;

import java.util.Optional;
import com.nwboxed.simplespring.model.ResourceNotFoundException;

public class AbstractController {
    public <T> T checkResourceFound(final Optional<T> resource) {
        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return resource.get();
    }
}
