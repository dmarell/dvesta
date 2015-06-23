/*
 * Created by Daniel Marell 2011-11-29 22:59
 */
package se.marell.dvesta.ioscan;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BitPost {
    private boolean status;
    private boolean overrideEnable;
    private boolean overrideStatus;

    public BitPost() {
    }

    public BitPost(boolean status, boolean overrideEnable, boolean overrideStatus) {
        this.status = status;
        this.overrideEnable = overrideEnable;
        this.overrideStatus = overrideStatus;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getOverrideEnable() {
        return overrideEnable;
    }

    public void setOverrideEnable(boolean overrideEnable) {
        this.overrideEnable = overrideEnable;
    }

    public boolean getOverrideStatus() {
        return overrideStatus;
    }

    public void setOverrideStatus(boolean overrideStatus) {
        this.overrideStatus = overrideStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
