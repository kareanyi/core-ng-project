package core.framework.impl.template.html;

import core.framework.api.util.Exceptions;

/**
 * @author neo
 */
public class HTMLLexer {
    private final String source;
    private final String html;

    int startIndex;
    int currentIndex;

    int currentLine = 1;
    int currentColumn = 1;

    public HTMLLexer(String source, String html) {
        this.source = source;
        this.html = html;
    }

    public HTMLTokenType nextNodeToken() {
        reset();

        if (currentIndex >= html.length()) {
            return HTMLTokenType.EOF;
        } else if (match(currentIndex, "<!--")) {
            move(4);
            return HTMLTokenType.COMMENT_START;
        } else if (match(currentIndex, "</")) {
            move(findCloseElementLength());
            return HTMLTokenType.END_TAG;
        } else if (isStartTag(currentIndex)) {
            move(findStartElementLength());
            return HTMLTokenType.START_TAG;
        } else {
            move(findTextLength());
            return HTMLTokenType.TEXT;
        }
    }

    public HTMLTokenType nextElementToken() {
        skipWhitespaces();

        if (currentIndex >= html.length()) {
            return HTMLTokenType.EOF;
        } else if (match(currentIndex, ">")) {
            move(1);
            return HTMLTokenType.START_TAG_END;
        } else if (match(currentIndex, "/>")) {
            move(2);
            return HTMLTokenType.START_TAG_END_CLOSE;
        } else if (match(currentIndex, "=")) {
            move(findAttrValueLength());
            return HTMLTokenType.ATTR_VALUE;
        } else {
            move(findAttrNameLength());
            return HTMLTokenType.ATTR_NAME;
        }
    }

    public HTMLTokenType nextCommentEndToken() {
        reset();

        int length = -1;
        for (int i = currentIndex; i < html.length() - 3; i++) {
            if (match(i, "-->")) {
                length = i - currentIndex;
                break;
            }
        }
        if (length == -1) throw Exceptions.error("comment is not closed, location={}", currentLocation());
        move(length);

        return HTMLTokenType.COMMENT_END;
    }

    public HTMLTokenType nextScriptToken(String tagName) {
        reset();
        String closeTag = "</" + tagName + ">";
        int length = -1;
        int maxLength = html.length() - closeTag.length();
        for (int i = currentIndex; i < maxLength; i++) {
            if (match(i, closeTag)) {
                length = i - currentIndex;
                break;
            }
        }
        if (length == -1) throw Exceptions.error("script/css is not closed, location={}", currentLocation());
        move(length);
        return HTMLTokenType.TEXT;
    }

    private void skipWhitespaces() {
        int length = 0;
        for (int i = currentIndex; i < html.length(); i++) {
            if (!Character.isWhitespace(html.charAt(i))) {
                break;
            }
            length++;
        }
        if (length > 0) move(length);
        reset();
    }

    private int findStartElementLength() {
        for (int i = currentIndex + 1; i < html.length(); i++) {
            char ch = html.charAt(i);
            if (Character.isWhitespace(ch) || ch == '>' || ch == '/') {
                return i - currentIndex;
            }
        }
        throw Exceptions.error("start tag is invalid, location={}", currentLocation());
    }

    private int findCloseElementLength() {
        for (int i = currentIndex + 2; i < html.length(); i++) {
            char ch = html.charAt(i);
            if (Character.isWhitespace(ch)) break;
            if (ch == '>') {
                return i - currentIndex + 1;
            }
        }
        throw Exceptions.error("close tag is invalid, location={}", currentLocation());
    }

    private int findTextLength() {
        int length = 0;
        for (int i = currentIndex; i < html.length(); i++) {
            if (isStartTag(i) || match(i, "</")) break;
            length++;
        }
        return length;
    }

    private int findAttrNameLength() {
        for (int i = currentIndex; i < html.length(); i++) {
            char ch = html.charAt(i);
            if (Character.isWhitespace(ch) || ch == '=' || ch == '/' || ch == '>') return i - currentIndex;
            if (ch == '<') throw Exceptions.error("attr is invalid, location={}", currentLocation());
        }
        throw Exceptions.error("attr is invalid, location={}", currentLocation());
    }

    private int findAttrValueLength() {
        boolean started = false;
        boolean hasDoubleQuote = false;
        for (int i = currentIndex + 1; i < html.length(); i++) {  // skip first '='
            char ch = html.charAt(i);
            if (!started && !Character.isWhitespace(ch)) {
                started = true;
                if (ch == '"') hasDoubleQuote = true;
            } else if (started) {
                if ((!hasDoubleQuote && (Character.isWhitespace(ch) || ch == '>'))) {
                    return i - currentIndex;
                } else if (ch == '"') {
                    return i - currentIndex + 1;
                }
            } else if (ch == '>' || ch == '/') {
                return i - currentIndex + 1;
            }
        }
        throw Exceptions.error("attr value is invalid, location={}", currentLocation());
    }

    public String currentToken() {
        return html.substring(startIndex, currentIndex);
    }

    public String currentLocation() {
        return source + ":" + currentLine + ":" + currentColumn;
    }

    private boolean isStartTag(int index) {
        if (index + 1 >= html.length()) return false;
        return html.charAt(index) == '<' && Character.isLetter(html.charAt(index + 1));
    }

    private boolean match(int index, String token) {
        if (index + token.length() >= html.length()) return false;
        for (int i = 0; i < token.length(); i++) {
            if (html.charAt(index + i) != token.charAt(i)) return false;
        }
        return true;
    }

    private void reset() {
        startIndex = currentIndex;
    }

    private void move(int length) {
        if (length == 0) throw Exceptions.error("syntax is invalid, L{}:{}", currentLine, currentColumn);
        for (int i = 0; i < length; i++) {
            char ch = html.charAt(currentIndex);
            if (ch == '\n') {
                currentLine++;
                currentColumn = 1;
            } else {
                currentColumn++;
            }
            currentIndex++;
        }
    }
}