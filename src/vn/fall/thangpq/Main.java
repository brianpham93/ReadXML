package vn.fall.thangpq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws IOException {
	// write your code here

        BufferedReader br = new BufferedReader(new FileReader("demo.xml"));
        Stack<String> stack = new Stack<String>();
        String result = "";
        try {
            //StringBuilder sb = new StringBuilder();
            String line = br.readLine();

           mainLoop: while (line != null) {
                while (true){
                    String extractedTag = line.substring(line.indexOf("<"), line.indexOf(">" + 1));
                    if (isOpenTag(extractedTag)){
                        stack.add(extractedTag);
                        line = line.replace(extractedTag,"");
                        continue;

                    } else if (isEndTag(extractedTag)){
                        if (getTagName(extractedTag).equals(getTagName(stack.lastElement()))){
                            stack.remove(stack.lastElement());
                            line = line.replace(extractedTag,"");
                            continue;
                        } else {
                            result = "Close tag doesn't match Open tag";
                            break mainLoop;
                        }
                    }
                }
//                String tag = line.trim();
//
//                if ((line.startsWith("<?xml") && line.endsWith(">")) || line.isEmpty()){
//                    line = br.readLine();
//                    continue;
//                }
//
//                if (isOpenTag(tag)){
//                    stack.add(tag);
//                    line = br.readLine();
//                    continue;
//
//                } else if (isEndTag(tag)){
//
//                    if (getTagName(tag).equals(getTagName(stack.lastElement()))){
//                        stack.remove(stack.lastElement());
//                        line = br.readLine();
//                        continue;
//                    } else {
//                        result = "The open tag and close tag do not match: " + stack.lastElement() + " and " + tag;
//                    }
//                } else if (isEmptyTag(tag)){
//                    line = br.readLine();
//                    continue;
//                } else if (isFullTag(line)){
//                    line = br.readLine();
//                    continue;
//                }
            }
            if (stack.empty()){
                result = "Stack is empty";
            }
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
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
            String tagName = element.replace("<", "").replace(">", "");
            if (isValidTagName(tagName)){
                return true;
            } else{

                return false;
            }

        }
        return false;
    }



    public static boolean isEndTag(String element){
        Pattern endTagPattern = Pattern.compile("</(.*?)>");
        Matcher m = endTagPattern.matcher(element);
        if (m.find() && !element.endsWith("/>")){

            String tagName = element.replace("</", "").replace(">", "");
            if (isValidTagName(tagName)){
                return true;
            }
            return false;

        }
        return false;
    }

    public static boolean isEmptyTag(String element){
        Pattern emptyTagPattern = Pattern.compile("<(.*?)/>");
        Matcher m = emptyTagPattern.matcher(element);
        if (m.find() && !element.startsWith("</")){
            String tagName = element.replace("<", "").replace("/>", "");
            if (isValidTagName(tagName)){
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isFullTag(String element){
        Pattern fullTagPattern = Pattern.compile("<(.*?)>(.*?)</(.*?)>");
        Matcher m = fullTagPattern.matcher(element);
        String openTagName = "";
        String closeTagName = "";
        if (m.find()){

            String firstString = element.substring(element.indexOf('<'), element.indexOf('>') + 1);
            if (isOpenTag(firstString)){
                openTagName = getTagName(firstString);

                element = element.replace(firstString, "");

                String secondString = element.substring(element.indexOf('<'), element.indexOf('>') + 1);
                if (isEndTag(secondString)){
                    closeTagName = getTagName(secondString);
                }

                if (isValidTagName(openTagName) && isValidTagName(closeTagName) && openTagName.equals(closeTagName)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    //Check if the name of tag is valid
    private static boolean isValidTagName(String tagName) {

        return !tagName.toLowerCase().startsWith("xml") && Character.isLetter(tagName.charAt(0)) && !tagName.contains(" ")
                && !tagName.contains("<") && !tagName.contains(">") && !tagName.contains("/");
    }
}
