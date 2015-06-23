/*
 * Created by Daniel Marell 15-01-04 00:16
 */
package se.marell.dvesta.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BitStateChangeDetectorTest {
    @Test
    public void shouldReportFirstEdge() throws Exception {
        BitStateChangeDetector d = new BitStateChangeDetector();
        assertThat(d.hasChanged(true), is(true));
        assertThat(d.hasChanged(true), is(false));
        assertThat(d.hasChanged(false), is(true));
    }

    @Test
    public void shouldNotReportFirstRisingEdge() throws Exception {
        BitStateChangeDetector d = new BitStateChangeDetector(false);
        assertThat(d.hasChanged(true), is(false));
        assertThat(d.hasChanged(true), is(false));
        assertThat(d.hasChanged(false), is(true));
    }

    @Test
    public void shouldNotReportFirstTrailingEdge() throws Exception {
        BitStateChangeDetector d = new BitStateChangeDetector(false);
        assertThat(d.hasChanged(false), is(false));
        assertThat(d.hasChanged(false), is(false));
        assertThat(d.hasChanged(true), is(true));
    }
}
