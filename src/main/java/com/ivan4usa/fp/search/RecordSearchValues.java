package com.ivan4usa.fp.search;

import com.ivan4usa.fp.entities.Filter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecordSearchValues {
    private Filter filter;
    private PageParams pageParams;
}
