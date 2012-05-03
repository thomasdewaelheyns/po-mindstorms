/**
 * RobotAdminShellRunner
 * 
 * Processes CLI parameters, constructs a RobotAdminShell and starts it.
 *
 * Author: Team Platinum
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import penoplatinum.Config;

import penoplatinum.ui.admin.RobotAdminClient;
import penoplatinum.ui.admin.RobotAdminShell;


public class RobotAdminShellRunner extends BaseRunner {

  private RobotAdminClient client;
  private RobotAdminShell  shell;

  private RobotAdminShellRunner(String[] args) {
    super(args, "drsmj");
    this.setupClient();
    this.setupShell();
  }
  
  private void setupClient() {
    this.client = new RobotAdminClient()
      .useQueue(this.queue);
  }

  private void setupShell() {
    this.shell = new RobotAdminShell()
      .useInput(new BufferedReader(new InputStreamReader(System.in)))
      .useOutput(new BufferedWriter(new OutputStreamWriter(System.out)))
      .useClient(this.client);
    if( this.secret != null ) {
      this.client.authenticateWith(this.robotName, this.secret);
      this.shell.setPrompt(this.robotName);
    }
  }
  
  private void start() {
    System.out.println("--- Starting shell.");
    this.shell.run();
  }
  
  public static void main(String[] args) {
    new RobotAdminShellRunner(args).start();
  }
}
