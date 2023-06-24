package com.project.runningcrew.member.entity;

public enum MemberRole {
    ROLE_NORMAL("크루원", false),
    ROLE_MANAGER("매니저", true),
    ROLE_LEADER("크루리더", true);

    private String name;

    private boolean isManager;

    MemberRole(String name, boolean isManager) {
        this.name = name;
        this.isManager = isManager;
    }

    public String getName() {
        return name;
    }

    public boolean isManager() {
        return isManager;
    }

}
