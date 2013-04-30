/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2011 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.DocViewer.client.controller;

//import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.vangent.hieos.DocViewer.client.model.authentication.AuthenticationContext;
import com.vangent.hieos.DocViewer.client.model.authentication.Credentials;
import com.vangent.hieos.DocViewer.client.model.config.Config;
import com.vangent.hieos.DocViewer.client.helper.Observer;
import com.vangent.hieos.DocViewer.client.helper.TimeOutHelper;
import com.vangent.hieos.DocViewer.client.model.document.DocumentSearchCriteria;
import com.vangent.hieos.DocViewer.client.model.patient.Patient;
import com.vangent.hieos.DocViewer.client.model.patient.PatientRecord;
import com.vangent.hieos.DocViewer.client.model.patient.PatientSearchCriteria;
import com.vangent.hieos.DocViewer.client.services.proxy.AuthenticationService;
import com.vangent.hieos.DocViewer.client.services.proxy.ConfigRetrieveService;
import com.vangent.hieos.DocViewer.client.services.proxy.DocumentQueryService;
import com.vangent.hieos.DocViewer.client.services.proxy.LogoutService;
import com.vangent.hieos.DocViewer.client.services.proxy.PatientQueryService;
import com.vangent.hieos.DocViewer.client.view.document.DocumentListObserver;
import com.vangent.hieos.DocViewer.client.view.document.DocumentContainerCanvas;
import com.vangent.hieos.DocViewer.client.view.patient.FindPatientsMainCanvas;
import com.vangent.hieos.DocViewer.client.view.patient.PatientListObserver;
import com.vangent.hieos.DocViewer.client.view.patient.PatientContainerCanvas;

//import com.vangent.hieos.DocViewer.client.services.PDSRemoteService.Util;

/**
 * 
 * @author Bernie Thuman
 * 
 */
public class DocViewerController {
	private Canvas mainCanvas;
	private AuthenticationContext authContext;
	private FindPatientsMainCanvas findPatientsMainCanvas;
	private TabSet patientTabSet = null;
	private Config config = null;
	private ToolStripButton viewPatientsButton;

	/**
	 * 
	 */
	public DocViewerController() {

	}

	/**
	 * 
	 * @param authObserver
	 * @param userid
	 * @param password
	 */
	public void login(AuthenticationObserver authObserver, String userid,
			String password, String authDomainTypeKey) {
		Credentials creds = new Credentials();
		creds.setPassword(password);
		creds.setUserId(userid);
		creds.setAuthDomainTypeKey(authDomainTypeKey);
		TimeOutHelper timeOutHelper = new TimeOutHelper();
		timeOutHelper.setPrompt("Authenticating ...");
		AuthenticationService service = new AuthenticationService(creds,
				authObserver, timeOutHelper);
		service.doWork();
	}

	/**
	 * 
	 * @param observer
	 */
	public void logout(LogoutObserver observer) {
		// Reset view elements.
		this.findPatientsMainCanvas = null;
		this.patientTabSet = null;

		TimeOutHelper timeOutHelper = new TimeOutHelper();
		LogoutService service = new LogoutService(observer, timeOutHelper);
		service.doWork();
	}

	/**
	 * 
	 */
	public void loadConfig(Observer observer) {
		// ConfigObserver observer = new ConfigObserver(this);
		TimeOutHelper timeOutHelper = new TimeOutHelper();
		timeOutHelper.setPrompt("Loading configuration ...");
		ConfigRetrieveService service = new ConfigRetrieveService(observer,
				timeOutHelper);
		service.doWork();
	}

	/**
	 * 
	 * @param config
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * 
	 * @return
	 */
	public Config getConfig() {
		return this.config;
	}

	/**
	 * 
	 * @return
	 */
	public AuthenticationContext getAuthContext() {
		return authContext;
	}

	/**
	 * 
	 * @param authContext
	 */
	public void setAuthContext(AuthenticationContext authContext) {
		this.authContext = authContext;
	}

	/**
	 * 
	 * @param canvas
	 */
	public void setMainCanvas(Canvas canvas) {
		mainCanvas = canvas;
	}
	

	/**
	 * 
	 * @return
	 */
	public ToolStripButton getViewPatientsButton() {
		return viewPatientsButton;
	}

	/**
	 * 
	 * @param viewPatientsButton
	 */
	public void setViewPatientsButton(ToolStripButton viewPatientsButton) {
		this.viewPatientsButton = viewPatientsButton;
	}

	/**
	 * 
	 * @param findPatientsMainCanvas
	 */
	public void setPatientViewContainer(
			FindPatientsMainCanvas findPatientsMainCanvas) {
		this.findPatientsMainCanvas = findPatientsMainCanvas;
	}

	/**
	 * 
	 * @param criteria
	 */
	public void findPatients(PatientSearchCriteria criteria) {
		PatientListObserver observer = new PatientListObserver(
				findPatientsMainCanvas);
		this.findPatients(criteria, observer);
	}

