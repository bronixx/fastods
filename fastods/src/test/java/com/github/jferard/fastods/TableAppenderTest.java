/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class TableAppenderTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableAppender tableAppender;
    private XMLUtil xmlUtil;
    private TableBuilder tb;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        this.tb = PowerMock.createMock(TableBuilder.class);
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.tableAppender = new TableAppender(this.tb);
        this.xmlUtil = xmlUtil;
    }

    @Test
    public void appendEmptyPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newListWithCapacity(1));
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendShapesTest() throws IOException {
        final DrawFrame drawFrame =
                DrawFrame.builder("a", new DrawImage("href"), SVGRectangle.cm(0, 1, 2, 3)).build();

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newListWithCapacity(1));
        EasyMock.expect(this.tb.getShapes()).andReturn(Arrays.<Shape>asList(drawFrame));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:shapes><draw:frame draw:name=\"a\" " +
                        "draw:z-index=\"0\" svg:width=\"2cm\" svg:height=\"3cm\" svg:x=\"0cm\" " +
                        "svg:y=\"1cm\"><draw:image xlink:href=\"href\" xlink:type=\"simple\" " +
                        "xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" +
                        "</draw:frame></table:shapes>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendOneElementPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.newList(this.newTC("x")));
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1023\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendTwoElementsPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.newList(this.newTC("x"), this.newTC("x")));
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1022\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendFourElementsPreambleTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.newList(x, x, this.newTC("y"), x));
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"x\"" + " " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " " +
                        "table:number-columns-repeated=\"1020\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public void appendTenElementsPreambleTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("table1");
        EasyMock.expect(this.tb.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public final void testName() throws IOException {
        final StringBuilder sb = new StringBuilder();

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("tb");
        EasyMock.expect(this.tb.getStyleName()).andReturn("tb-style");
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tb.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>builder().build());
        EasyMock.expect(this.tb.getTableRowsUsedSize()).andReturn(0);
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.tableAppender.appendAllAvailableRows(this.xmlUtil, sb);

        PowerMock.verifyAll();
        sb.append("</table:table>");
        DomTester.assertEquals("<table:table table:name=\"tb\" table:style-name=\"tb-style\" " +
                "table:print=\"false\"><office:forms form:automatic-focus=\"false\" " +
                "form:apply-design-mode=\"false\"/><table:table-column " +
                "table:style-name=\"co1\" " + "table:number-columns-repeated=\"1024\" " +
                "table:default-cell-style-name=\"Default\"/></table:table>", sb.toString());
    }

    @Test
    public final void testAppendTwoWriters() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final FastFullList<TableColumnImpl> emptyFullList = FastFullList.<TableColumnImpl>builder().build();

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getName()).andReturn("tb").times(2);
        EasyMock.expect(this.tb.getStyleName()).andReturn("tb-style").times(2);
        EasyMock.expect(this.tb.getCustomValueByAttribute()).andReturn(null).times(2);
        EasyMock.expect(this.tb.getColumns()).andReturn(emptyFullList).times(2);
        EasyMock.expect(this.tb.getTableRowsUsedSize()).andReturn(0).times(2);
        EasyMock.expect(this.tb.getShapes()).andReturn(Collections.<Shape>emptyList()).times(2);

        PowerMock.replayAll();
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb1);
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb2);

        PowerMock.verifyAll();
        DomTester.assertEquals(sb1.toString(), sb2.toString());
    }

    private void assertPreambleXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.tableAppender.appendPreambleOnce(this.xmlUtil, sb);
        sb.append("</table:table>");
        DomTester.assertEquals(xml + "</table:table>", sb.toString());
    }

    private TableColumnImpl newTC(final String name) {
        final TableColumnStyle tcs = TableColumnStyle.builder(name).build();
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        return tc;
    }
}
