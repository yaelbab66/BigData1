package notvsUse;

class AppData {
    private final String name;
    private final int totalUsage;
    private final int totalNotifications;

    public AppData(String name, int totalUsage, int totalNotifications) {
        this.name = name;
        this.totalUsage = totalUsage;
        this.totalNotifications = totalNotifications;
    }

    public String getName() {
        return name;
    }

    public int getTotalUsage() {
        return totalUsage;
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }
}