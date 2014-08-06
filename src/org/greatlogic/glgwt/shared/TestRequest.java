package org.greatlogic.glgwt.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;

public class TestRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long                  serialVersionUID = 1122L;

private ArrayList<GLDBUpdate>              _dbUpdateList;
private TreeMap<IGLTable, TreeSet<String>> _deletedKeyValueMap;
private int                                _numberOfValues;
private EGLServiceRequestType        _remoteServiceRequestType;
private String                             _sessionToken;
private String                             _tableName;
private ArrayList<String>                  _tableNameList;
private String                             _xmlString;
//--------------------------------------------------------------------------------------------------
/**
 * Do not use this constructor ... it is here just so that objects can be passed to the server using
 * GWT RPC.
 */
public TestRequest() {

}
//--------------------------------------------------------------------------------------------------
public TestRequest(final String sessionToken,
                   final EGLServiceRequestType remoteServiceRequestType) {
  _sessionToken = sessionToken;
  _remoteServiceRequestType = remoteServiceRequestType;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<GLDBUpdate> getDBUpdateList() {
  return _dbUpdateList;
}
//--------------------------------------------------------------------------------------------------
public TreeMap<IGLTable, TreeSet<String>> getDeletedKeyValueMap() {
  return _deletedKeyValueMap;
}
//--------------------------------------------------------------------------------------------------
public int getNumberOfValues() {
  return _numberOfValues;
}
//--------------------------------------------------------------------------------------------------
public EGLServiceRequestType getRemoteServiceRequestType() {
  return _remoteServiceRequestType;
}
//--------------------------------------------------------------------------------------------------
public String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
public String getTableName() {
  return _tableName;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getTableNameList() {
  return _tableNameList;
}
//--------------------------------------------------------------------------------------------------
public String getXMLString() {
  return _xmlString;
}
//--------------------------------------------------------------------------------------------------
public void setDBUpdateList(final ArrayList<GLDBUpdate> dbUpdateList) {
  _dbUpdateList = dbUpdateList;
}
//--------------------------------------------------------------------------------------------------
public void setDeletedKeyValueMap(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap) {
  _deletedKeyValueMap = deletedKeyValueMap;
}
//--------------------------------------------------------------------------------------------------
public void setNumberOfValues(final int numberOfValues) {
  _numberOfValues = numberOfValues;
}
//--------------------------------------------------------------------------------------------------
public void setTableName(final String tableName) {
  _tableName = tableName;
}
//--------------------------------------------------------------------------------------------------
public void setTableNameList(final ArrayList<String> tableNameList) {
  _tableNameList = tableNameList;
}
//--------------------------------------------------------------------------------------------------
public void setXMLString(final String xmlString) {
  _xmlString = xmlString;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "Type:" + _remoteServiceRequestType + "Token:" + _sessionToken;
}
//--------------------------------------------------------------------------------------------------
}