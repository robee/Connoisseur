package com.gwt.conn.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Data Editor Class **
 * 
 * The Data Editor allows the user to modify menu content data such as category names,
 * menu item names, and menu item content. Categories and menu items can be rearranged,
 * deleted and renamed. Menu item content includes name, description, price, image URL.
 * 
 */

public class DataEditor {

	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** Declare these as global for convenience. */
	private static final HorizontalPanel dataEditorPan = new HorizontalPanel();
	private static final VerticalPanel navigationPan = new VerticalPanel();
	private static final VerticalPanel nullPage = new VerticalPanel();
	private static final ArrayList <ButtonRow> buttonRows = new ArrayList<ButtonRow>();
	private static final ArrayList <ArrayList<ButtonRow>> miButtonRows = new ArrayList<ArrayList<ButtonRow>>();
	private static final HTML testLabel = new HTML();
	
	/** Called when the data editor needs to be loaded. */
	public static HorizontalPanel getDataEditor(final Menu menu) {
		// testing
		testLabel.setText(storage.getItem("menu"));
		RootPanel.get().add(testLabel, 0, 500);
		// this is used so that buttons don't do anything when clicked
		// if the contents that the button would load are already visible
		// need to use storage to save state of editor when interacting with buttons
		storage.setItem("curDataPage", "null"); // default to nothing

		// add styles to global widgets
		dataEditorPan.addStyleName("marginlessPanel");
		navigationPan.addStyleName("marginPanel");
		nullPage.addStyleName("marginlessPanel");

		// navigationPane and temporary nullPage go in dataEditor
		dataEditorPan.add(navigationPan);
		dataEditorPan.setCellWidth(navigationPan, "33.3%");
		dataEditorPan.add(nullPage);
		dataEditorPan.setCellWidth(nullPage, "66.7%");

		// stuff to be used in the loop
		String[] catNames = menu.getCategoryNames();
		ArrayList <Category> cats = menu.getCategories();

		// iterate out buttons for all categories and put them in navigationPan
		for (int i=0; i < catNames.length; ++i) {
			// create row of buttons, and add it to buttonRows array
			buttonRows.add(createNewCategoryButtonRow(i, catNames[i], cats.get(i), menu));
			// attach button row to UI so it becomes visible
			navigationPan.add(buttonRows.get(i).getButtonRow());
		}
		
		// add a create new category button at bottom of navigationPan
		final Button newButton = new Button("New");
		navigationPan.add(newButton);

		// handler for new button
		class NewHandler implements ClickHandler {
			// fired when the user clicks this button; shows a submit form for a new category name
			public void onClick(ClickEvent event) {
				// declarations
				final DialogBox submitBox = new DialogBox();
				submitBox.setAnimationEnabled(true);
				final VerticalPanel boxPanel = new VerticalPanel();
				boxPanel.addStyleName("marginPanel");
				boxPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				final Label errorLabel = new Label();
				errorLabel.addStyleName("errorLabel");
				final Button sendButton = new Button("Submit");
				sendButton.addStyleName("myButton");
				final Button cancelButton = new Button("Cancel");
				sendButton.addStyleName("myButton");
				final TextBox submitField = new TextBox(); // user can input text using this
				submitField.setText("category name..."); // default text to be seen on load
				final HorizontalPanel submitPanel = new HorizontalPanel();
				submitPanel.addStyleName("marginPanel");
				
				// organize UI
				boxPanel.add(new HTML("Enter a name for the new category:"));
				submitPanel.add(submitField);
				submitPanel.add(sendButton);
				submitPanel.add(cancelButton);
				boxPanel.add(submitPanel);
				boxPanel.add(errorLabel);
				submitBox.setWidget(boxPanel);
				submitBox.center();
				submitField.setFocus(true);
				submitField.selectAll();
				
				// handler for the sendButton and submitField
				class SubmitHandler implements ClickHandler, KeyUpHandler {
					// fired when the user clicks submit
					public void onClick(ClickEvent event) {
						submit();
					}
					
					// fired when the user presses Enter in submitField
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
					}
					
					// checks the submitted category name for valid format
					// if valid, loads a new category object and row of buttons
					private void submit() {
						// validate category name
						String catName = submitField.getText();
						String[] catNames = menu.getCategoryNames();
						String test = FieldVerifier.isValidCategoryName(catName, catNames);
						if (!test.equals("")) {
							errorLabel.setText(test);
							submitField.selectAll();
							return;
						}
						
						// propagate changes then hide the dialog box
						menu.addCategory(catName);
						ArrayList <Category> cats = menu.getCategories();
						int i = cats.size()-1;
						buttonRows.add(createNewCategoryButtonRow(i, catName, cats.get(i), menu));
						navigationPan.insert(buttonRows.get(i).getButtonRow(), i);
						StorageContainer.saveChange(menu); // save change anytime something changes
						testLabel.setText(storage.getItem("menu"));
						sendButton.setEnabled(false);
						submitField.setEnabled(false);
						submitBox.hide();
					}
				}
				
				// handler for the cancel button
				class CancelHandler implements ClickHandler {
					// fired when the user clicks cancel
					public void onClick(ClickEvent event) {
						sendButton.setEnabled(false);
						submitField.setEnabled(false);
						submitBox.hide();
					}
				}
				
				// attach the handlers
				final SubmitHandler submitHandler = new SubmitHandler();
				sendButton.addClickHandler(submitHandler);
				submitField.addKeyUpHandler(submitHandler);
				final CancelHandler cancelHandler = new CancelHandler();
				cancelButton.addClickHandler(cancelHandler);
			} // end onClick
		} // NewHandler
		
