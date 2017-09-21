package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationType;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardServerRestriction;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing switchyard quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(state = ServerRequirementState.PRESENT)
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class QuickstartsSwitchYardTest extends QuickstartsTest {

	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new SwitchYardServerRestriction(ServerImplementationType.ANY);
	}

	public QuickstartsSwitchYardTest() {
		super("quickstarts/switchyard");
	}

	@Test
	public void beanServiceTest() {
		testQuickstart("bean-service");
	}

	@Test
	public void bpelServiceTest() {
		testQuickstart("bpel-service/jms_binding");
		deleteAllProjects();
		testQuickstart("bpel-service/loan_approval");
		deleteAllProjects();
		testQuickstart("bpel-service/say_hello");
		deleteAllProjects();
		testQuickstart("bpel-service/simple_correlation");
		deleteAllProjects();
	}

	@Test
	public void bpmServiceTest() {
		testQuickstart("bpm-service");
	}

	@Test
	public void camelBindingTest() {
		testQuickstart("camel-binding");
	}

	@Test
	public void camelFtpBindingTest() {
		testQuickstart("camel-ftp-binding");
	}

	@Test
	public void camelJaxbTest() {
		testQuickstart("camel-jaxb");
	}

	@Test
	public void camelJmsBindingTest() {
		testQuickstart("camel-jms-binding");
	}

	@Test
	public void camelJpaBindingTest() {
		testQuickstart("camel-jpa-binding");
	}

	@Test
	public void camelMailTest() {
		testQuickstart("camel-mail-binding");
	}

	@Test
	public void camelNettyBindingTest() {
		testQuickstart("camel-netty-binding");
	}

	@Test
	public void camelQuartzBindingTest() {
		testQuickstart("camel-quartz-binding");
	}

	@Test
	public void camelServiceBindingTest() {
		testQuickstart("camel-service");
	}

	@Test
	public void camelSoapProxyTest() {
		testQuickstart("camel-soap-proxy");
	}

	@Test
	public void camelSqlBindingTest() {
		testQuickstart("camel-sql-binding");
	}

	@Test
	public void camelEarDeploymentTest() {
		testQuickstart("ear-deployment");
	}

	@Test
	public void httpBindingTest() {
		testQuickstart("http-binding");
	}

	@Test
	public void jcaInflowHornetqTest() {
		testQuickstart("jca-inflow-hornetq");
	}

	@Test
	public void jcaOutboundHornetqTest() {
		testQuickstart("jca-outbound-hornetq");
	}

	@Test
	public void remoteInvokerTest() {
		testQuickstart("remote-invoker");
	}

	@Test
	public void restBindingTest() {
		testQuickstart("rest-binding");
	}

	@Test
	public void rulesCamelCbrTest() {
		testQuickstart("rules-camel-cbr");
	}

	@Test
	public void rulesInterviewTest() {
		testQuickstart("rules-interview");
	}

	@Test
	public void rulesInterviewContainerTest() {
		testQuickstart("rules-interview-container");
	}

	@Test
	public void rulesInterviewDtableTest() {
		testQuickstart("rules-interview-dtable");
	}

	@Test
	public void soapAddressingTest() {
		testQuickstart("soap-addressing");
	}

	@Test
	public void soapAttachmentTest() {
		testQuickstart("soap-attachment");
	}

	@Test
	public void soapBindingRpcTest() {
		testQuickstart("soap-binding-rpc");
	}

	@Test
	public void soapMtomTest() {
		testQuickstart("soap-mtom");
	}

	@Test
	public void transformJaxbTest() {
		testQuickstart("transform-jaxb");
	}

	@Test
	public void transformJsonTest() {
		testQuickstart("transform-json");
	}

	@Test
	public void transformSmooksTest() {
		testQuickstart("transform-smooks");
	}

	@Test
	public void transformXsltTest() {
		testQuickstart("transform-xslt");
	}

	@Test
	public void validateXmlTest() {
		testQuickstart("validate-xml");
	}

}
