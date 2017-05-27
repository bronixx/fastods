/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jferard on 19/05/17.
 */
public class ConfigItemMapNamedTest {
    private ConfigItem item;
    private ConfigItemMapEntrySingleton block;
    private ConfigItemMapEntrySet set;
    private XMLUtil util;
    private String setXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.set = ConfigItemMapEntrySet.createSet("set");
        this.set.add(this.item);
        this.util = XMLUtil.create();

        final StringBuilder sb = new StringBuilder();
        this.set.appendXML(this.util, sb);
        this.setXML = sb.toString();
    }

    @Test
    public void createMapNamed() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
        Assert.assertEquals("mapnamed", m.getName());
    }

    @Test
    public void createMapNamed2() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        Assert.assertEquals(1, m.size());
        Assert.assertFalse(m.isEmpty());
    }

    @Test
    public void removeByName() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        Assert.assertEquals(1, m.size());
        Assert.assertNull(m.removeByName("s"));
        Assert.assertEquals(1, m.size());
        Assert.assertEquals(this.set, m.removeByName("set"));
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
    }

    @Test
    public void getByName() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        Assert.assertEquals(this.set, m.getByName("set"));
        Assert.assertNull(m.getByName("s"));
    }

    @Test
    public void contains() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        Assert.assertFalse(m.contains("s"));
        Assert.assertTrue(m.contains("set"));
    }

    @Test
    public void iterator() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        final Iterator<ConfigItemMapEntry> i = m.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(this.set, i.next());
        Assert.assertFalse(i.hasNext());
    }

    @Test
    public void appendXML() throws Exception {
        final ConfigItemMapNamed m = new ConfigItemMapNamed("mapnamed");
        m.put(this.set);
        final StringBuilder sb = new StringBuilder();
        m.appendXML(this.util, sb);
        Assert.assertEquals("<config:config-item-map-named config:name=\"mapnamed\">" +
                this.setXML+
                "</config:config-item-map-named>", sb.toString());
    }

}