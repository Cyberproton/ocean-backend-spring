package me.cyberproton.ocean.features.history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.history.entity.HistoryType;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class HistoryQuery extends BaseQuery {
    private HistoryType type;
}
