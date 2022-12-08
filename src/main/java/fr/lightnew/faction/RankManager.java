package fr.lightnew.faction;

public class RankManager {

    private String name;
    private PermissionManager permissions;
    private String prefix;

    public RankManager(String name, String prefix, PermissionManager permissions) {
        this.name = name;
        this.prefix = prefix;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public PermissionManager getPermissions() {
        return permissions;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(PermissionManager permissions) {
        this.permissions = permissions;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
