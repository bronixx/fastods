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
package com.github.jferard.fastods;

import java.util.Calendar;
import java.util.Date;

public abstract class CellValue {
	public static CellValue fromObject(final Object o) {
		if (o == null)
			return new VoidValue();
		else if (o instanceof String)
			return new StringValue((String) o);
		else if (o instanceof Number) // BigDecimal, Byte, Short, Integer,
										// Long, Float, Double
			return new FloatValue((Number) o);
		else if (o instanceof Boolean)
			return new BooleanValue((Boolean) o);
		else if (o instanceof Date) // java.util.Date & java.sql.Date,
									// java.sql.Time, java.sql.Timestamp
			return new DateValue((Date) o);
		else if (o instanceof Calendar)
			return new DateValue(((Calendar) o).getTime());
		else // Byte[], ...
			return new StringValue(o.toString());
	}

	public static CellValue fromTypeAndObject(final Object o) {
		// TODO: use the type hint, with a switch...
		return CellValue.fromObject(o);
	}

	public abstract void setToRow(HeavyTableRow heavyTableRow, int i);
}