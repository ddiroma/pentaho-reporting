/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/


package org.pentaho.reporting.designer.core.inspections;

import org.pentaho.reporting.engine.classic.core.event.ReportModelListener;

public interface InspectionRunner extends ReportModelListener {
  void startTimer();

  void dispose();

  void addInspectionListener( InspectionResultListener listener );

  void removeInspectionListener( InspectionResultListener listener );

}
