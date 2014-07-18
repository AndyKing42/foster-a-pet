package org.greatlogic.glgwt.shared;

import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
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
public interface IGLColumn {
//--------------------------------------------------------------------------------------------------
public int getComboboxSeq();
public EGLColumnDataType getDataType();
public int getDefaultGridColumnWidth();
public Object getDefaultValue();
public IGLLookupType getLookupType();
public boolean getNullable();
public int getNumberOfDecimalPlaces();
public int getPrimaryKeySeq();
public String getTitle();
//--------------------------------------------------------------------------------------------------
}