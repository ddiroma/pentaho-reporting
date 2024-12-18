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


package org.pentaho.reporting.engine.classic.extensions.modules.sparklines;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.libraries.libsparklines.BarGraphDrawable;
import org.pentaho.reporting.libraries.libsparklines.LineGraphDrawable;
import org.pentaho.reporting.libraries.libsparklines.PieGraphDrawable;

public class SparklineExpressionTest {

  private static final String RAW_PARAM = "rawDataField";

  private SparklineExpression expression;
  private DataRow dataRow;

  @Before
  public void setUp() {
    expression = spy( new SparklineExpression() );

    dataRow = mock( DataRow.class );
    doReturn( dataRow ).when( expression ).getDataRow();
    doReturn( null ).when( dataRow ).get( RAW_PARAM );

    expression.setBackgroundColor( Color.YELLOW );
    expression.setColor( Color.BLUE );
    expression.setHighColor( Color.BLACK );
    expression.setLowColor( Color.RED );
    expression.setMediumColor( Color.CYAN );
    expression.setLastColor( Color.GREEN );
    expression.setSpacing( 14 );
    expression.setCounterClockWise( true );
    expression.setStartAngle( 10 );
  }

  @Test
  public void testGetValueByDefault() {
    Object obj = expression.getValue();
    assertThat( obj, is( instanceOf( BarGraphDrawable.class ) ) );

    expression.setType( null );
    obj = expression.getValue();
    assertThat( obj, is( nullValue() ) );
  }

  @Test
  public void testGetValueLine() {
    expression.setType( "line" );

    Object obj = expression.getValue();

    assertThat( obj, is( instanceOf( LineGraphDrawable.class ) ) );
    LineGraphDrawable drawable = (LineGraphDrawable) obj;
    assertThat( drawable.getData(), is( equalTo( new Number[] {} ) ) );
    assertThat( drawable.getBackground(), is( equalTo( Color.YELLOW ) ) );
    assertThat( drawable.getColor(), is( equalTo( Color.BLUE ) ) );
    assertThat( drawable.getSpacing(), is( equalTo( 14 ) ) );
  }

  @Test
  public void testGetValueBar() {
    expression.setType( "bar" );

    Object obj = expression.getValue();

    assertThat( obj, is( instanceOf( BarGraphDrawable.class ) ) );
    BarGraphDrawable drawable = (BarGraphDrawable) obj;
    assertThat( drawable.getData(), is( equalTo( new Number[] {} ) ) );
    assertThat( drawable.getBackground(), is( equalTo( Color.YELLOW ) ) );
    assertThat( drawable.getColor(), is( equalTo( Color.BLUE ) ) );
    assertThat( drawable.getSpacing(), is( equalTo( 14 ) ) );
    assertThat( drawable.getHighColor(), is( equalTo( Color.BLACK ) ) );
    assertThat( drawable.getLastColor(), is( equalTo( Color.GREEN ) ) );
  }

  @Test
  public void testGetValuePie() {
    expression.setType( "pie" );

    Object obj = expression.getValue();
    assertThat( obj, is( nullValue() ) );

    Number[] data = new Number[] { 5 };
    doReturn( data ).when( expression ).getData();
    obj = expression.getValue();

    assertThat( obj, is( instanceOf( PieGraphDrawable.class ) ) );
    PieGraphDrawable drawable = (PieGraphDrawable) obj;
    assertThat( drawable.getValue(), is( equalTo( data[0] ) ) );
    assertThat( drawable.getBackground(), is( equalTo( Color.YELLOW ) ) );
    assertThat( drawable.getColor(), is( equalTo( Color.BLUE ) ) );
    assertThat( drawable.getHighColor(), is( equalTo( Color.BLACK ) ) );
    assertThat( drawable.getLowColor(), is( equalTo( Color.RED ) ) );
    assertThat( drawable.getMediumColor(), is( equalTo( Color.CYAN ) ) );
    assertThat( drawable.isCounterClockWise(), is( equalTo( true ) ) );
    assertThat( drawable.getStartAngle(), is( equalTo( 10 ) ) );
    assertThat( drawable.getHighSlice(), is( (Number) 1.0 ) );
    assertThat( drawable.getMediumSlice(), is( (Number) 0.7 ) );
    assertThat( drawable.getLowSlice(), is( (Number) 0.3 ) );
  }

  @Test
  public void testGetValuePieSlicer() {
    expression.setType( "pie" );
    expression.setHighSlice( 5.5 );
    expression.setMediumSlice( 3.0 );
    expression.setLowSlice( 1.1 );
    Number[] data = new Number[] { 5 };
    doReturn( data ).when( expression ).getData();

    Object obj = expression.getValue();
    assertThat( obj, is( instanceOf( PieGraphDrawable.class ) ) );

    PieGraphDrawable drawable = (PieGraphDrawable) obj;
    assertThat( drawable.getValue(), is( equalTo( data[0] ) ) );
    assertThat( drawable.getBackground(), is( equalTo( Color.YELLOW ) ) );
    assertThat( drawable.getColor(), is( equalTo( Color.BLUE ) ) );
    assertThat( drawable.getHighColor(), is( equalTo( Color.BLACK ) ) );
    assertThat( drawable.getLowColor(), is( equalTo( Color.RED ) ) );
    assertThat( drawable.getMediumColor(), is( equalTo( Color.CYAN ) ) );
    assertThat( drawable.isCounterClockWise(), is( equalTo( true ) ) );
    assertThat( drawable.getStartAngle(), is( equalTo( 10 ) ) );
    assertThat( drawable.getHighSlice(), is( equalTo( (Number) 5.5 ) ) );
    assertThat( drawable.getMediumSlice(), is( equalTo( (Number) 3.0 ) ) );
    assertThat( drawable.getLowSlice(), is( equalTo( (Number) 1.1 ) ) );
  }

  @Test
  public void testGetData() {
    Number[] result = expression.getData();
    assertThat( result, is( equalTo( new Number[] {} ) ) );

    expression.setField( new String[] { RAW_PARAM } );
    doReturn( 1.0 ).when( dataRow ).get( RAW_PARAM );
    result = expression.getData();
    assertThat( result, is( equalTo( new Number[] { 1.0 } ) ) );

    expression.setRawDataField( RAW_PARAM );
    doReturn( 1.0 ).when( dataRow ).get( RAW_PARAM );
    result = expression.getData();
    assertThat( result, is( equalTo( new Number[] { 1.0 } ) ) );
  }
}
