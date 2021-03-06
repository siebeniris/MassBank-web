/*******************************************************************************
 *
 * Copyright (C) 2008 JST-BIRD MassBank
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *******************************************************************************
 *
 * Data Model（Leaf）クラス
 *
 * ver 2.0.1 2008.12.05
 *
 ******************************************************************************/

/**
 * Data Model（Leaf）クラス
 */
@SuppressWarnings("serial")
public class LeafNode extends MyTreeNode {
	
	public String acc;
	public String name;

	public LeafNode(String x, String n, String a, String name) {
		id = x;
		acc = a;
		this.name = name;
		setUserObject(n);
	}
}
