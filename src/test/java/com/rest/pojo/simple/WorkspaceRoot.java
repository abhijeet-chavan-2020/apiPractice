package com.rest.pojo.simple;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WorkspaceRoot {
    private Workspace workspace;

    public WorkspaceRoot() {
    }

    public WorkspaceRoot(Workspace workspace) {
        this.workspace = workspace;
    }


}
