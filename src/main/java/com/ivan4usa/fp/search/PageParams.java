package com.ivan4usa.fp.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PageParams {
    private Integer pageSize;
    private Integer pageNumber;
}
