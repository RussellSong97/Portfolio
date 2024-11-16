package com.cuv.domain.linklisting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LinkListingTransDto {
    private Long id;
    private String carFrameNo;
    private List<String> carFrameNoList;

}
