/*
 * Created by Daniel Marell 14-11-23 17:53
 */
package se.marell.dvesta.iodevices.razberry.impl.commanddata.zautomation;

public class ZAutomationDeviceMetrics {
    private String level;
    private String icon;
    private String title;

    public ZAutomationDeviceMetrics() {
    }

    public ZAutomationDeviceMetrics(String level, String icon, String title) {
        this.level = level;
        this.icon = icon;
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "{" +
                "class: RazberryDeviceMetrics" +
                ", level: " + level +
                ", icon: " + icon +
                ", title: " + title +
                '}';
    }
}