	/**
	 * 
	 * @param criteria
	 * @param observer
	 */
	public void findPatients(PatientSearchCriteria criteria,
			final PatientListObserver observer) {
		TimeOutHelper timeOutHelper = new TimeOutHelper();
		PatientQueryService service = new PatientQueryService(criteria,
				observer, timeOutHelper);
		service.doWork();
	}

	/**
	 * 
	 * @param patientRecord
	 */
	public void viewPatient(PatientRecord patientRecord) {
		// Create a patient view container for the patient.
		viewPatientsButton.setSelected(true);
		final PatientContainerCanvas patientContainerCanvas = new PatientContainerCanvas(
				patientRecord, this);
		this.addPatientTab(patientRecord, patientContainerCanvas);

		/*
		 * DocumentSearchCriteria criteria = new DocumentSearchCriteria();
		 * criteria.setPatient(patientRecord.getPatient()); String searchMode =
		 * this.getConfig().get(Config.KEY_SEARCH_MODE);
		 * criteria.setSearchMode(searchMode); DocumentListObserver observer =
		 * new DocumentListObserver(patientRecord, this);
		 * this.findDocuments(criteria, observer);
		 */
	}

	/**
	 * 
	 * @param patientRecord
	 * @param documentContainerCanvas
	 */
	public void findDocuments(PatientRecord patientRecord, DocumentContainerCanvas documentContainerCanvas) {
		DocumentSearchCriteria criteria = new DocumentSearchCriteria();
		criteria.setPatient(patientRecord.getPatient());
		String searchMode = this.getConfig().get(Config.KEY_SEARCH_MODE);
		criteria.setSearchMode(searchMode);
		DocumentListObserver observer = new DocumentListObserver(documentContainerCanvas);
		this.findDocuments(criteria, observer);
	}

	/**
	 * 
	 * @param criteria
	 * @param observer
	 */
	private void findDocuments(final DocumentSearchCriteria criteria,
			final DocumentListObserver observer) {
		TimeOutHelper timeOutHelper = new TimeOutHelper();
		DocumentQueryService service = new DocumentQueryService(criteria,
				observer, timeOutHelper);
		service.doWork();
	}

	/**
	 * 
	 */
	public void showViewPatients() {
		if (patientTabSet != null) {
			this.addPaneToMainCanvas(patientTabSet);
		} else {
			SC.warn("You must choose a Patient using \"Find Patients\" before being able to view patient data");
		}
	}

	/**
	 * 
	 */
	public void showFindPatients() {
		if (findPatientsMainCanvas == null) {
			findPatientsMainCanvas = new FindPatientsMainCanvas(this);
		}
		this.addPaneToMainCanvas(findPatientsMainCanvas);
	}

	/**
	 * 
	 * @param patientRecord
	 * @param patientContainerCanvas
	 */
	public void addPatientTab(PatientRecord patientRecord,
			PatientContainerCanvas patientContainerCanvas) {
		// Add the document view container to the patient tab.
		final Tab patientTab = this.getPatientTab(patientRecord);
		patientTab.setPane(patientContainerCanvas);
		this.addPaneToMainCanvas(patientTabSet);

		// Show the patient as the current tab.
		patientTabSet.selectTab(patientTab);
	}

	/**
	 * 
	 * @param patientRecord
	 * @return
	 */
	private Tab getPatientTab(PatientRecord patientRecord) {
		// Create patient tab set if it does not already exist.
		if (patientTabSet == null) {
			this.createPatientTabSet();
		}

		// Create a new tab for the patient.
		final Tab patientTab = new Tab();
		String imageName = "person.png";
		Patient patient = patientRecord.getPatient();
		if (patient.getGender() != null) {
			if (patient.getGender().equals("M")) {
				imageName = "gender_male.png";
			} else if (patient.getGender().equals("F")) {
				imageName = "gender_female.png";
			}
		}
		patientTab.setTitle(Canvas.imgHTML(imageName) + " "
				+ patientRecord.getFormattedName());
		patientTab.setCanClose(true);

		// Add tab to the tab set.
		patientTabSet.addTab(patientTab);

		return patientTab;
	}

	/**
	 * 
	 */
	private void createPatientTabSet() {
		// Create patient tab set if it does not already exist.
		patientTabSet = new TabSet();
		patientTabSet.setWidth100();
		patientTabSet.setHeight100();
		patientTabSet.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				// Tab tab = event.getTab();
				int numTabs = patientTabSet.getTabs().length;
				if (numTabs == 1) {
					// Clear out the tabs.
					patientTabSet = null;
					// Show the find patients view.
					showFindPatients();
				}
			}
		});
	}

	/**
	 * 
	 * @param childPane
	 */
	private void addPaneToMainCanvas(Canvas childPane) {
		Canvas[] children = mainCanvas.getChildren();
		boolean foundPane = false;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != childPane) {
				// A bit of a hack: should only have one item (so we can break
				// here).
				mainCanvas.removeChild(children[i]);
				mainCanvas.addChild(childPane);
				foundPane = true;
				break;
			}
		}
		if (foundPane == false) {
			mainCanvas.addChild(childPane);
		}
	}
}