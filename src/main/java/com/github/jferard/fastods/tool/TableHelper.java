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

import java.util.Calendar;

import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCell.Type;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.PositionUtil.Position;

public class TableHelper {
	private Util util;

	public TableHelper(Util util) {
		this.util = util;
	}

	/**
	 * Sets the cell value in a table to the date from the Calendar object.
	 *
	 * @param rowIndex
	 *            The row, 0 is the first row
	 * @param col
	 *            The column, 0 is the first column
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final Table table, final int rowIndex,
			final int colIndex, final Calendar cal, final TableCellStyle ts)
			throws FastOdsException {
		final TableCell cell = getCell(table, rowIndex, colIndex);
		cell.setDateValue(cal);
		cell.setStyle(ts);
	}

	private TableCellWalker getCell(final Table table, final int rowIndex,
			final int colIndex) throws FastOdsException {
		final HeavyTableRow row = table.getRow(rowIndex);
		final TableCellWalker walker = row.getWalker();
		walker.to(colIndex);
		return walker;
	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cells, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final Table table, final String pos, final Calendar cal,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCell(table, row, col, cal, ts);
	}

	/**
	 * Sets the cell value in all tables to the given values.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param valuetype
	 *            The value type of value,
	 *            OldHeavyTableCell.Type.STRING,OldHeavyTableCell.Type.FLOAT or
	 *            OldHeavyTableCell.Type.PERCENTAGE.
	 * @param value
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final Table table, final String pos,
			final TableCell.Type valuetype, final String value,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCell(table, row, col, valuetype, value, ts);
	}

	/** TODO: do the code */
	public void setCell(Table table, int rowIndex, int colIndex, Type valuetype,
			String value, TableCellStyle ts) throws FastOdsException {
		throw new FastOdsException("not implemented yet");
//		final TableCell cell = getCell(table, rowIndex, colIndex);
//		cell.setStyle(ts);
//		switch (valuetype) {
//		case BOOLEAN: break;
//		case CURRENCY: break;
//		case DATE: break;
//		case FLOAT: break;
//		case PERCENTAGE: break;
//		case STRING: break;
//		case TIME: break;
//		case VOID: break;
//		default:
//			break;
//		}
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param rowIndex
	 *            The row, 0 is the first row
	 * @param colIndex
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 * @throws FastOdsException
	 */
	public void setCellMerge(final Table table, final int rowIndex, final int colIndex,
			final int rowMerge, final int columnMerge) throws FastOdsException {
			final TableCellWalker walker = getCell(table, rowIndex, colIndex);
			walker.setRowsSpanned(rowMerge);
			walker.setColumnsSpanned(columnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell in all existing tables.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param rowMerge
	 * @param columnMerge
	 * @throws FastOdsException
	 */
	public void setCellMerge(final Table table, final String pos, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellMerge(table, row, col, rowMerge, columnMerge);
	}
}