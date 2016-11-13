/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class Util {
	private PositionUtil positionUtil;
	private WriteUtil writeUtil;

	public Util(PositionUtil positionUtil, WriteUtil writeUtil) {
		this.positionUtil = positionUtil;
		this.writeUtil = writeUtil;
	}

	public boolean equal(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}
	
	public boolean different(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 != null;
		} else {
			return !o1.equals(o2);
		}
	}

	/**
	 * Convert a cell position string like B3 to the column number.
	 *
	 * @param pos
	 *            The cell position in the range 'A1' to 'IV65536'
	 * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
	 *         4
	 */
	public PositionUtil.Position getPosition(final String pos) {
		return this.positionUtil.getPosition(pos);
	}

	public String toString(final int value) {
		return this.writeUtil.toString(value);
	}

	/**
	 * Wraps an OutputStream in a BufferedWriter
	 *
	 * @param out
	 *            the stream
	 * @return the writer
	 */
	public Writer wrapStream(final OutputStream out, final int size) {
		return this.writeUtil.wrapStream(out, size);
	}

	public static Util create() {
		return new Util(new PositionUtil(), new WriteUtil());
	}
}
