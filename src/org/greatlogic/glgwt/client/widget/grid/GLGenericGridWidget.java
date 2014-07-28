package org.greatlogic.glgwt.client.widget.grid;
/*
 * Copyright 2006-2014 Andy King (GreatLogic.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLGenericGridWidget extends GLGridWidget {
//--------------------------------------------------------------------------------------------------
private static final IGLColumn[] EmptyColumnArray = new IGLColumn[0];
//--------------------------------------------------------------------------------------------------
public GLGenericGridWidget(final IGLTable table) {
  super(null, "There are no rows in the " + table + " table", null, false, true, true,
        table.getColumns().toArray(EmptyColumnArray));
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addFilters() {
  for (final IGLColumn column : _columns) {
    addFilter(column);
  }
}
//--------------------------------------------------------------------------------------------------
}