package org.greatlogic.glgwt.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class TestResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;

private int               _nextId;
private ArrayList<String> _selectResultList;
private ArrayList<String> _tableMetadataList;
//--------------------------------------------------------------------------------------------------
public int getNextId() {
  return _nextId;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getSelectResultList() {
  return _selectResultList;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getTableMetadataList() {
  return _tableMetadataList;
}
//--------------------------------------------------------------------------------------------------
public void setNextId(final int nextId) {
  _nextId = nextId;
}
//--------------------------------------------------------------------------------------------------
public void setSelectResultList(final ArrayList<String> selectResultList) {
  _selectResultList = selectResultList;
}
//--------------------------------------------------------------------------------------------------
public void setTableMetadataList(final ArrayList<String> tableMetadataList) {
  _tableMetadataList = tableMetadataList;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return super.toString();
}
//--------------------------------------------------------------------------------------------------
}