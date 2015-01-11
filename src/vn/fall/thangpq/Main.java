package vn.fall.thangpq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws IOException {
	// write your code here

        LineNumberReader lr = new LineNumberReader(new FileReader("demo.xml"));
        Stack<String> stack = new Stack<String>();
        String result = "Stack is empty";
        try {
            //StringBuilder sb = new StringBuilder();
            String line = lr.readLine();

           mainLoop: while (line != null) {

                while (true){
                    String extractedTag;
                    if (line.indexOf("<") != -1){
                        extractedTag = line.substring(line.indexOf("<"), line.indexOf(">") + 1);

                        if (isOpenTag(extractedTag)){
                            if (!isValidTagName(getTagName(extractedTag))){
                                result = "Invalid tag name. Error line: " + lr.getLineNumber();
                                break mainLoop;
                            }
                            stack.add(extractedTag);
                            line = line.replace(extractedTag,"");
                            if (line.indexOf("<") != -1){
                                continue ;
                            }
                            else {
                                line = lr.readLine();
                                continue mainLoop;
                            }

                        } else if (isEndTag(extractedTag)){
                            if (!isValidTagName(getTagName(extractedTag))){
                                result = "Invalid tag name. Error line: " + lr.getLineNumber();
                                break mainLoop;
                            }

                            if (getTagName(extractedTag).equals(getTagName(stack.lastElement()))){
                                stack.remove(stack.lastElement());
                                line = line.replace(extractedTag,"");
                                if (line.indexOf("<") != -1){
                                    continue;
                                } else {
                                    line = lr.readLine();
                                    continue mainLoop;
                                }

                            } else {
                                result = "Error: "  + lr.getLineNumber();
                                break mainLoop;
                            }
                        } else if (isEmptyTag(extractedTag)){
                            if (!isValidTagName(getTagName(extractedTag))){
                                result = "Invalid tag name. Error line: " + lr.getLineNumber();
                                break mainLoop;
                            }

                            line = line.replace(extractedTag,"");
                            if (line.indexOf("<") != -1){
                                continue;
                            } else {
                                line = lr.readLine();
                                continue mainLoop;
                            }
                        }
                    } else {
                        line = lr.readLine();
                        continue ;
                    }
                }

            }

            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lr.close();
        }
    }

    private static String getTagName(String tag) {
        String tagName = "";
        if (isOpenTag(tag)){
            tagName = tag.replace("<", "").replace(">", "");
        } else if (isEndTag(tag)){
            tagName = tag.replace("</", "").replace(">", "");
        } else if (isEmptyTag(tag)){
            tagName = tag.replace("<", "").replace("/>", "");
        }
        return tagName;
    }

    public static boolean isOpenTag(String element){
        Pattern openTagPattern = Pattern.compile("<(.*?)>");
        Matcher m = openTagPattern.matcher(element);
        if (m.find() && !element.startsWith("</") && !element.endsWith("/>")){

            return true;
        }
        return false;
    }

    public static boolean isEndTag(String element){
        Pattern endTagPattern = Pattern.compile("</(.*?)>");
        Matcher m = endTagPattern.matcher(element);
        if (m.find() && !element.endsWith("/>")){

            return true;
        }
        return false;
    }


    public static boolean isEmptyTag(String element){
        Pattern emptyTagPattern = Pattern.compile("<(.*?)/>");
        Matcher m = emptyTagPattern.matcher(element);
        if (m.find() && !element.startsWith("</")){
            return true;
        }
        return false;
    }


    //Check if the name of tag is valid
    private static boolean isValidTagName(String tagName) {

        return !tagName.toLowerCase().startsWith("xml") && Character.isLetter(tagName.charAt(0)) && !tagName.contains(" ")
                && !tagName.contains("<") && !tagName.contains(">") && !tagName.contains("/");
    }
}
