package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class SmooksXML2POJO extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Smooks XML->POJO Example";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"transform_XML2POJO","transform_XML2POJO_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformxml2pojo.test","SendJMSMessage.java");
		bot.sleep(Timing.time3S());
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Order Items"));
		text = executeClient(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformxml2pojo.test","ReceiveJMSMessage.java");
		SWTTestExt.servers.removeAllProjectsFromServer();
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Order Items"));
	}
}
