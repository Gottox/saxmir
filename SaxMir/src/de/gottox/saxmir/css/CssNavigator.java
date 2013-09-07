package de.gottox.saxmir.css;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class CssNavigator implements CssSelectorCallback {
	class State {
		final HashMap<CssSelector, CssSelectorCallback> childSelectors;
		final HashSet<CssSelectorCallback> matchingHandler;

		final HashSet<CssSelectorCallback> matchingHandlerRoot = new HashSet<CssSelectorCallback>();
		HashMap<CssSelector, CssSelectorCallback> nextElementSelector = new HashMap<CssSelector, CssSelectorCallback>();
		HashMap<CssSelector, CssSelectorCallback> directChildSelectors = new HashMap<CssSelector, CssSelectorCallback>();
		
		int index = 0;

		public State(State parent) {
			childSelectors = new HashMap<CssSelector, CssSelectorCallback>(
					parent.childSelectors);
			matchingHandler = new HashSet<CssSelectorCallback>(
					parent.matchingHandler);
			matchingHandler.addAll(parent.matchingHandlerRoot);
		}

		public State() {
			childSelectors = new HashMap<CssSelector, CssSelectorCallback>();
			matchingHandler = new HashSet<CssSelectorCallback>();
		}
	}

	private Stack<State> states = new Stack<State>();
	private State currentState = new State();
	private Map<CharSequence, CharSequence> attributes;
	private CharSequence tagname;

	public void register(String selector, CssSelectorCallback handler) {
		register(CssSelector.parse(selector), handler);
	}
	
	public void register(Set<CssSelector> selectors, CssSelectorCallback handler) {
		for (CssSelector selector : selectors) {
			if (selector.combinator == '>')
				currentState.directChildSelectors.put(selector, handler);
			else
				currentState.childSelectors.put(selector, handler);
		}
	}

	@Override
	public void onStartChild(CssNavigator container, CharSequence tagname,
			Map<CharSequence, CharSequence> attrs) {
		this.tagname = tagname;
		this.attributes = attrs;
		final State oldState = currentState;
		states.push(oldState);
		State newState = new State(oldState);
		HashMap<CssSelector, CssSelectorCallback> thisElementHandler = oldState.nextElementSelector;
		oldState.index++;
		oldState.nextElementSelector = new HashMap<CssSelector, CssSelectorCallback>();

		currentState = newState;
		handleNewElement(oldState, newState,
				oldState.directChildSelectors);
		handleNewElement(oldState, newState, oldState.childSelectors);
		handleNewElement(oldState, newState, thisElementHandler);

		callHandler(container, newState);
	}

	public void callHandler(CssNavigator container, State state) {
		for (CssSelectorCallback handler : state.matchingHandlerRoot) {
			handler.onStartMatching(container, this.tagname, this.attributes);
		}
		for (CssSelectorCallback handler : state.matchingHandler) {
			handler.onStartChild(container, this.tagname, this.attributes);
		}
	}

	private void handleNewElement(State oldState,
			State newState, Map<CssSelector, CssSelectorCallback> handler) {
		Set<Entry<CssSelector, CssSelectorCallback>> entrySet = new HashSet<Entry<CssSelector, CssSelectorCallback>>(
				handler.entrySet());
		for (Entry<CssSelector, CssSelectorCallback> entry : entrySet) {
			handleNewElement(oldState, newState, entry.getKey(), entry.getValue());
		}
	}

	public void handleNewElement(State oldState, State newState,
			CssSelector selector, CssSelectorCallback callback) {
		if (selector.matches(tagname, attributes, oldState.index)) {
			if (selector.deeper == null) {
				newState.matchingHandlerRoot.add(callback);
			} else if (selector.deeper.combinator == '>') {
				newState.directChildSelectors.put(selector.deeper,
						callback);
			} else if (selector.deeper.combinator == ' ') {
				newState.childSelectors.put(selector.deeper, callback);
			} else if (selector.deeper.combinator == '+') {
				oldState.nextElementSelector.put(selector.deeper, callback);
			} else if (selector.deeper.combinator == '~') {
				oldState.childSelectors.put(selector.deeper, callback);
			}
		}
	}

	@Override
	public void onEndChild(CssNavigator container, CharSequence tagname) {
		for (CssSelectorCallback handler : currentState.matchingHandler) {
			handler.onEndChild(container, tagname);
		}
		for (CssSelectorCallback handler : currentState.matchingHandlerRoot) {
			handler.onEndMatching(container, tagname);
		}
		currentState = states.pop();
	}

	@Override
	public void onCharacters(CssNavigator container, CharSequence seq) {
		for (CssSelectorCallback handler : currentState.matchingHandlerRoot) {
			handler.onCharacters(container, seq);
		}
		for (CssSelectorCallback handler : currentState.matchingHandler) {
			handler.onCharacters(container, seq);
		}
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
	}

}
