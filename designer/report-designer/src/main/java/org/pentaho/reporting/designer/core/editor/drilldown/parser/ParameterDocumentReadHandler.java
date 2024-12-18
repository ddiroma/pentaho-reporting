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


package org.pentaho.reporting.designer.core.editor.drilldown.parser;

import org.pentaho.reporting.designer.core.editor.drilldown.model.Parameter;
import org.pentaho.reporting.designer.core.editor.drilldown.model.ParameterDocument;
import org.pentaho.reporting.designer.core.editor.drilldown.model.ParameterGroup;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Todo: Document me!
 * <p/>
 * Date: 13.08.2010 Time: 17:27:37
 *
 * @author Thomas Morgner.
 */
public class ParameterDocumentReadHandler extends AbstractXmlReadHandler {
  private ParameterDocument parameterDefinition;
  private ArrayList parameters;

  public ParameterDocumentReadHandler() {
    parameters = new ArrayList();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    parameterDefinition = new ParameterDocument();
    parameterDefinition.setPromptNeeded( "true".equals( attrs.getValue( getUri(), "is-prompt-needed" ) ) ); // NON-NLS
    parameterDefinition.setPaginate( "true".equals( attrs.getValue( getUri(), "paginate" ) ) );// NON-NLS
    parameterDefinition.setSubscribe( "true".equals( attrs.getValue( getUri(), "subscribe" ) ) );// NON-NLS
    parameterDefinition
      .setShowParameterUi( "true".equals( attrs.getValue( getUri(), "show-parameter-ui" ) ) );// NON-NLS
    parameterDefinition.setLayout( attrs.getValue( getUri(), "layout" ) );// NON-NLS
    final String autoSubmit = attrs.getValue( getUri(), "autoSubmit" );
    if ( "true".equals( autoSubmit ) ) {
      parameterDefinition.setAutoSubmit( Boolean.TRUE );
    } else if ( "false".equals( autoSubmit ) ) {
      parameterDefinition.setAutoSubmit( Boolean.FALSE );
    } else {
      parameterDefinition.setAutoSubmit( null );
    }

    parameterDefinition.setAutoSubmitUI( "true".equals( attrs.getValue( getUri(), "autoSubmitUI" ) ) );// NON-NLS

  }

  /**
   * Returns the handler for a child element.
   *
   * @param uri     the URI of the namespace of the current element.
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri,
                                               final String tagName,
                                               final Attributes atts ) throws SAXException {
    if ( isSameNamespace( uri ) == false ) {
      return null;
    }
    if ( "errors".equals( tagName ) )//NON-NLS
    {
      return new IgnoreAnyChildReadHandler();
    }
    if ( "parameter".equals( tagName ) )//NON-NLS
    {
      final ParameterReadHandler readHandler = new ParameterReadHandler();
      parameters.add( readHandler );
      return readHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException {
    for ( int i = 0; i < parameters.size(); i++ ) {
      final ParameterReadHandler readHandler = (ParameterReadHandler) parameters.get( i );
      final Parameter parameter = readHandler.getParameter();
      String parameterGroupName = parameter.getAttribute( "parameter-group" ); //$NON-NLS-1$
      if ( StringUtils.isEmpty( parameterGroupName ) ) {
        // default group
        parameterGroupName = "parameters"; //$NON-NLS-1$
      }

      ParameterGroup parameterGroup = parameterDefinition.getParameterGroup( parameterGroupName );
      if ( parameterGroup == null ) {
        final String parameterGroupLabel = parameter.getAttribute( "parameter-group-label" ); //$NON-NLS-1$
        parameterGroup = new ParameterGroup( parameterGroupName, parameterGroupLabel );
        parameterDefinition.addParameterGroup( parameterGroup );
      }
      parameterGroup.addParameter( parameter );
    }


  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws org.xml.sax.SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return parameterDefinition;
  }
}
