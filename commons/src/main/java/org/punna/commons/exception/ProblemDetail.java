package org.punna.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDetail {

    private final Map<String, Object> errors = new HashMap<>();

    private String message;

    private Integer status;
}
