package de.gottox.saxmir.css;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CssSelector {
	CssSelector deeper = null;
	char combinator = ' ';
	CssSelector root = this;
	int nthChild = 0;

	enum MatcherType {
		EXISTS, EQUALS, LIST_CONTAINS, IS_LANG, STARTS_WITH, ENDS_WITH, SUBSTRING
	}

	static public class AttributeMatcher {
		final String name;
		final MatcherType matcherType;
		final String pattern;

		public AttributeMatcher(String tagName, MatcherType matcherType,
				String pattern) {
			super();
			this.name = tagName;
			this.matcherType = matcherType;
			this.pattern = pattern;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder(name);
			switch (matcherType) {
			case STARTS_WITH:
				builder.append("^=");
				break;
			case LIST_CONTAINS:
				builder.append("~=");
				break;
			case SUBSTRING:
				builder.append("*=");
				break;
			case IS_LANG:
				builder.append("|=");
				break;
			case ENDS_WITH:
				builder.append("$=");
				break;
			case EQUALS:
				builder.append("=");
				break;
			case EXISTS:
				return builder.toString();
			}
			builder.append(pattern);
			return builder.toString();
		}

	}

	String tagname = "*";
	String id = null;
	HashMap<String, List<AttributeMatcher>> attributes = new HashMap<String, List<AttributeMatcher>>();
	HashSet<String> pseudoClasses = new HashSet<String>();

	public static Set<CssSelector> parse(CharSequence str) {
		ArrayList<CharSequence> tokens = CssSelector.tokenizer(str);
		return lexer(tokens);
	}

	private static Set<CssSelector> lexer(ArrayList<CharSequence> seq) {
		CssSelector root = new CssSelector();
		CssSelector current = root;
		HashSet<CssSelector> result = new HashSet<CssSelector>();
		for (int i = 0, tokNbr = 0; i < seq.size(); i++, tokNbr++) {
			CharSequence token = seq.get(i);
			if (token.equals("[")) {
				CharSequence tagName = seq.get(++i);
				CharSequence matcher = seq.get(++i);
				MatcherType matcherType = MatcherType.EXISTS;
				CharSequence pattern = null;
				if (!matcher.equals("]")) {
					pattern = seq.get(++i);
					if (matcher.equals("^=")) {
						matcherType = MatcherType.STARTS_WITH;
					} else if (matcher.equals("~=")) {
						matcherType = MatcherType.LIST_CONTAINS;
					} else if (matcher.equals("*=")) {
						matcherType = MatcherType.SUBSTRING;
					} else if (matcher.equals("|=")) {
						matcherType = MatcherType.IS_LANG;
					} else if (matcher.equals("$=")) {
						matcherType = MatcherType.ENDS_WITH;
					} else if (matcher.equals("=")) {
						matcherType = MatcherType.EQUALS;
					} else {
						throw new RuntimeException("Unknown Operator "
								+ matcher);
					}
					if (!"]".equals(seq.get(++i))) {
						throw new RuntimeException("Unterminated ]");
					}
				}
				current.addAttributeMatcher(tagName, matcherType, pattern);
			} else if (token.length() == 1
					&& ">+~ ".indexOf(token.charAt(0)) >= 0) {
				if (tokNbr != 0) {
					current.deeper = new CssSelector();
					current = current.deeper;
				}
				current.combinator = token.charAt(0);
				current.root = root;
			} else if (token.equals("#")) {
				current.id = seq.get(i++).toString();
			} else if (token.equals(".")) {
				current.addAttributeMatcher("class", MatcherType.LIST_CONTAINS,
						seq.get(++i));
			} else if (token.equals(":")) {
				String pseudoCls = seq.get(++i).toString();
				if ("root".equals(pseudoCls)) {
					if (current == root)
						current.combinator = '>';
					else
						throw new RuntimeException(
								":root used for non-root selector");
				} else if ("nth-child".equals(pseudoCls)) {
					if (!"(".equals(seq.get(++i).toString()))
						throw new RuntimeException(
								":nth-child not followed by (");
					while (!(token = seq.get(++i).toString()).equals(")")) {
						current.nthChild = Integer.parseInt(token.toString());
					}

				} else
					current.pseudoClasses.add(pseudoCls);
			} else if (token.equals(",")) {
				result.add(root);
				tokNbr = -1;
				current = root = new CssSelector();
			} else {
				current.tagname = token.toString();
			}
		}
		result.add(root);
		return result;
	}

	private void addAttributeMatcher(CharSequence attrName,
			MatcherType matcherType, CharSequence pattern) {
		String attrNameStr = attrName.toString();
		String patternStr = pattern == null ? null : pattern.toString();
		List<AttributeMatcher> matcher = attributes.get(attrName.toString());
		if (matcher == null)
			attributes.put(attrNameStr,
					matcher = new ArrayList<CssSelector.AttributeMatcher>());
		matcher.add(new AttributeMatcher(attrNameStr, matcherType, patternStr));

	}

	static ArrayList<CharSequence> tokenizer(CharSequence seq) {
		ArrayList<CharSequence> tokens = new ArrayList<CharSequence>();
		int length = seq.length();
		for (int i = 0; i < length; i++) {
			char chr = seq.charAt(i);
			int begin = i;
			switch (chr) {
			case '~':
			case '^':
			case '*':
			case '|':
			case '$':
				if (i+1 < length && seq.charAt(i + 1) == '=') {
					i++;
					tokens.add(seq.subSequence(begin, i + 1));
					break;
				}
			case '>':
			case ' ':
			case '+':
			case '=':
				int tokenSize = tokens.size();
				if (tokenSize > 0 && " ".equals(tokens.get(tokenSize - 1)))
					tokens.set(tokens.size() - 1, seq.subSequence(begin, i + 1));
				else
					tokens.add(seq.subSequence(begin, i + 1));
				while (i+1 < length && Character.isWhitespace(seq.charAt(i + 1)))
					i++;
				break;
			case '#':
			case '[':
			case ']':
			case '(':
			case ')':
			case '.':
			case ':':
				tokens.add(seq.subSequence(i, i + 1));
				break;
			case ',':
				tokens.add(seq.subSequence(i, i + 1));
				while (Character.isWhitespace(seq.charAt(i + 1)))
					i++;
				break;
			case '"':
			case '\'':
				char tmp;
				while ((tmp = seq.charAt(++i)) != chr) {
					if (tmp == '\\')
						i++;
				}
				tokens.add(seq.subSequence(begin + 1, i).toString()
						.replace("\\" + chr, "" + chr));
				break;
			default:
				if (Character.isLetter(chr) || Character.isDigit(chr)
						|| "-_".indexOf(chr) >= 0) {
					while (i + 1 < seq.length()
							&& Character.isLetter(chr = seq.charAt(i + 1))
							|| Character.isDigit(chr) || "-_".indexOf(chr) >= 0)
						i++;
					tokens.add(seq.subSequence(begin, i + 1));
				}
			}
		}
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this == root) {
			builder.append(tagname);
			if (this.combinator == '>')
				builder.append(":root");
		} else if (combinator != ' ') {
			builder.append(' ');
			builder.append(combinator);
			builder.append(' ');
			builder.append(tagname);
		} else {
			builder.append(combinator);
			builder.append(tagname);
		}
		if (id != null) {
			builder.append('#');
			builder.append(id);
		}
		for (String pseudoCls : pseudoClasses) {
			builder.append(':');
			builder.append(pseudoCls);
		}
		for (List<AttributeMatcher> attrs : attributes.values()) {
			for (AttributeMatcher attr : attrs) {
				builder.append("[");
				builder.append(attr.toString());
				builder.append("]");
			}
		}
		if (deeper != null) {
			builder.append(deeper.toString());
		}
		return builder.toString();
	}

	public boolean matches(CharSequence tagname,
			Map<CharSequence, CharSequence> attr, int index) {
		// MATCH TAGNAME
		if (!"*".equals(this.tagname) && !tagname.equals(this.tagname))
			return false;

		// MATCH ID
		else if (this.id != null && !this.id.equals(attr.get("id")))
			return false;

		// MATCH ATTRIBUTES
		if (this.attributes.size() != 0) {
			if (!matchAttributes(attr))
				return false;
		}

		// MATCH NTH-CHILD
		if (nthChild > 0 && index != nthChild)
			return false;

		return true;
	}

	private boolean matchAttributes(Map<CharSequence, CharSequence> attr) {
		for (List<AttributeMatcher> matchers : this.attributes.values()) {
			for (AttributeMatcher matcher : matchers) {
				if (!attr.containsKey(matcher.name))
					return false;
				String value = attr.get(matcher.name).toString();

				switch (matcher.matcherType) {
				case ENDS_WITH:
					if (!value.endsWith(matcher.pattern))
						return false;
					break;
				case EQUALS:
					if (!value.equals(matcher.pattern))
						return false;
					break;
				case EXISTS:
					break;
				case IS_LANG:
					if (!value.startsWith(matcher.pattern + "-"))
						return false;
					break;
				case LIST_CONTAINS:
					String[] list = value.split(" ");
					Arrays.sort(list);
					if (Arrays.binarySearch(list, matcher.pattern) < 0)
						return false;
					break;
				case STARTS_WITH:
					if (!value.startsWith(matcher.pattern))
						return false;
					break;
				case SUBSTRING:
					if (value.indexOf(matcher.pattern) < 0)
						return false;
					break;
				}
			}
		}
		return true;

	}
}
