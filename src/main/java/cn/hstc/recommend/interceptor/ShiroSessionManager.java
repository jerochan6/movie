//package cn.hstc.recommend.interceptor;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.apache.shiro.web.util.WebUtils;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import java.io.Serializable;
//
///**
// * @ClassName ShiroSessionManager
// * @Description TODO
// * @Author zehao
// * @Date 2020/7/7 9:58
// * @Version 1.0
// **/
//public class ShiroSessionManager extends DefaultWebSessionManager {
//
//    private static final String AUTHORIZATION = "token";
//
//    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";
//
//    public ShiroSessionManager(){
//        super();
//    }
//
//    @Override
//    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
//        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
//        System.out.println("id："+id);
//        if(StringUtils.isEmpty(id)){
//            //如果没有携带id参数则按照父类的方式在cookie进行获取
//            System.out.println("super："+super.getSessionId(request, response));
//            return super.getSessionId(request, response);
//        }else{
//            //如果请求头中有 authToken 则其值为sessionId
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,id);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
//            return id;
//        }
//    }
//}