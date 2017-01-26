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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileWriter {
	public static OdsFileWriterBuilder builder(final Logger logger, final OdsDocument document) {
		return new OdsFileWriterBuilder(logger, document);
	}

	private final OdsDocument document;
	private final Logger logger;
	private final ZipUTF8Writer writer;

	/**
	 * Create a new ODS file.
	 *
	 * @param logger   the logger
	 * @param document the document to write
	 * @param writer   The writer for this file
	 */
	OdsFileWriter(final Logger logger, final OdsDocument document, final ZipUTF8Writer writer)
			throws FileNotFoundException {
		this.logger = logger;
		this.document = document;
		this.writer = writer;
	}

	public void close() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	public void finalizeFlush() throws IOException {
		this.document.flushTables(this.writer);
		this.document.finalizeContent(this.writer);
		this.document.finalizeFlush(this.writer);
	}

	public void flushEditableElements() throws IOException {
		this.document.flushEditableElements(this.writer);
	}

	public void flushRows() throws IOException {
		this.document.flushRows(this.writer);
	}

	public void flushTables() throws IOException {
		this.document.flushTables(this.writer);
	}

	public void prepareForFlush() throws IOException {
		this.document.prepareForFlush(this.writer);
	}

	/**
	 * Save the new file.
	 *
	 * @throws IOException If an I/O error occurs
	 */
	public void save() throws IOException {
		this.document.save(this.writer);
	}
}