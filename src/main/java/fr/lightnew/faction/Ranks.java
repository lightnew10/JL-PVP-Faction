package fr.lightnew.faction;

public enum Ranks {
    CHEF,
    ADJOINT,
    MEMBRE,
    RECRUE,
    NONE;

    @Override
    public String toString() {
        return super.toString();
    }

    public static Boolean isRank(String str) {
        if (CHEF.toString().equalsIgnoreCase(str) || ADJOINT.toString().equalsIgnoreCase(str) || MEMBRE.toString().equalsIgnoreCase(str) || RECRUE.toString().equalsIgnoreCase(str))
            return true;
        return false;
    }
}
