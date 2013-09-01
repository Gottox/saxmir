package de.gottox.saxmir.css;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class CssNavigator<T> implements CssSelectorCallback<T> {
	class State {
		final HashMap<CssSelector, CssSelectorCallback<T>> childSelectors;
		final HashSet<CssSelectorCallback<T>> matchingHandler;

		final HashSet<CssSelectorCallback<T>> matchingHandlerRoot = new HashSet<CssSelectorCallback<T>>();
		HashMap<CssSelector, CssSelectorCallback<T>> nextElementSelector = new HashMap<CssSelector, CssSelectorCallback<T>>();
		HashMap<CssSelector, CssSelectorCallback<T>> directChildSelectors = new HashMap<CssSelector, CssSelectorCallback<T>>();

		public State(State parent) {
			childSelectors = new HashMap<CssSelector, CssSelectorCallback<T>>(
					parent.childSelectors);
			matchingHandler = new HashSet<CssSelectorCallback<T>>(
					parent.matchingHandler);
		}

		public State() {
			childSelectors = new HashMap<CssSelector, CssSelectorCallback<T>>();
			matchingHandler = new HashSet<CssSelectorCallback<T>>();
		}
	}

	private Stack<State> states = new Stack<State>();
	private State currentState = new State();

	public void register(String selector, CssSelectorCallback<T> handler) {
		register(CssSelector.parse(selector), handler);
	}

	public void register(Set<CssSelector> selectors,
			CssSelectorCallback<T> handler) {
		for (CssSelector selector : selectors) {
			if (selector.combinator == '>')
				currentState.directChildSelectors.put(selector, handler);
			else
				currentState.childSelectors.put(selector, handler);
		}
	}

	@Override
	public void onStartElement(T container, CharSequence tagname,
			Map<CharSequence, CharSequence> attrs) {
		states.push(currentState);
		State newState = new State(currentState);
		HashMap<CssSelector, CssSelectorCallback<T>> thisElementHandler = currentState.nextElementSelector;
		currentState.nextElementSelector = new HashMap<CssSelector, CssSelectorCallback<T>>();

		handleNewElement(tagname, attrs, newState,
				currentState.directChildSelectors);
		handleNewElement(tagname, attrs, newState, currentState.childSelectors);
		handleNewElement(tagname, attrs, newState, thisElementHandler);

		currentState = newState;

		for (CssSelectorCallback<T> handler : currentState.matchingHandlerRoot) {
			handler.onStartMatching(container, tagname, attrs);
		}
		for (CssSelectorCallback<T> handler : currentState.matchingHandler) {
			handler.onStartElement(container, tagname, attrs);
		}
	}

	private void handleNewElement(CharSequence tagname,
			Map<CharSequence, CharSequence> attributes, State newState,
			Map<CssSelector, CssSelectorCallback<T>> handler) {
		Set<Entry<CssSelector, CssSelectorCallback<T>>> entrySet = new HashSet<Entry<CssSelector, CssSelectorCallback<T>>>(
				handler.entrySet());
		for (Entry<CssSelector, CssSelectorCallback<T>> entry : entrySet) {
			final CssSelector selector = entry.getKey();
			final CssSelectorCallback<T> callback = entry.getValue();
			if (selector.matches(tagname, attributes)) {
				if (selector.deeper == null) {
					newState.matchingHandler.add(callback);
					newState.matchingHandlerRoot.add(callback);
				} else if (selector.deeper.combinator == '>') {
					newState.directChildSelectors.put(selector.deeper, callback);
				} else if (selector.deeper.combinator == ' ') {
					newState.childSelectors.put(selector.deeper, callback);
				}
				else if (selector.deeper.combinator == '+') {
					currentState.nextElementSelector.put(selector.deeper,
							callback);
				}
				else if (selector.deeper.combinator == '~'){
					currentState.childSelectors.put(selector.deeper, callback);
				}
			}
		}
	}

	@Override
	public void onEndElement(T container, CharSequence tagname) {
		for (CssSelectorCallback<T> handler : currentState.matchingHandler) {
			handler.onEndElement(container, tagname);
		}
		for (CssSelectorCallback<T> handler : currentState.matchingHandlerRoot) {
			handler.onEndMatching(container, tagname);
		}
		currentState = states.pop();
	}

	@Override
	public void onCharacters(T container, CharSequence seq) {
		for (CssSelectorCallback<T> handler : currentState.matchingHandler) {
			handler.onCharacters(container, seq);
		}
	}

	@Override
	public void onStartMatching(T handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onEndMatching(T handler, CharSequence tag) {
	}

}
