package com.adnan.test_interview_wti.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private String message;

    private T data;

    private String errors;

    private PagingResponse paging;
}
