package com.adhm.dawam.dto;

import com.adhm.dawam.entity.Section;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SectionResponse {

    private UUID id;
    private String name;
    private Integer position;

    public static SectionResponse from(Section section) {
        SectionResponse response = new SectionResponse();
        response.id = section.getId();
        response.name = section.getName();
        response.position = section.getPosition();
        return response;
    }
}