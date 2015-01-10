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
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();


            while (line != null) {

                String result = "INVALID TAG" ;

                if (isFullTag(line.trim())){
                    result = TagType.FULL_TAG;
                } else if (isEndTag(line.trim())){
                    result = TagType.END_TAG;
                } else if (isEmptyTag(line.trim())){
                    result = TagType.EMPTY_TAG;
                } else if (isOpenTag(line.trim())) {
                    result = TagType.OPEN_TAG;
                }
                sb.append(result);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            System.out.println(everything);



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }

    public static boolean isOpenTag(String element){
        Pattern openTagPattern = Pattern.compile("<(.*?)>");
        Matcher m = openTagPattern.matcher(element);
        if (m.find() && !element.startsWith("</") && !element.endsWith("/>")){
            String tagName = element.replace("<", "").replace(">", "");
            if (isValidTagName(tagName)){
                return true;
            }
            return false;
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
                openTagName = element.replace("<", "").replace(">", "");

                element = element.replace(firstString, "");
                String secondString = element.substring(element.indexOf('<'), element.indexOf('>') + 1);
                if (isEndTag(secondString)){
                    closeTagName = element.replace("</", "").replace(">", "");
                }

                if (isValidTagName(openTagName) && isValidTagName(closeTagName)){
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
