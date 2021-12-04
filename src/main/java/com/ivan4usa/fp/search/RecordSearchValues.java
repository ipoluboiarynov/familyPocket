package com.ivan4usa.fp.search;

import com.ivan4usa.fp.entities.Filter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecordSearchValues {
    private Filter filter;
    private PageParams pageParams;
}
