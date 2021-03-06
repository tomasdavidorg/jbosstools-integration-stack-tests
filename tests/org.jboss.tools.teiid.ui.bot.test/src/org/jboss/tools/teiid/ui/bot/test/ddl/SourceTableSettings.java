package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class SourceTableSettings {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "SourceTableSettings";
	private static final String NAME_SOURCE_MODEL = "TableSettingsSourceModel";
	private static final String NAME_VDB = "TableSettingsVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "TableSettingsVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		/* data source is needed when exported VDB will be tested to deploy on the server */
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void importDdlTest(){
		ddlHelper.importDdlFromSource(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);		
		checkImportedModel();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);		
		checkImportedModel();
		ddlHelper.checkDeploy(NAME_SOURCE_MODEL, null, WORK_PROJECT_NAME, NAME_VDB, teiidServer);		
	}
	
	
	private void checkImportedModel(){
		new ModelExplorer().selectItem(WORK_PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi", "myTable");

		PropertiesView propertiesView = new PropertiesView();
		collector.checkThat("Wrong cardinality for Table",
				propertiesView.getProperty("Misc", "Cardinality").getPropertyValue(), is("120"));
		collector.checkThat("Materialized is false(should be true)",
				propertiesView.getProperty("Misc", "Materialized").getPropertyValue(), is("true"));		
		collector.checkThat("Materialized table is not set correctly",
				propertiesView.getProperty("Misc", "Materialized Table").getPropertyValue(), new StringContains("helpTable"));		
		collector.checkThat("Table name in source is badly set",
				propertiesView.getProperty("Misc", "Name In Source").getPropertyValue(), is("myTableSource"));
		collector.checkThat("Supports Update is badly set",
				propertiesView.getProperty("Misc", "Supports Update").getPropertyValue(), is("true"));
		
		RelationalModelEditor editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
		editor.close();
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi");	
		editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
	    
		// check table description
		tableEditor.openTab(TableEditor.Tabs.BASE_TABLES);
		collector.checkThat("Description is not set correctly", tableEditor.getCellText(1,"myTable", "Description"),
	    		is("This is Table description"));

		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());		
	}
	
	@Test
	public void exportVdbTest(){
		ddlHelper.createStaticVdb(NAME_VDB, PROJECT_NAME, NAME_SOURCE_MODEL);		
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	@Test
	public void exportDdlTest(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("missing name in source for table", contentFile, new StringContains("NAMEINSOURCE 'myTableSource'"));
		collector.checkThat("missing materialized checkbox on true", contentFile, new StringContains("MATERIALIZED 'TRUE'"));
		collector.checkThat("missing updatable on true", contentFile, new StringContains("UPDATABLE 'TRUE'"));
		collector.checkThat("missing cardinality", contentFile, new StringContains("CARDINALITY '120'"));
		collector.checkThat("missing materialized table", contentFile, new StringContains("MATERIALIZED_TABLE 'TableSettingsSourceModel.helpTable'"));
		collector.checkThat("missing table description", contentFile, new StringContains("ANNOTATION 'This is Table description'"));		
	}
}

