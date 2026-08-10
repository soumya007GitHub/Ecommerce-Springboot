package com.soumya.ecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response{

    private int httpStatusCode;
    private String message;
}