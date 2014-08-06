package org.fosterapet.server;
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
import org.fosterapet.shared.IRemoteService;
import org.greatlogic.glgwt.server.GLRemoteServiceServlet;
import com.greatlogic.glbase.gllib.GLLog;

@SuppressWarnings("serial")
public class FAPRemoteServiceServlet extends GLRemoteServiceServlet implements IRemoteService {
//--------------------------------------------------------------------------------------------------
@Override
public String loadTestData(final String connectionInfo, final String testDataOptionString) {
  try {
    DBTestData.processRequest(testDataOptionString);
  }
  catch (final Exception e) {
    GLLog.major("Error loading test data", e);
  }
  return "";
}
//--------------------------------------------------------------------------------------------------
}