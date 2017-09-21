package org.jboss.tools.runtime.reddeer.requirement;

import org.eclipse.reddeer.junit.requirement.configuration.RequirementConfiguration;
import org.jboss.tools.runtime.reddeer.ServerBase;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServerConfiguration implements RequirementConfiguration {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private ServerBase server;

	public void setServer(ServerBase server) {
		this.server = server;
	}

	public ServerBase getServer() {
		if (server != null) {
			server.setName(name);
		}
		return server;
	}

	@Override
	public String getId() {
		if (getName() != null) {
			return getName();
		}
		return getServer().getName();
	}
}