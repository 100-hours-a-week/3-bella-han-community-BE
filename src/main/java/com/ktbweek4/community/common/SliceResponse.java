// com.ktbweek4.community.common.SliceResponse
package com.ktbweek4.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SliceResponse<T> {
    private final List<T> items;
    private final boolean hasNext;
    private final Long nextCursor; // 다음 요청 시 사용할 cursor (postId 기반)
}