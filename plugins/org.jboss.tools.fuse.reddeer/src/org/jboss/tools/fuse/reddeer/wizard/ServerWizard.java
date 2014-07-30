package org.jboss.tools.fuse.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Wizard for creating a Fuse server.
 * 
 * @author tsedmik
 * 
 */
public class ServerWizard extends NewWizardDialog {

	private static final String SERVER_SECTION = "JBoss Fuse";
	private static final String HOST_NAME = "Server's host name:";
	private static final String NAME = "Server name:";
	private static final String PORT_NUMBER = "SSH Port: ";
	private static final String USER_NAME = "User Name:";
	private static final String PASSWORD = "Password: ";

	private String type;
	private String name;
	private String hostName;
	private String portNumber;
	private String userName;
	private String password;

	public ServerWizard() {
		super("Server", "Server");
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void execute() {
		open();

		new DefaultTreeItem(SERVER_SECTION, type).select();
		if (name != null) {
			new LabeledText(NAME).setText(name);
		}
		if (hostName != null) {
			new LabeledText(HOST_NAME).setText(hostName);
		}

		next();
		closeSecureStorage();

		if (portNumber != null) {
			new LabeledText(PORT_NUMBER).setText(portNumber);
		}
		if (userName != null) {
			new LabeledText(USER_NAME).setText(userName);
		}
		if (password != null) {
			new LabeledText(PASSWORD).setText(password);
		}

		finish();
	}

	/**
	 * Tries to close 'Secure Storage' dialog window
	 */
	private static void closeSecureStorage() {

		try {
			new WaitUntil(new ShellWithTextIsAvailable("Secure Storage"), TimePeriod.getCustom(5));
		} catch (RuntimeException ex) {
			return;
		}
		new DefaultShell("Secure Storage");
		new LabeledText("Password:").setText("admin");
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell("New Server");
	}
}