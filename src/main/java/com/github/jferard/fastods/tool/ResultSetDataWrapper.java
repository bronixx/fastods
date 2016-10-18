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
package com.github.jferard.fastods.tool;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;

/**
 *
 * This file OldLightTableRow.java is part of FastODS.
 *
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * Usage :
 *
 * <pre>
 * {@code
 * 		OdsFile file = OdsFile.create("7columns.ods");
 *		final Table table = file.addTable("test", 50, 5);
 *		XMLUtil xmlUtil = FastOds.getXMLUtil();
 *		TableCellStyle tcls = TableCellStyle.builder(xmlUtil, "rs-head")
 *				.backgroundColor("#dddddd").fontWeightBold().build();
 *		DataWrapper data = new ResultSetDataWrapper(rs, tcls, 100);
 *		DataWrapper data2 = new ResultSetDataWrapper(rs2, tcls, 100);
 *
 *		table.addData(data);
 *		table.nextRow();
 *		table.addData(data2);
 *		TableCellStyle tcls = TableCellStyle.builder(xmlUtil, "rs-head")
 *				.backgroundColor("#dddddd").fontWeightBold().build();
 *		DataWrapper data = new ResultSetDataWrapper(rs, tcls, 100);
 *		DataWrapper data2 = new ResultSetDataWrapper(rs2, tcls, 100);
 *
 *		table.addData(data);
 *		table.nextRow();
 *		table.addData(data2);
 * }
 *
 * <pre>
 *
 * @author Julien Férard
 */
public final class ResultSetDataWrapper implements DataWrapper {
	/**
	 * column count of the ResultSet.
	 */
	private final TableCellStyle headCellStyle;
	/**
	 * maximum number of lines to be written
	 */
	private final int max;

	/**
	 * the ResultSet.
	 */
	private final ResultSet resultSet;
	private Logger logger;
	private ResultSet rs;

	public ResultSetDataWrapper(final Logger logger, final ResultSet rs,
			final TableCellStyle headCellStyle, final int max) {
		this.logger = logger;
		this.resultSet = rs;
		this.headCellStyle = headCellStyle;
		this.max = max;
		this.rs = rs;
	}

	@Override
	public boolean addToTable(final Table table) {
		int rowCount = 0; // at least 
		try {
			ResultSetMetaData metadata = this.rs.getMetaData();
			HeavyTableRow row;
			try {
				row = table.nextRow();
				final int columnCount = metadata.getColumnCount();
				this.writeFirstLineDataTo(metadata, row);
				if (this.resultSet.next()) {
					do {
						if (++rowCount <= this.max) {
							row = table.nextRow();
							this.writeDataLineTo(row,
									columnCount);
						}
					} while (this.resultSet.next());
				}
				this.writeMaybeLastLineDataTo(columnCount, table, rowCount);
			} catch (final SQLException e) {
				this.logger.log(Level.SEVERE, "Can't read ResultSet row", e);
			}
		} catch (final SQLException e) {
			this.logger.log(Level.SEVERE, "Can't read ResultSet metadata", e);
		}
		return rowCount > 0;
	}

	/**
	 * @param metadata
	 * @return the name of the columns
	 * @throws SQLException
	 */
	private List<String> getColumnNames(ResultSetMetaData metadata)
			throws SQLException {
		int columnCount = metadata.getColumnCount();
		final List<String> names = new ArrayList<String>(columnCount);
		for (int i = 0; i < columnCount; i++)
			names.add(metadata.getColumnName(i + 1));

		return names;
	}

	/**
	 * @return the values of the current column
	 * @throws SQLException
	 * @throws IOException
	 */
	private List<Object> getColumnValues(final int columnCount)
			throws SQLException {
		final List<Object> values = new ArrayList<Object>(columnCount);
		for (int i = 0; i < columnCount; i++) {
			values.add(this.resultSet.getObject(i + 1));
		}
		return values;
	}

	private void writeDataLineTo(final HeavyTableRow row, final int columnCount)
			throws SQLException {
		final List<Object> columnValues = this.getColumnValues(columnCount);
		final TableCellWalker walker = row.getWalker();
		for (int j = 0; j <= columnCount - 1; j++) {
			final Object object = columnValues.get(j);
			walker.nextCell();
			if (object == null)
				walker.setStringValue("<NULL>");
			else
				walker.setObjectValue(object);
		}
	}

	private void writeFirstLineDataTo(ResultSetMetaData metadata,
			final HeavyTableRow row) throws SQLException {
		final int columnCount = metadata.getColumnCount();
		final List<String> columnNames = this.getColumnNames(metadata);
		final TableCellWalker walker = row.getWalker();
		for (int j = 0; j <= columnCount - 1; j++) {
			walker.nextCell();
			final String name = columnNames.get(j);
			walker.setStringValue(name);
			walker.setStyle(this.headCellStyle);
		}
	}

	private void writeMaybeLastLineDataTo(final int columnCount,
			final Table table, final int rowCount) {
		HeavyTableRow row;
		if (rowCount == 0) {// no data row
			row = table.nextRow();
			final TableCellWalker walker = row.getWalker();
			for (int j = 0; j <= columnCount - 1; j++) {
				walker.nextCell();
				walker.setStringValue("");
			}
		} else if (rowCount > this.max) {
			row = table.nextRow();
			final TableCellWalker walker = row.getWalker();
			for (int j = 0; j <= columnCount - 1; j++) {
				walker.nextCell();
				walker.setStringValue(String.format("... (%d rows remaining)",
						rowCount - this.max));
			}
		}
	}

}