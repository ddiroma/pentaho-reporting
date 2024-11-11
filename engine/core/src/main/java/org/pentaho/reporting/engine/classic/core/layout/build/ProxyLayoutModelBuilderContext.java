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


package org.pentaho.reporting.engine.classic.core.layout.build;

import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.states.ReportStateKey;

public class ProxyLayoutModelBuilderContext implements LayoutModelBuilderContext {
  private LayoutModelBuilderContext context;

  public ProxyLayoutModelBuilderContext( final LayoutModelBuilderContext context ) {
    this.context = context;
  }

  public RenderBox getRenderBox() {
    return context.getRenderBox();
  }

  public void addChild( final RenderBox child ) {
    context.addChild( child );
  }

  public void removeChild( final RenderBox child ) {
    context.removeChild( child );
  }

  public LayoutModelBuilderContext getParent() {
    return context;
  }

  public boolean isEmpty() {
    return context.isEmpty();
  }

  public void setEmpty( final boolean empty ) {

  }

  public boolean isKeepWrapperBoxAlive() {
    return context.isKeepWrapperBoxAlive();
  }

  public void setKeepWrapperBoxAlive( final boolean keepWrapperBoxAlive ) {

  }

  public boolean isAutoGeneratedWrapperBox() {
    return context.isAutoGeneratedWrapperBox();
  }

  public void setAutoGeneratedWrapperBox( final boolean autoGeneratedWrapperBox ) {

  }

  public LayoutModelBuilderContext close() {
    return context;
  }

  public Object clone() {
    try {
      return super.clone();
    } catch ( CloneNotSupportedException e ) {
      throw new IllegalStateException( e );
    }
  }

  public LayoutModelBuilderContext deriveForPagebreak() {
    final ProxyLayoutModelBuilderContext clone = (ProxyLayoutModelBuilderContext) clone();
    clone.context = context.deriveForPagebreak();
    return clone;
  }

  public LayoutModelBuilderContext deriveForStorage( final RenderBox clonedRoot ) {
    if ( clonedRoot == null ) {
      throw new NullPointerException();
    }
    final ProxyLayoutModelBuilderContext clone = (ProxyLayoutModelBuilderContext) clone();
    clone.context = context.deriveForStorage( clonedRoot );
    return clone;
  }

  public boolean mergeSection( final ReportStateKey stateKey ) {
    return context.mergeSection( stateKey );
  }

  public void validateAfterCommit() {

  }

  public void performParanoidModelCheck() {

  }

  public void restoreStateAfterRollback() {

  }

  public void commitAsEmpty() {

  }

  public int getDepth() {
    if ( context == null ) {
      return 1;
    }
    return 1 + context.getDepth();
  }
}
