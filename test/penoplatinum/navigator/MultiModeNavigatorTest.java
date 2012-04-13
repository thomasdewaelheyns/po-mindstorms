package penoplatinum.navigator;

/**
 * MultiModeNavigatorTest
 * 
 * Tests MultiModeNavigator
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

import penoplatinum.driver.Driver;

import penoplatinum.model.Model;

import penoplatinum.navigator.action.NavigatorAction;
import penoplatinum.navigator.mode.NavigatorMode;


public class MultiModeNavigatorTest extends TestCase {

  private MultiModeNavigator    navigator;
  private NavigatorMode         mockedMode1,
                                mockedMode2;
  private Model                 mockedModel;
  private List<NavigatorAction> plan1,
                                plan2;


  public MultiModeNavigatorTest(String name) { 
    super(name);
  }

  public void testRequiresAtLeastAMode() {
    this.setupEmptyNavigator();
    try {
      this.navigator.reachedGoal();
      fail( "MultiMode requires at least one Mode" );
    } catch(Exception e) {}
	}

  public void testTwoModesInSequence() {
    this.setupNavigatorWithTwoModes();
    Driver mockedDriver = this.mockDriver();

    when(mockedDriver.completedLastInstruction()).thenReturn(false, true);    
    when(this.mockedMode1.reachedGoal()).thenReturn(false, false, false, false,
                                                    false, false, true);
    when(this.mockedMode2.reachedGoal()).thenReturn(false, false, true);
        
    // first action of first plan of first mode
    this.navigator.instruct(mockedDriver);
    verify(mockedDriver).completedLastInstruction(); // false
    verify(this.mockedMode1, times(2)).reachedGoal();
    verify(this.mockedMode2, never()).reachedGoal();
    verify(this.mockedMode1).createNewPlan();
    verify(this.plan1.get(0)).instruct(mockedDriver); // action 1/1 instruct
    verify(this.plan1.get(1), never()).instruct(mockedDriver);
    verify(this.plan2.get(0), never()).instruct(mockedDriver);
    verify(this.plan2.get(1), never()).instruct(mockedDriver);

    assertFalse(this.navigator.reachedGoal());

    // second action of first plan of first mode
    this.navigator.instruct(mockedDriver);
    verify(mockedDriver, times(2)).completedLastInstruction(); // true
    verify(this.mockedMode1, times(4)).reachedGoal();
    verify(this.mockedMode2, never()).reachedGoal();
    verify(this.mockedMode1).createNewPlan();
    verify(this.plan1.get(0)).instruct(mockedDriver); // action 2/1 instruct
    assertEquals(1, this.plan1.size()); // first action is consumed
    verify(this.plan2.get(0), never()).instruct(mockedDriver);
    verify(this.plan2.get(1), never()).instruct(mockedDriver);


    assertFalse(this.navigator.reachedGoal());

    // first action of first plan of second mode
    this.navigator.instruct(mockedDriver);
    verify(mockedDriver, times(3)).completedLastInstruction(); // true
    verify(this.mockedMode1, times(7)).reachedGoal();
    verify(this.mockedMode2, never()).reachedGoal();
    verify(this.mockedMode2).createNewPlan();
    assertEquals(0, this.plan1.size()); // first plan is consumed
    verify(this.plan2.get(0)).instruct(mockedDriver); // action 1/2 instruct
    verify(this.plan2.get(1), never()).instruct(mockedDriver);

    assertFalse(this.navigator.reachedGoal());

    // second action of first plan of second mode
    this.navigator.instruct(mockedDriver);
    verify(mockedDriver, times(4)).completedLastInstruction(); // true
    verify(this.mockedMode2, times(2)).reachedGoal();
    verify(this.plan2.get(0)).instruct(mockedDriver); // action 2/2 instruct
    assertEquals(1, this.plan2.size()); // first action is consumed

    assertTrue(this.navigator.reachedGoal());
  }
  
  // TODO: multiple plans by the same mode
  // TODO: discarting partial plans due to not completedLastInstruction

  private void setupEmptyNavigator() {
	  this.navigator = new MultiModeNavigator();
  }
  
  private void setupNavigatorWithTwoModes() {
    this.mockedMode1 = mock(NavigatorMode.class);
    this.mockedMode2 = mock(NavigatorMode.class);
    
    this.navigator = new MultiModeNavigator() {
      // as soon as we get a Model, we can setup our Modes
      public Navigator useModel(Model model) {
        this.firstUse(mockedMode1);
        this.thenUse (mockedMode2);
        return this;
      }
    };
    
    this.mockedModel = mock(Model.class);
    this.navigator.useModel(this.mockedModel);

    this.plan1 = new ArrayList<NavigatorAction>();
    plan1.add(mock(NavigatorAction.class));
    plan1.add(mock(NavigatorAction.class));
    this.plan2 = new ArrayList<NavigatorAction>();
    plan2.add(mock(NavigatorAction.class));
    plan2.add(mock(NavigatorAction.class));
    
    when(this.mockedMode1.createNewPlan()).thenReturn(this.plan1);
    when(this.mockedMode2.createNewPlan()).thenReturn(this.plan2);
  }
  
  private Driver mockDriver() {
    return mock(Driver.class);
  }
}
