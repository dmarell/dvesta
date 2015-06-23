/*
 * Created by Daniel Marell 2011-12-10 21:12
 */
package se.marell.dvesta.ioscan;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntegerPost {
    private int value;
    private boolean overrideEnable;
    private int overrideValue;

    public IntegerPost() {
    }

    public IntegerPost(int value, boolean overrideEnable, int overrideValue) {
        this.value = value;
        this.overrideEnable = overrideEnable;
        this.overrideValue = overrideValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isOverrideEnable() {
        return overrideEnable;
    }

    public void setOverrideEnable(boolean overrideEnable) {
        this.overrideEnable = overrideEnable;
    }

    public int getOverrideValue() {
        return overrideValue;
    }

    public void setOverrideValue(int overrideValue) {
        this.overrideValue = overrideValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}