		// attach newHandler to newButton
		final NewHandler newHandler = new NewHandler();
		newButton.addClickHandler(newHandler);
		
		return dataEditorPan;
	} // end getDataEditor
	
	/** Creates a new row of buttons for the navigation panel. */
	private static ButtonRow createNewCategoryButtonRow(final int i, final String catName, final Category cat, final Menu menu) {
		// button declarations
		final Button catButton = new Button(catName);
		catButton.addStyleName("myButton");
		final Button upButton = new Button("^");
		upButton.addStyleName("myButton");
		final Button downButton = new Button("v");
		downButton.addStyleName("myButton");
		final Button deleteButton = new Button("x");
		deleteButton.addStyleName("myButton");
		
		// construct actual buttonRow
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.addStyleName("marginlessPanel");
		buttonPanel.add(catButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(deleteButton);
		final ButtonRow buttonRow = new ButtonRow(buttonPanel, createNewCategoryContentPanel(cat, catButton), i);
		
		// handler for catButton
		class CatClickHandler implements ClickHandler {
			// fired when the user clicks this button; shows the contents of the clicked category
			public void onClick(ClickEvent event) {
				String currentPage = storage.getItem("curDataPage");
				if (!currentPage.equals(catName)) { // doesn't load content of something that's already loaded
					// current list of all categories
					ArrayList <Category> cats = menu.getCategories(); // from Menu.java
					
					// remove current panel before mounting new one
					if (!currentPage.equals("null")) {
						// searchForCategoryContentPanel is a private helper function for this class
						dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
					}
					else dataEditorPan.remove(nullPage);

					// update current page in storage and mount new content panel in dataEditorPan
					storage.setItem("curDataPage", catName);
					VerticalPanel newContentPage = buttonRow.getContentPage();
					dataEditorPan.add(newContentPage);
					dataEditorPan.setCellWidth(newContentPage, "66.7%");
				}
			}
		}

		// handler for upButton
		class UpHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				// not first row
				int posn = buttonRow.getPosition();
				if (posn > 0) swapCategoryButtonRows(posn-1, posn, menu);
			}
		}

		// handler for downButton
		class DownHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				// not last row
				int posn = buttonRow.getPosition();
				if (posn < buttonRows.size() - 1) swapCategoryButtonRows(posn, posn+1, menu);
			}
		}

		// handler for deleteButton
		class DeleteHandler implements ClickHandler {
			// makes an "are you sure?" popup box
			public void onClick(ClickEvent event) {
				// declarations
				final DialogBox warningBox = new DialogBox();
				warningBox.setAnimationEnabled(true);
				warningBox.setText("Warning");
				final VerticalPanel warningPan = new VerticalPanel();
				warningPan.addStyleName("marginPanel");
				warningPan.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				final HorizontalPanel answerPan = new HorizontalPanel();
				answerPan.addStyleName("marginPanel");
				final Button yesButton = new Button("Yes");
				yesButton.addStyleName("myButton");
				final Button noButton = new Button("No");
				noButton.addStyleName("myButton");

				// organize UI
				warningPan.add(new HTML("Hold on! You are about to delete the category<br><br><b>" +
						catName + "</b><br><br>and all of its contents. Do you want to continue?<br><br>"));
				answerPan.add(yesButton);
				answerPan.add(noButton);
				warningPan.add(answerPan);
				warningBox.setWidget(warningPan);
				warningBox.center();

				// handler for yesButton
				class YesHandler implements ClickHandler {
					public void onClick(ClickEvent event) {
						String currentPage = storage.getItem("curDataPage");
						if (currentPage.equals(catName)) { // deleting current page
							storage.setItem("curDataPage", "null");
							ArrayList <Category> cats = menu.getCategories();
							dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
							dataEditorPan.add(nullPage);
							dataEditorPan.setCellWidth(nullPage, "66.7%");
						}
						navigationPan.remove(buttonPanel);
						int posn = buttonRow.getPosition();
						buttonRows.remove(buttonRow);
						menu.deleteCategory(catName); // in Menu.java
						while (posn < buttonRows.size()) { // update position of rows below the one deleted
							int oldPosn = buttonRows.get(posn).getPosition();
							buttonRows.get(posn).setPosition(oldPosn-1);
							posn++;
						}
						StorageContainer.saveChange(menu); // in StorageContainer.java
						testLabel.setText(storage.getItem("menu"));
						warningBox.hide();
					}
				}

				// handler for noButton
				class NoHandler implements ClickHandler {
					public void onClick(ClickEvent event) {
						warningBox.hide();
					}
				}

				// attach the handlers
				final YesHandler yesHandler = new YesHandler();
				yesButton.addClickHandler(yesHandler);
				final NoHandler noHandler = new NoHandler();
				noButton.addClickHandler(noHandler);
			} // end onClick
		} // DeleteHandler

		// attach the handlers
		final CatClickHandler catClickHandler = new CatClickHandler();
		catButton.addClickHandler(catClickHandler);
		final UpHandler upHandler = new UpHandler();
		upButton.addClickHandler(upHandler);
		final DownHandler downHandler = new DownHandler();
		downButton.addClickHandler(downHandler);
		final DeleteHandler deleteHandler = new DeleteHandler();
		deleteButton.addClickHandler(deleteHandler);
		
		return buttonRow;
	} // end createNewCategoryButtonRow
	
	/** Creates a new page on which the user can modify category contents. */
	private static VerticalPanel createNewCategoryContentPanel(Category cat, Button catButton) {
		final VerticalPanel contentPage = new VerticalPanel();
		contentPage.addStyleName("marginlessPanel");
		
		//TODO
		
		return contentPage;
	} // end createNewCategoryContentPanel
	
	/** Helper for finding the current page panel, which could be a category or menu item. */
	private static VerticalPanel searchForCurrentContentPanel(ArrayList <Category> cats, String curPage) {
		// search category names
		for (int i=0; i < cats.size(); ++i) {
			if (cats.get(i).getTitle().equals(curPage)) {
				return buttonRows.get(i).getContentPage(); // in Category.java
			}
		}
		
		// search menu item names for each category
		for (int i=0; i < cats.size(); ++i) {
			ArrayList <MenuItem> items = cats.get(i).getMenuItems();
			for (int j=0; j < items.size(); ++j) {
				if (("mi." + items.get(j).getName()).equals(curPage)) {
					return miButtonRows.get(i).get(j).getContentPage();
				}
			}
		}
		
		return null;
	} // end searchForCurrentContentPanel

	/** Swaps two elements of the buttonRows array. Note: a < b */
	private static void swapCategoryButtonRows(final int a, final int b, final Menu menu) {
		// menu object
		menu.swapCategories(a, b);
		
		// ui
		navigationPan.remove(buttonRows.get(b).getButtonRow());
		navigationPan.insert(buttonRows.get(b).getButtonRow(), a);
		
		// local array
		buttonRows.get(a).setPosition(b);
		buttonRows.get(b).setPosition(a);
		Collections.swap(buttonRows, a, b);
		
		// json
		StorageContainer.saveChange(menu);
		testLabel.setText(storage.getItem("menu"));
	} // end swapCategoryButtonRows
	
	/** Swaps two elements of the miButtonRows array. Note: a < b */
/*	private static void swapMenuItemButtonRows(final int a, final int b, final Menu menu,
			final Category cat, ArrayList <HorizontalPanel> itemButtonRows) {
		cat.swapMenuItems(a, b);
		navigationPan.remove(buttonRows.get(b));
		navigationPan.insert(buttonRows.get(b), a);
		Collections.swap(buttonRows, a, b);
		Collections.swap(catPages, a, b);
		StorageContainer.saveChange(menu);
	} // end swapCategoryButtonRows
*/	
} // DataEditor
