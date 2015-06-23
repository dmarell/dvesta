/*
 * Created by Daniel Marell 2011-12-10 21:08
 */
package se.marell.dvesta.ioscan;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FloatPost {
    private float value;
    private boolean overrideEnable;
    private float overrideValue;

    public FloatPost() {
    }

    public FloatPost(float value, boolean overrideEnable, float overrideValue) {
        this.value = value;
        this.overrideEnable = overrideEnable;
        this.overrideValue = overrideValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isOverrideEnable() {
        return overrideEnable;
    }

    public void setOverrideEnable(boolean overrideEnable) {
        this.overrideEnable = overrideEnable;
    }

    public float getOverrideValue() {
        return overrideValue;
    }

    public void setOverrideValue(float overrideValue) {
        this.overrideValue = overrideValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
