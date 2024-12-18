/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.engine.classic.extensions.datasources.cda.writer;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ParameterMapping;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.PasswordEncryptionService;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.DataFactoryWriteHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterContext;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterException;
import org.pentaho.reporting.engine.classic.extensions.datasources.cda.CdaDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.cda.CdaModule;
import org.pentaho.reporting.engine.classic.extensions.datasources.cda.CdaQueryEntry;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;

import java.io.IOException;

public class CdaDataFactoryWriteHandler implements DataFactoryWriteHandler {
  public CdaDataFactoryWriteHandler() {
  }

  /**
   * Writes a data-source into a XML-stream.
   *
   * @param reportWriter   the writer context that holds all factories.
   * @param xmlWriter      the XML writer that will receive the generated XML data.
   * @param rawDataFactory the data factory that should be written.
   * @throws IOException           if any error occured
   * @throws ReportWriterException if the data factory cannot be written.
   */
  public void write( final ReportWriterContext reportWriter,
                     final XmlWriter xmlWriter,
                     final DataFactory rawDataFactory )
    throws IOException, ReportWriterException {
    final CdaDataFactory dataFactory = (CdaDataFactory) rawDataFactory;

    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration( "data", CdaModule.NAMESPACE );

    xmlWriter.writeTag( CdaModule.NAMESPACE, "cda-datasource", rootAttrs, XmlWriter.OPEN );

    final AttributeList configAttrs = new AttributeList();
    if ( StringUtils.isEmpty( dataFactory.getBaseUrl() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "base-url", dataFactory.getBaseUrl() );
    }
    if ( StringUtils.isEmpty( dataFactory.getBaseUrlField() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "base-url-field", dataFactory.getBaseUrlField() );
    }
    if ( StringUtils.isEmpty( dataFactory.getSolution() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "solution", dataFactory.getSolution() );
    }
    if ( StringUtils.isEmpty( dataFactory.getPath() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "path", dataFactory.getPath() );
    }
    if ( StringUtils.isEmpty( dataFactory.getFile() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "file", dataFactory.getFile() );
    }
    if ( StringUtils.isEmpty( dataFactory.getUsername() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE, "username", dataFactory.getUsername() );
    }
    if ( StringUtils.isEmpty( dataFactory.getPassword() ) == false ) {
      configAttrs.setAttribute( CdaModule.NAMESPACE,
        "password", PasswordEncryptionService.getInstance().encrypt( dataFactory.getPassword() ) );
    }

    configAttrs.setAttribute( CdaModule.NAMESPACE, "use-local-call", String.valueOf( dataFactory.isUseLocalCall() ) );
    configAttrs.setAttribute( CdaModule.NAMESPACE, "is-sugar-mode", String.valueOf( dataFactory.isSugarMode() ) );

    xmlWriter.writeTag( CdaModule.NAMESPACE, "config", configAttrs, XmlWriterSupport.CLOSE );

    final String[] queryNames = dataFactory.getQueryNames();
    for ( int i = 0; i < queryNames.length; i++ ) {
      final String queryName = queryNames[ i ];
      final CdaQueryEntry query = dataFactory.getQueryEntry( queryName );
      final AttributeList queryAttr = new AttributeList();
      queryAttr.setAttribute( CdaModule.NAMESPACE, "query", query.getId() );
      queryAttr.setAttribute( CdaModule.NAMESPACE, "name", query.getName() );
      xmlWriter.writeTag( CdaModule.NAMESPACE, "query", queryAttr, XmlWriterSupport.OPEN );

      final ParameterMapping[] parameterMappings = query.getParameters();
      for ( int j = 0; j < parameterMappings.length; j++ ) {
        final ParameterMapping parameterMapping = parameterMappings[ j ];
        final AttributeList paramAttr = new AttributeList();
        paramAttr.setAttribute( CdaModule.NAMESPACE, "datarow-name", parameterMapping.getName() );
        paramAttr.setAttribute( CdaModule.NAMESPACE, "variable-name", parameterMapping.getAlias() );
        xmlWriter.writeTag( CdaModule.NAMESPACE, "variable", paramAttr, XmlWriterSupport.CLOSE );
      }

      xmlWriter.writeCloseTag();
    }
  }
}
