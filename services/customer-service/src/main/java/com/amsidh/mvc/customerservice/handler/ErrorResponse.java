package com.amsidh.mvc.customerservice.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors) {
}
