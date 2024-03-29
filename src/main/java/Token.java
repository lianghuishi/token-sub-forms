import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class Token {

    private static final String TOKEN_LIST_NAME = "tokenList";
    public static final String TOKEN_STRING_NAME = "token";

    private static ArrayList getTokenList(HttpSession session) {
        Object obj = session.getAttribute(TOKEN_LIST_NAME);
        if (obj != null) {
            return (ArrayList) obj;
        } else {
            ArrayList tokenList = new ArrayList();
            session.setAttribute(TOKEN_LIST_NAME, tokenList);
            return tokenList;
        }
    }

    private static void saveTokenString(String tokenStr, HttpSession session) {
        ArrayList tokenList = getTokenList(session);
        tokenList.add(tokenStr);
        session.setAttribute(TOKEN_LIST_NAME, tokenList);
    }

    private static String generateTokenString(){
        return new Long(System.currentTimeMillis()).toString();
    }

/** *//**
     * Generate a token string, and save the string in session, then return the token string.
     * @param HttpSession session
     * @return a token string used for enforcing a single request for a particular transaction.
     */

    public static String getTokenString(HttpSession session) {
        String tokenStr = generateTokenString();
        saveTokenString(tokenStr, session);
        return tokenStr;
    }

/** *//**
     * check whether token string is valid. if session contains the token string, return true.
     * otherwise, return false.
     * @param String tokenStr
     * @param HttpSession session
     * @return true: session contains tokenStr; false: session is null or tokenStr is id not in session
     */

    public static boolean isTokenStringValid(String tokenStr, HttpSession session) {
        boolean valid = false;
        if(session != null){
            ArrayList tokenList = getTokenList(session);
            if (tokenList.contains(tokenStr)) {
                valid = true;
                tokenList.remove(tokenStr);
            }
        }
        return valid;
    }


/**

 （二）JSP页面中
 1：先import该Token工具类

 <%@ page import="com.company.util.Token" %>
 2：在表单中添加隐藏的Token值
 <form>
 <input type="hidden" name="<%=Token.TOKEN_STRING_NAME %>" value="<%=Token.getTokenString(session) %>">
 </form>
 （三）在服务器端Servlet中添加如下代码

 if(Token.isTokenStringValid(request.getParameter(Token.TOKEN_STRING_NAME), request.getSession())){
 //To Do 业务代码
 }




 */



}
