package com.gwt.conn.client;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.TextArea;
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
		//RootPanel.get().add(testLabel, 0, 500);
		
		// this is used so that buttons don't do anything when clicked
		// if the contents that the button would load are already visible
		// need to use storage to save state of editor when interacting with buttons
		storage.setItem("curDataPage", ".null"); // default to nothing

		// add styles to global widgets
		dataEditorPan.addStyleName("marginlessPanel");
		navigationPan.addStyleName("marginPanel");
		nullPage.addStyleName("marginlessPanel");

		// navigationPane and temporary nullPage go in dataEditor
		dataEditorPan.add(navigationPan);
		dataEditorPan.setCellWidth(navigationPan, "33.3%");
		dataEditorPan.add(nullPage);
		dataEditorPan.setCellWidth(nullPage, "66.7%");

		// list of categories and their contents
		ArrayList <Category> cats = menu.getCategories();

		// iterate out buttons for all categories and put them in navigationPan
		for (int i=0; i < cats.size(); ++i) {
			// setup first dimension of menu item button rows array
			miButtonRows.add(new ArrayList <ButtonRow>());
			// create row of buttons, and add it to buttonRows array
			buttonRows.add(createNewCategoryButtonRow(i, cats.get(i), menu));
			// attach button row to UI so it becomes visible
			navigationPan.add(buttonRows.get(i).getButtonRow());
		}

		// add a create new category button at bottom of navigationPan
		final Button newButton = new Button("New");
		newButton.addStyleName("myButton");
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
						String test = FieldVerifier.isValidName(catName, catNames);
						if (!test.equals("")) {
							errorLabel.setText(test);
							submitField.selectAll();
							return;
						}

						// propagate changes then hide the dialog box
						menu.addCategory(catName);
						ArrayList <Category> cats = menu.getCategories();
						int i = cats.size()-1;
						miButtonRows.add(new ArrayList <ButtonRow>());
						buttonRows.add(createNewCategoryButtonRow(i, cats.get(i), menu));
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
	private static ButtonRow createNewCategoryButtonRow(final int i, final Category cat, final Menu menu) {
		// button declarations
		final Button catButton = new Button(cat.getTitle());
		catButton.addStyleName("myButton");
		final Button upButton = new Button("^");
		upButton.addStyleName("myButtonSmall");
		final Button downButton = new Button("v");
		downButton.addStyleName("myButtonSmall");
		final Button deleteButton = new Button("x");
		deleteButton.addStyleName("myButtonSmall");

		// construct actual buttonRow
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.addStyleName("marginlessPanel");
		buttonPanel.add(catButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(deleteButton);
		
		final VerticalPanel page = createNewCategoryContentPanel(miButtonRows.get(i), cat, catButton, menu);
		final ButtonRow buttonRow = new ButtonRow(buttonPanel, page, i);

		// handler for catButton
		class CatClickHandler implements ClickHandler {
			// fired when the user clicks this button; shows the contents of the clicked category
			public void onClick(ClickEvent event) {
				String currentPage = storage.getItem("curDataPage");
				if (!currentPage.equals(cat.getTitle())) { // doesn't load content of page that's already loaded
					// remove current panel before mounting new one
					if (!currentPage.equals(".null")) {
						// searchForCurrentContentPanel is a private helper function for this class
						ArrayList <Category> cats = menu.getCategories(); // from Menu.java
						dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
					}
					else dataEditorPan.remove(nullPage);

					// update current page in storage and mount new content panel in dataEditorPan
					storage.setItem("curDataPage", cat.getTitle());
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
						cat.getTitle() + "</b><br><br>and all of its contents. Do you want to continue?<br><br>"));
				answerPan.add(yesButton);
				answerPan.add(noButton);
				warningPan.add(answerPan);
				warningBox.setWidget(warningPan);
				warningBox.center();

				// handler for yesButton
				class YesHandler implements ClickHandler {
					public void onClick(ClickEvent event) {
						// check to see if one of the pages to be deleted is the current page
						String currentPage = storage.getItem("curDataPage");
						String[] itemNames = cat.getMenuItemNames();
						boolean test = false;
						if (currentPage.equals(cat.getTitle())) test = true;
						for (int i=0; i < itemNames.length; ++i) {
							if (currentPage.equals("mi." + itemNames[i])) test = true;
						}
						if (test) { // deleting current page, so load null page
							storage.setItem("curDataPage", ".null");
							ArrayList <Category> cats = menu.getCategories();
							dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
							dataEditorPan.add(nullPage);
							dataEditorPan.setCellWidth(nullPage, "66.7%");
						}
						
						// totally remove all traces of this category
						navigationPan.remove(buttonPanel); // remove from UI
						int posn = buttonRow.getPosition();
						buttonRows.remove(posn); // remove this category's button row
						miButtonRows.remove(posn); // remove this category's menu item's button rows
						menu.deleteCategory(cat.getTitle()); // remove from menu object
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
	
	/** Swaps the position of two categories. Note: a < b */
	private static void swapCategoryButtonRows(int a, int b, Menu menu) {
		// menu object
		menu.swapCategories(a, b);

		// ui
		navigationPan.remove(buttonRows.get(b).getButtonRow());
		navigationPan.insert(buttonRows.get(b).getButtonRow(), a);

		// local arrays
		buttonRows.get(a).setPosition(b);
		buttonRows.get(b).setPosition(a);
		Collections.swap(buttonRows, a, b);
		Collections.swap(miButtonRows, a, b);

		// json
		StorageContainer.saveChange(menu);
		testLabel.setText(storage.getItem("menu"));
	} // end swapCategoryButtonRows
	
	/** Creates a new page on which the user can modify category contents. */
	private static VerticalPanel createNewCategoryContentPanel(
			final ArrayList <ButtonRow> itemButtonRows, // button rows for this category's menu items
			final Category cat, // current category
			final Button catButton, // category's button in navigation panel
			final Menu menu) {
		
		// first, construct a horizontal panel wherein the category's title can be modified
		final VerticalPanel page = new VerticalPanel();
		page.addStyleName("marginlessPanel");
		final Label errorLabel = new Label();
		errorLabel.addStyleName("errorLabel");
		final Button sendButton = new Button("Update");
		sendButton.addStyleName("myButton");
		final TextBox submitField = new TextBox(); // user can input text using this
		submitField.setText(cat.getTitle()); // default text to be seen on load
		final HorizontalPanel submitPanel = new HorizontalPanel();
		submitPanel.addStyleName("marginlessPanel");
		submitPanel.add(submitField);
		submitPanel.add(sendButton);
		page.add(new HTML("Category Name:"));
		page.add(submitPanel);
		page.add(errorLabel);
		page.add(new HTML("<br>"));

		// add handler for update button
		class UpdateHandler implements ClickHandler, KeyUpHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				submit();
			}

			// fired when the user presses Enter in submitField
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}

			// checks the submitted category name for valid format and whether a category by
			// the given name exists, and, if valid, renames the category
			private void submit() {
				// check if anything was changed
				String newCatName = submitField.getText();
				if (newCatName.equals(cat.getTitle())) return;

				// check for validity
				String[] catNames = menu.getCategoryNames();
				String test = FieldVerifier.isValidName(newCatName, catNames);
				if (!test.equals("")) {
					errorLabel.setText(test);
					submitField.selectAll();
					return;
				}

				// propagate changes
				errorLabel.setText("");
				storage.setItem("curDataPage", newCatName);
				catButton.setText(newCatName);
				cat.setTitle(newCatName);
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		}
		final UpdateHandler updateHandler = new UpdateHandler();
		sendButton.addClickHandler(updateHandler);
		submitField.addKeyUpHandler(updateHandler);
		
		// second, construct a vertical panel for the menu button rows
		final VerticalPanel menuButtonsPan = new VerticalPanel();
		menuButtonsPan.addStyleName("marginlessPanel");
		ArrayList <MenuItem> items = cat.getMenuItems();

		// iterate out buttons for all menuItems in the category and put them in menuButtons
		for (int i=0; i < items.size(); ++i) {
			// create row of buttons, and add it to menuButtonRows array
			itemButtonRows.add(createNewMenuItemButtonRow(i, cat, items.get(i), page, menuButtonsPan, itemButtonRows, menu));
			// attach button row to UI so it becomes visible
			menuButtonsPan.add(itemButtonRows.get(i).getButtonRow());
		}

		// add a create new menu items button at bottom of page
		final Button newButton = new Button("New");
		newButton.addStyleName("myButton");
		page.add(menuButtonsPan);
		page.add(newButton);
		
		// handler for new button
		class NewHandler implements ClickHandler {
			// fired when the user clicks this button; shows a submit form for a new menu item name
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
				submitField.setText("item name..."); // default text to be seen on load
				final HorizontalPanel submitPanel = new HorizontalPanel();
				submitPanel.addStyleName("marginPanel");

				// organize UI
				boxPanel.add(new HTML("Enter a name for the new menu item:"));
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

					// checks the submitted menu item name for valid format
					// if valid, loads a new menu item object and row of buttons
					private void submit() {
						// validate category name
						String itemName = submitField.getText();
						String[] itemNames = cat.getMenuItemNames();
						String test = FieldVerifier.isValidItemName(itemName, itemNames);
						if (!test.equals("")) {
							errorLabel.setText(test);
							submitField.selectAll();
							return;
						}

						// propagate changes then hide the dialog box
						menu.addMenuItem(cat.getTitle(), itemName);
						ArrayList <MenuItem> items = cat.getMenuItems();
						int i = items.size()-1;
						itemButtonRows.add(createNewMenuItemButtonRow(i, cat, items.get(i), page,
								menuButtonsPan, itemButtonRows, menu));
						menuButtonsPan.insert(itemButtonRows.get(i).getButtonRow(), i);
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
		
		return page;
	} // end createNewCategoryContentPanel
	
	/** Creates a new row of buttons for the catPage's panel. */
	private static ButtonRow createNewMenuItemButtonRow(
			final int i, // location in miButtonRows
			final Category cat, // current category
			final MenuItem item, // current item
			final VerticalPanel prevPage, // page of category to which this item belongs
			final VerticalPanel menuButtonsPan, // panel containing menu item button rows
			final ArrayList <ButtonRow> itemButtonRows, // so we can find the current location in miButtonRows
			final Menu menu) {
		
		// button declarations
		final Button itemButton = new Button(item.getName());
		itemButton.addStyleName("myButton");
		final Button upButton = new Button("^");
		upButton.addStyleName("myButtonSmall");
		final Button downButton = new Button("v");
		downButton.addStyleName("myButtonSmall");
		final Button deleteButton = new Button("x");
		deleteButton.addStyleName("myButtonSmall");

		// construct actual buttonRow
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.addStyleName("marginlessPanel");
		buttonPanel.add(itemButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(deleteButton);
		final VerticalPanel page = createNewMenuItemContentPanel(item, itemButton, prevPage, cat, menu);
		final ButtonRow buttonRow = new ButtonRow(buttonPanel, page, i);
		
		// handler for itemButton
		class ItemClickHandler implements ClickHandler {
			// fired when the user clicks this button; shows the contents of the clicked category
			public void onClick(ClickEvent event) {
				// remove current page from UI
				String currentPage = storage.getItem("curDataPage");
				ArrayList <Category> cats = menu.getCategories();
				dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
				
				// update current page in storage
				storage.setItem("curDataPage", "mi." + item.getName());
				
				// mount new page
				VerticalPanel newContentPage = buttonRow.getContentPage();
				dataEditorPan.add(newContentPage);
				dataEditorPan.setCellWidth(newContentPage, "66.7%");
			}
		}

		// handler for upButton
		class UpHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				// not first row
				int posn = buttonRow.getPosition();
				int loc = miButtonRows.indexOf(itemButtonRows);
				if (posn > 0) swapMenuItemButtonRows(posn-1, posn, loc, cat, menuButtonsPan, menu);
			}
		}

		// handler for downButton
		class DownHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				// not last row
				int posn = buttonRow.getPosition();
				int loc = miButtonRows.indexOf(itemButtonRows);
				if (posn < miButtonRows.get(loc).size() - 1)
					swapMenuItemButtonRows(posn, posn+1, loc, cat, menuButtonsPan, menu);
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
				warningPan.add(new HTML("Hold on! You are about to delete the menu item<br><br><b>" +
						item.getName() + "</b><br><br>and all of its contents. Do you want to continue?<br><br>"));
				answerPan.add(yesButton);
				answerPan.add(noButton);
				warningPan.add(answerPan);
				warningBox.setWidget(warningPan);
				warningBox.center();

				// handler for yesButton
				class YesHandler implements ClickHandler {
					public void onClick(ClickEvent event) {
						menuButtonsPan.remove(buttonPanel); // remove row of buttons from UI
						int posn = buttonRow.getPosition(); // get position of current button row in itemButtonRows
						int loc = miButtonRows.indexOf(itemButtonRows); // get position of itemButtonRows in miButtonRows
						miButtonRows.get(loc).remove(posn); // remove current button row
						menu.deleteMenuItem(cat.getTitle(), item.getName()); // delete this menu item from the menu
						while (posn < miButtonRows.get(loc).size()) { // update position of rows below the one deleted
							int oldPosn = miButtonRows.get(loc).get(posn).getPosition();
							miButtonRows.get(loc).get(posn).setPosition(oldPosn-1);
							posn++;
						}
						StorageContainer.saveChange(menu);
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
		final ItemClickHandler itemClickHandler = new ItemClickHandler();
		itemButton.addClickHandler(itemClickHandler);
		final UpHandler upHandler = new UpHandler();
		upButton.addClickHandler(upHandler);
		final DownHandler downHandler = new DownHandler();
		downButton.addClickHandler(downHandler);
		final DeleteHandler deleteHandler = new DeleteHandler();
		deleteButton.addClickHandler(deleteHandler);
		
		return buttonRow;
	} // end createNewMenuItemButtonRow
	
	/** Creates a new page on which the user can modify menu item contents. */
	private static VerticalPanel createNewMenuItemContentPanel(
			final MenuItem item, // menu item for which this page is being constructed
			final Button itemButton, // button from category page
			final VerticalPanel prevPage, // this menu item's category page
			final Category cat, // this menu item's category
			final Menu menu) {

		// the page to be returned
		final VerticalPanel page = new VerticalPanel();
		page.addStyleName("marginlessPanel");
		
		// first, construct a horizontal panel wherein the menu item's name can be modified
		final Label nameErrorLabel = new Label();
		nameErrorLabel.addStyleName("errorLabel");
		final Button nameSendButton = new Button("Update");
		nameSendButton.addStyleName("myButton");
		final TextBox nameSubmitField = new TextBox(); // user can input text using this
		nameSubmitField.setText(item.getName()); // default text to be seen on load
		final HorizontalPanel nameSubmitPanel = new HorizontalPanel();
		nameSubmitPanel.addStyleName("marginlessPanel");
		nameSubmitPanel.add(nameSubmitField);
		nameSubmitPanel.add(nameSendButton);
		page.add(new HTML("Item Name:"));
		page.add(nameSubmitPanel);
		page.add(nameErrorLabel);
		page.add(new HTML("<br>"));

		// add handler for name update button
		class NameHandler implements ClickHandler, KeyUpHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				submit();
			}

			// fired when the user presses Enter in submitField
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}

			// checks the submitted category name for valid format and whether a menu item by
			// the given name exists, and, if valid, renames the menu item
			private void submit() {
				// check if anything was changed
				String newItemName = nameSubmitField.getText();
				if (newItemName.equals(item.getName())) return;

				// check for validity
				String[] itemNames = menu.getCategoryNames();
				String test = FieldVerifier.isValidItemName(newItemName, itemNames);
				if (!test.equals("")) {
					nameErrorLabel.setText(test);
					nameSubmitField.selectAll();
					return;
				}

				// propagate changes
				nameErrorLabel.setText("");
				storage.setItem("curDataPage", "mi." + newItemName);
				itemButton.setText(newItemName);
				item.setName(newItemName);
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		} // NameHandler
		final NameHandler nameHandler = new NameHandler();
		nameSendButton.addClickHandler(nameHandler);
		nameSubmitField.addKeyUpHandler(nameHandler);

		// second, construct a horizontal panel wherein the menu item's image URL can be modified
		final Label imgErrorLabel = new Label();
		imgErrorLabel.addStyleName("errorLabel");
		final Button imgSendButton = new Button("Update");
		imgSendButton.addStyleName("myButton");
		final TextBox imgSubmitField = new TextBox(); // user can input text using this
		imgSubmitField.setText(item.getImage()); // default text to be seen on load
		final HorizontalPanel imgSubmitPanel = new HorizontalPanel();
		imgSubmitPanel.addStyleName("marginlessPanel");
		imgSubmitPanel.add(imgSubmitField);
		imgSubmitPanel.add(imgSendButton);
		page.add(new HTML("Image URL (leave blank if none):"));
		page.add(imgSubmitPanel);
		page.add(imgErrorLabel);
		page.add(new HTML("<br>"));

		// add handler for image update button
		class ImageHandler implements ClickHandler, KeyUpHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				submit();
			}

			// fired when the user presses Enter in submitField
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}

			// checks the submitted URL for validity (starts with http://)
			private void submit() {
				// check if anything was changed
				String newURL = imgSubmitField.getText();
				if (newURL.equals(item.getImage())) return;

				// check for validity
				String test = FieldVerifier.isValidURL(newURL);
				if (!test.equals("")) {
					imgErrorLabel.setText(test);
					imgSubmitField.selectAll();
					return;
				}

				// propagate changes
				imgErrorLabel.setText("");
				item.setImage(escapeHtml(newURL));
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
			
			// escape an html string to prevent cross-site script vulnerabilities
			private String escapeHtml(String url) {
				return url.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			}
		} // ImageHandler
		final ImageHandler imgHandler = new ImageHandler();
		imgSendButton.addClickHandler(imgHandler);
		imgSubmitField.addKeyUpHandler(imgHandler);
		
		// third, construct a horizontal panel wherein the menu item's description can be modified
		final Label descErrorLabel = new Label();
		descErrorLabel.addStyleName("errorLabel");
		final Button descSendButton = new Button("Update");
		descSendButton.addStyleName("myButton");
		final TextArea descSubmitField = new TextArea(); // user can input text using this
		descSubmitField.setCharacterWidth(40); // 40 columns
		descSubmitField.setVisibleLines(5); // 5 rows
		descSubmitField.setText(item.getDescription()); // default text to be seen on load
		final HorizontalPanel descSubmitPanel = new HorizontalPanel();
		descSubmitPanel.addStyleName("marginlessPanel");
		descSubmitPanel.add(descSubmitField);
		descSubmitPanel.add(descSendButton);
		page.add(new HTML("Description:<br>Use \\n for new lines.<br>Use \\\" for quotes.<br>Use \\\\ for backslash."));
		page.add(descSubmitPanel);
		page.add(descErrorLabel);
		page.add(new HTML("<br>"));

		// add handler for description update button
		class DescriptionHandler implements ClickHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				// check if anything was changed
				String newDesc = descSubmitField.getText();
				if (newDesc.equals(item.getDescription())) return;

				// check for validity
				String test = FieldVerifier.isValidDescription(newDesc);
				if (!test.equals("")) {
					descErrorLabel.setText(test);
					return;
				}

				// propagate changes
				descErrorLabel.setText("");
				item.setDescription(newDesc);
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		} // DescriptionHandler
		final DescriptionHandler descHandler = new DescriptionHandler();
		descSendButton.addClickHandler(descHandler);

		// fourth, construct a horizontal panel wherein the menu item's price can be modified
		final Label priceErrorLabel = new Label();
		priceErrorLabel.addStyleName("errorLabel");
		final Button priceSendButton = new Button("Update");
		priceSendButton.addStyleName("myButton");
		final TextBox priceSubmitField = new TextBox(); // user can input text using this
		priceSubmitField.setText(item.getPrice()); // default text to be seen on load
		final HorizontalPanel priceSubmitPanel = new HorizontalPanel();
		priceSubmitPanel.addStyleName("marginlessPanel");
		priceSubmitPanel.add(priceSubmitField);
		priceSubmitPanel.add(priceSendButton);
		page.add(new HTML("Price:"));
		page.add(priceSubmitPanel);
		page.add(priceErrorLabel);
		page.add(new HTML("<br>"));

		// add handler for price update button
		class PriceHandler implements ClickHandler, KeyUpHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				submit();
			}

			// fired when the user presses Enter in submitField
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}

			// checks the submitted price for validity (real number)
			private void submit() {
				// check if anything was changed
				String newPrice = priceSubmitField.getText();
				if (newPrice.equals(item.getPrice())) return;

				// check for validity
				String test = FieldVerifier.isValidPrice(newPrice);
				if (!test.equals("")) {
					priceErrorLabel.setText(test);
					priceSubmitField.selectAll();
					return;
				}

				// propagate changes
				priceErrorLabel.setText("");
				item.setPrice(newPrice);
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		} // PriceHandler
		final PriceHandler priceHandler = new PriceHandler();
		priceSendButton.addClickHandler(priceHandler);
		priceSubmitField.addKeyUpHandler(priceHandler);

		// finally, construct a back button to back to this menu item's category page
		final Button backButton = new Button("< Go Back");
		backButton.addStyleName("myButton");
		page.add(backButton);

		// add handler for back button
		class BackHandler implements ClickHandler {
			// fired when the user clicks go back
			public void onClick(ClickEvent event) {
				// remove current page
				String currentPage = storage.getItem("curDataPage");
				ArrayList <Category> cats = menu.getCategories();
				dataEditorPan.remove(searchForCurrentContentPanel(cats, currentPage));
				
				// update current page in storage
				storage.setItem("curDataPage", cat.getTitle());
				
				// mount new page
				dataEditorPan.add(prevPage);
				dataEditorPan.setCellWidth(prevPage, "66.7%");
			}
		} // BackHandler
		final BackHandler backHandler = new BackHandler();
		backButton.addClickHandler(backHandler);
		
		return page;
	} // end createNewMenuItemContentPanel
	
	/** Swaps the position of two menu items. Note: a < b */
	private static void swapMenuItemButtonRows(
			int a, // position to be swapped
			int b, // position to be swapped
			int i, // position in miButtonRows array
			Category cat, // current category
			VerticalPanel itemButtonsPan, // panel containing menu button rows for this category
			Menu menu) {
		
		// menu object
		cat.swapMenuItems(a, b);

		// ui
		itemButtonsPan.remove(miButtonRows.get(i).get(b).getButtonRow());
		itemButtonsPan.insert(miButtonRows.get(i).get(b).getButtonRow(), a);

		// local array
		miButtonRows.get(i).get(a).setPosition(b);
		miButtonRows.get(i).get(b).setPosition(a);
		Collections.swap(miButtonRows.get(i), a, b);

		// json
		StorageContainer.saveChange(menu);
		testLabel.setText(storage.getItem("menu"));
	} // end swapMenuItemButtonRows
	
	/** Helper for finding the current page panel, which could be a category or menu item. */
	private static VerticalPanel searchForCurrentContentPanel(ArrayList <Category> cats, String curPage) {
		// search category names
		for (int i=0; i < cats.size(); ++i) {
			if (cats.get(i).getTitle().equals(curPage)) {
				return buttonRows.get(i).getContentPage(); // in ButtonRow.java
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
	
} // DataEditor
