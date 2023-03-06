package com.project.runningcrew.entity.members;

public enum MemberRole {
    ROLE_NORMAL(false),
    ROLE_MANAGER(true),
    ROLE_LEADER(true);

    private boolean isManager;

    MemberRole(boolean isManager) {
        this.isManager = isManager;
    }

    public boolean isManager() {
        return isManager;
    }
}
