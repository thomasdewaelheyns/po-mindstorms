package penoplatinum.ui.admin;

/**
 * RobotAdminShellTest
 * 
 * Tests RobotAdminShell class
 * 
 * @author: Team Platinum
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.gateway.Queue;


public class RobotAdminShellTest extends TestCase {

  private RobotAdminShell  shell;
  private BufferedReader   mockedInput;
  private BufferedWriter   mockedOutput;
  private RobotAdminClient mockedClient;


  public RobotAdminShellTest(String name) { 
    super(name);
  }

  public void testExit() {
    this.setup();
    try {
      when(this.mockedInput.readLine()).thenReturn("exit");
      this.shell.run();
      verify(this.mockedOutput).write( "rash> ", 0, 6 );
    } catch(Exception e) {
      fail( "Unexpected exception..." + e );
    }
  }

  public void testPrompt() {
    this.setup();
    this.shell.setPrompt( "unit" );
    try {
      when(this.mockedInput.readLine()).thenReturn("exit");
      this.shell.run();
      verify(this.mockedOutput).write( "unit> ", 0, 6 );
      verifyZeroInteractions(this.mockedClient);
    } catch(Exception e) {
      fail( "Unexpected exception..." + e );
    }
  }

  public void testHandleUnknownShellCommandByPassingToClient() {
    this.setup();
    try {
      when(this.mockedInput.readLine()).thenReturn("beebabelooba", "exit");
      this.shell.run();
      verify(this.mockedOutput, times(2)).write( "rash> ", 0, 6 );
      verify(this.mockedClient).handleCommand("beebabelooba");
      verifyNoMoreInteractions(this.mockedClient);
    } catch(Exception e) {
      fail( "Unexpected exception..." + e );
    }
  }

  public void testDontDoAnythingOnEmptyInput() {
    this.setup();
    try {
      when(this.mockedInput.readLine()).thenReturn("", "exit");
      this.shell.run();
      verify(this.mockedOutput, times(2)).write( "rash> ", 0, 6 );
      verifyZeroInteractions(this.mockedClient);
    } catch(Exception e) {
      fail( "Unexpected exception..." + e );
    }
  }
  
  public void testLoadingOfFile() {
    this.setup();
    try {
      when(this.mockedInput.readLine()).thenReturn("load test.log", "exit");
      this.shell.run();
      verify(this.mockedOutput, times(2)).write( "rash> ", 0, 6 );
      verify(this.mockedClient).handleCommand("command1 arg1");
      verify(this.mockedClient).handleCommand("command2 arg2");
      verifyNoMoreInteractions(this.mockedClient);
    } catch(Exception e) {
      fail( "Unexpected exception..." + e );
    }
  }

  // construction helpers
  
  private void setup() {
    this.createShell();
    this.mockInput();
    this.mockOutput();
    this.mockClient();
    
    this.shell.useInput(this.mockedInput)
              .useOutput(this.mockedOutput)
              .useClient(this.mockedClient);
  }
  
  private void createShell() {
    this.shell = new RobotAdminShell();
  }
  
  private void mockInput() {
    this.mockedInput = mock(BufferedReader.class);
  }

  private void mockOutput() {
    this.mockedOutput = mock(BufferedWriter.class);
  }
  
  private void mockClient() {
    this.mockedClient = mock(RobotAdminClient.class);
  }
}
