package com.api.bookmanagement.dto.error;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseError implements Serializable {

    String type;
    String title;
    String detail;
    String status;
    String traceId;

}
