/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.ccc.console;

import io.vertigo.lang.Assertion;
import io.vertigo.shell.command.VCommand;
import io.vertigo.shell.command.VResponse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Console to talk with Vertigo.
 * @author pchretien
 */
public final class VConsole {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final JSonBeautifier jBeautifier = new JSonBeautifier();
	private final VConsoleHandler consoleHandler;
	private boolean jsonMode = true;

	public VConsole() {
		consoleHandler = new VConsoleHandler();
	}

	boolean isJsonMode() {
		return jsonMode;

	}

	private void display() {
		final JFrame frame = new JFrame("Vertigo Console");
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowStateChanged(final WindowEvent e) {
				//
			}

			@Override
			public void windowOpened(final WindowEvent e) {
				consoleHandler.start();

			}

			@Override
			public void windowClosing(final WindowEvent e) {
				//

			}

			@Override
			public void windowClosed(final WindowEvent e) {
				consoleHandler.stop();
			}

			@Override
			public void windowIconified(final WindowEvent e) {
				//

			}

			@Override
			public void windowDeiconified(final WindowEvent e) {
				//

			}

			@Override
			public void windowActivated(final WindowEvent e) {
				//
			}

			@Override
			public void windowDeactivated(final WindowEvent e) {
				//
			}
		});
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frameSize = new Dimension(screenSize.width / 2, screenSize.height / 2);
		final int x = frameSize.width / 2;
		final int y = frameSize.height / 2;
		frame.setBounds(x, y, frameSize.width, frameSize.height);

		final JTextField input = new JTextField();
		final JTextArea output = new JTextArea();

		//		final JButton connectionButton = new JButton("cxn");
		//		connectionButton.setEnabled(false);

		final JButton clearButton = new JButton("clear");
		final InputKeyListener inputKeyListener = new InputKeyListener(this, consoleHandler, input, output);

		final JToggleButton toggleButton = new JToggleButton(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				jsonMode = !jsonMode;
				inputKeyListener.selectCommand();
			}
		});
		toggleButton.setText("json");

		clearButton.addActionListener(new ClearActionListener(inputKeyListener, output, input));
		output.append("Hello World.");
		output.setEditable(false);

		input.setEditable(true);
		input.addKeyListener(inputKeyListener);

		frame.getContentPane().setLayout(new BorderLayout());

		final JPanel header = new JPanel(new BorderLayout());
		//	header.add(connectionButton, BorderLayout.WEST);
		header.add(input, BorderLayout.CENTER);
		header.add(toggleButton, BorderLayout.EAST);
		//		frame.getContentPane().add(input, BorderLayout.NORTH);
		frame.getContentPane().add(header, BorderLayout.NORTH);
		frame.getContentPane().add(new JScrollPane(output), BorderLayout.CENTER);
		frame.getContentPane().add(clearButton, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	private static final class ClearActionListener implements ActionListener {
		private final InputKeyListener inputKeyListener;
		private final JTextArea output;
		private final JTextField input;

		private ClearActionListener(final InputKeyListener inputKeyListener, final JTextArea output, final JTextField input) {
			this.inputKeyListener = inputKeyListener;
			this.output = output;
			this.input = input;
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			output.setText("");
			input.setText("");
			inputKeyListener.reset();
		}
	}

	private static final class InputKeyListener implements KeyListener {
		private final Set<String> commands;
		private final List<String> matchingCommands;
		private final JTextField input;
		private final JTextArea output;
		private final VConsole console;
		private final VConsoleHandler consoleHandler;
		private String typedInput;

		private int index;

		InputKeyListener(final VConsole console, final VConsoleHandler consoleHandler, final JTextField input, final JTextArea output) {
			Assertion.checkNotNull(console);
			Assertion.checkNotNull(consoleHandler);
			Assertion.checkNotNull(input);
			Assertion.checkNotNull(output);
			//-----
			this.console = console;
			this.consoleHandler = consoleHandler;
			commands = new LinkedHashSet<>();
			matchingCommands = new ArrayList<>();
			this.input = input;
			this.output = output;
			reset();
		}

		void reset() {
			index = -1;
			typedInput = "";
			matchingCommands.clear();
			matchingCommands.addAll(commands);
		}

		@Override
		public void keyPressed(final KeyEvent keyEvent) {
			//NOP
		}

		//suggest a command
		private void suggestAnotherCommand(final int offset) {
			index += offset;
			if (index < 0 || matchingCommands.isEmpty()) {
				//On remet la valeur initiale
				input.setText(typedInput);
			} else {
				//System.out.println(">>i" + index);
				if (index >= matchingCommands.size()) {
					index = matchingCommands.size() - 1;
				}
				input.setText(matchingCommands.get(index));
			}
		}

		private void onCommand(final VCommand command) {
			commands.add(command.getName());
			//System.out.println(">>>command : " + command);
			final VResponse response = consoleHandler.execCommand(command);

			if (response.hasError()) {
				output.setForeground(Color.RED);
				output.setText(response.getErrorMsg());
			} else {
				//on attend un json
				output.setForeground(Color.BLACK);
				final JsonParser parser = new JsonParser();
				final JsonElement jsonElement = parser.parse(response.getResponse());
				final String pretty;
				if (console.isJsonMode()) {
					pretty = GSON.toJson(jsonElement);
				} else {
					pretty = jBeautifier.beautify(jsonElement);
				}
				output.setText(pretty);
			}

			//On réinitialise le buffer d'input
			reset();
		}

		@Override
		public void keyReleased(final KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				selectCommand();
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
				//	System.out.println(">>>UP");
				suggestAnotherCommand(1);
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
				//	System.out.println(">>>DOWN");
				suggestAnotherCommand(-1);
			}
		}

		//ping
		//connect --host localhost --port 4443
		private void selectCommand() {
			final String inputText = input.getText();
			final Map<String, String> args = new HashMap<>();
			int idx = inputText.indexOf("--");
			final String commandName = (idx > 0) ? inputText.substring(0, idx).trim() : inputText.trim();
			int idx2, idx3 = 0;
			while (idx > 0) {
				idx2 = inputText.indexOf(" ", idx);
				if (idx2 > 0) {
					idx3 = inputText.indexOf(" ", idx2 + 1);
					if (idx3 < 0) {
						idx3 = inputText.length();
					}
					args.put(inputText.substring(idx + 2, idx2).trim(), inputText.substring(idx2 + 1, idx3).trim());

				} else {
					throw new RuntimeException("syntax error on command : " + commandName);
				}
				idx = inputText.indexOf("--", idx3);
			}
			final VCommand command = new VCommand(commandName, args);
			onCommand(command);
		}

		@Override
		public void keyTyped(final KeyEvent keyEvent) {
			typedInput = input.getText() + keyEvent.getKeyChar();
			for (final Iterator<String> it = matchingCommands.iterator(); it.hasNext();) {
				if (!it.next().startsWith(typedInput)) {
					it.remove();
				}
			}
		}
	}

	public static void main(final String[] args) {
		final VConsole console = new VConsole();
		console.display();
	}

}
