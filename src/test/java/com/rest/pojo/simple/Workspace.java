package com.rest.pojo.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workspace {
    private String name;
    private String type;
    private  String id;
    private String description;
    public Workspace() {
    }

    public Workspace(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }


}
