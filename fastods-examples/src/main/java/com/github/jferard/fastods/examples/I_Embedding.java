/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.examples;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.DrawFrame;
import com.github.jferard.fastods.DrawObject;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.Tooltip;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.ref.RangeRef;
import com.github.jferard.fastods.style.DrawFillImage;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.tool.InsertHelper;
import com.github.jferard.fastods.util.FileUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Section 8 of the tutorial
 *
 * @author J. Férard
 */
class I_Embedding {
    /**
     * @throws IOException if the file can't be written
     */
    static void exampleWithFile() throws IOException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("rs");
        final TableCellWalker walker = table.getWalker();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Embedding external content
        // Embedding an external content is a two steps operation: 1. embed the content in the ods
        // archive; 2. hook the content to the document: to a cell, a table, a tooltip, an
        // event...
        //
        // I kept those two steps in the API for a simple reason: you might want to perform only
        // one of those actions. Sometimes you don't want to really
        // embed the content in the archive, but only to make reference to an URL. And sometimes
        // you don't want to hook the content, just to embed it. To keep the process easy, there is
        // always a tool for grouping those steps.
        //
        // First, we'll see how to embed a file into the ods archive, then we'll discover the
        // available FastODS functions to hook this file to the document.
        //
        // ## Add files to the ods archive
        // Remember the method to add an auto update to the document? That was:
        //
        //     new MacroHelper().addRefreshMacro(document);
        //
        // Under the hood, this function adds some files to the ods archive. The ods
        // archive contains a `manifest.xml` that lists the files. If a file was added
        // without a matching entry in the manifest, LibreOffice will bark and refuse to
        // open the file.
        //
        // Let's add a file for the fun:
        document.addExtraDir("FastODS");
        document.addExtraFile("FastODS/fast.txt", "text/plain",
                "Hello from FastODS!".getBytes(ZipUTF8Writer.UTF_8));
        //
        // You can check that the file was added with your favorite file archive viewer.
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "i_embedding_a_file.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void exampleWithImage() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Add an image
        //
        // As usual, we create a document and a table:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("test");

        // We need to create the "Pictures" directory in the archive
        document.addExtraDir("Pictures");

        // And there is a tool to insert the image:
        final InputStream sourceStream = new URL("https://raw.githubusercontent" +
                ".com/wiki/jferard/fastods/images/j_periodic_table.png").openStream();
        InsertHelper.create()
                .insertImage(document, table, "Frame 1", sourceStream, "periodic_table.png",
                        Length.NULL_LENGTH, Length.NULL_LENGTH, SimpleLength.cm(15),
                        SimpleLength.cm(10));
        //
        // That's all!
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "i_embedding_image.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void exampleWithDocument() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Embed a document inside a table
        //
        // As usual, we create a document and a table:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("test");

        // We need to create the "Object 1" directory in the archive
        document.addExtraObject("Object 1", "application/vnd.oasis.opendocument.spreadsheet",
                "1.2");

        // And to fill the directory with the interesting elements of the object
        final InputStream inputStream =
                Resources.asByteSource(Resources.getResource("a_hello_world_example.ods"))
                        .openStream();
        final ZipInputStream zipStream = new ZipInputStream(inputStream);
        ZipEntry entry = zipStream.getNextEntry();
        while (entry != null) {
            final String name = entry.getName();
            if (!name.startsWith("META-INF") && !name.startsWith("mimetype") &&
                    !name.startsWith("Thumbnails")) {
                final byte[] bytes = FileUtil.create().readStream(zipStream);
                document.addExtraFile("Object 1/" + name, "text/xml", bytes);
            }
            entry = zipStream.getNextEntry();
        }

        // Now, we insert the document into a frame:
        final DrawObject object = new DrawObject("./Object 1", Collections.<RangeRef>emptyList());
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        table.addShape(DrawFrame.builder("embed", object, SimpleLength.cm(1), SimpleLength.cm(1),
                SimpleLength.cm(15), SimpleLength.cm(10)).style(gs)
                .build());

        // This will be a tool soon...
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "i_embedding_object.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void exampleWithCommentBG() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Add an image to the background of a comment.
        // That was a feature request. Here's the way to do it.
        //
        // The process is a little bit more complex, since the background of the tooltip is set
        // with a "fill style".
        //
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("test");

        // We create the cell to annotate
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("This cell has an interesting annotation");

        // Again, we need to create the "Pictures" directory in the archive
        document.addExtraDir("Pictures");

        // And there is a tool to create the fill style:
        final InputStream sourceStream = new URL("https://raw.githubusercontent" +
                ".com/wiki/jferard/fastods/images/j_periodic_table.png").openStream();
        final DrawFillImage drawFillImage = InsertHelper.create()
                .createDrawFillImage(document, sourceStream, "periodic", "Pictures/periodic.png");

        // Now that the "fill-style" is created, you just have to set the style of the tooltip.
        final GraphicStyle gs = GraphicStyle.builder("gs").fillImage(drawFillImage).build();
        final Tooltip tooltip =
                Tooltip.builder(XMLUtil.create(), "This the perodic table of elements")
                        .x(SimpleLength.cm(9)).y(SimpleLength.cm(1)).width(SimpleLength.cm(15))
                        .height(SimpleLength.cm(10)).graphicStyle(gs).visible().build();

        // And set the tooltip:
        walker.setTooltip(tooltip);

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "i_embed_tt_image.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void exampleWithTable() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## An table inside a table
        // Warning: this is not understood by LibreOffice.
        //
        // As usual, we create a document and a table:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // Then we create an outer and an inner table:
        final Table outerTable = document.createTable("outer");
        document.addTable(outerTable);
        final TableCellWalker outerWalker = outerTable.getWalker();
        outerWalker.setStringValue("I'm the outer table");

        final Table innerTable = document.createTable("inner");
        final TableCellWalker innerWalker = innerTable.getWalker();
        innerWalker.setStringValue("I'm the inner table");
        // We do not add the inner table to the document as usual, but we place the inner table
        // in the outer table:
        outerTable.addShape(DrawFrame
                .builder("embed", innerTable, SimpleLength.cm(1), SimpleLength.cm(1),
                        SimpleLength.cm(15), SimpleLength.cm(10)).build());
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "i_embedding_table.ods"));
    }
}