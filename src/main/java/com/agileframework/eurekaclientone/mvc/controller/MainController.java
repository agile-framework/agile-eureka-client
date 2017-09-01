package com.agileframework.eurekaclientone.mvc.controller;

import com.agileframework.eurekaclientone.common.base.Head;
import com.agileframework.eurekaclientone.common.base.RETURN;
import com.agileframework.eurekaclientone.common.server.ServiceInterface;
import com.agileframework.eurekaclientone.common.util.ObjectUtil;
import com.agileframework.eurekaclientone.common.util.ServletUtil;
import com.agileframework.eurekaclientone.common.util.StringUtil;
import com.netflix.discovery.EurekaClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 主控制层
 * Created by 佟盟 on 2017/8/22
 */
@Controller
@Scope("prototype")
public class MainController {

    //日志工具
    private Logger logger = LogManager.getLogger(this.getClass());
    //上下文
    private final ApplicationContext applicationContext;
    //服务对象
    private ServiceInterface service;
    //工程名
    @Value("${spring.application.name}")
    private String moduleName;

    private final EurekaClient eurekaClient;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public MainController(ApplicationContext applicationContext, @Qualifier("eurekaClient") EurekaClient eurekaClient, DiscoveryClient discoveryClient) {
        this.applicationContext = applicationContext;
        this.eurekaClient = eurekaClient;
        this.discoveryClient = discoveryClient;
    }

    /**
     * 获取微服务主页地址
     * @return 微服务主页地址
     */
    @GetMapping("/get-home-page-url")
    public String homePageUrl(){
        return this.eurekaClient.getNextServerFromEureka(moduleName,false).getHomePageUrl();
    }

    /**
     * 获取eureka客户端信息
     * @return eureka客户端信息
     */
    @GetMapping("/get-eureka-client-info")
    public List<ServiceInstance> eurekaInfo(){
        return this.discoveryClient.getInstances(moduleName);
    }

    /**
     * 非法请求处理器
     * @param request 请求对象
     * @return 响应视图
     */
    @RequestMapping(value = {"/","/*","/*/*","/*/*/*/*/**"})
    public ModelAndView processor(HttpServletRequest request){
        //初始化参数
        ModelAndView modelAndView = new ModelAndView();

        //判断模块存在
        modelAndView.addObject("head",new Head(RETURN.NO_COMPLETE,request));
        return modelAndView;
    }

    /**
     * agile框架处理器
     * @param request 请求对象
     * @param response 响应对象
     * @param service 服务名
     * @param method 方法名
     * @param forward 转发信息
     * @param authToken 认证令牌
     * @return 响应试图数据
     * @throws IOException 流异常
     * @throws IllegalAccessException 非法访问异常
     * @throws IllegalArgumentException 非法参数异常
     * @throws InvocationTargetException 调用目标异常
     * @throws NoSuchMethodException 没有这样的方法异常
     * @throws SecurityException 安全异常
     */
    @RequestMapping(value = "/{module}/{service}/{method}")
    public ModelAndView processor(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String module,
            @PathVariable String service,
            @PathVariable String method,
            @RequestParam(value = "forward", required = false) String forward,
            @RequestParam(value = "auth-token", required = false) String authToken,
            @RequestParam(value = "file-path", required = false) String filePath
    ) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        //初始化参数
        ModelAndView modelAndView = new ModelAndView();//响应视图对象
        service = StringUtil.toLowerName(service);//设置服务名
        method = StringUtil.toLowerName(method);//设置方法名

        //判断模块存在
        if (StringUtil.isEmpty(module) || !module.equals(moduleName)) {
            modelAndView.addObject("head", new Head(RETURN.NO_MODULE, request));
            return modelAndView;
        }

        //判断服务存在
        if (StringUtil.isEmpty(service) || ObjectUtil.isEmpty(this.getService(service))) {
            modelAndView.addObject("head", new Head(RETURN.NO_SERVICE, request));
            return modelAndView;
        }

        //判断方法存在
        if (StringUtil.isEmpty(method)) {
            modelAndView.addObject("head", new Head(RETURN.NO_METHOD, request));
            return modelAndView;
        }

        //调用目标方法前处理入参
        handleRequestUrl(request, authToken, service, method);

        //调用目标方法
        RETURN returnState = this.getService().executeMethod(method,this.applicationContext.getBean(service));

        //判断是否存在文件上传
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!StringUtil.isEmpty(filePath) && multipartResolver.isMultipart(request)){
            this.upLoadFile(multipartResolver, request, filePath);
        }

        //判断是否转发
        if (!StringUtil.isEmpty(forward) && RETURN.SUCCESS.equals(returnState)) {

            //过滤转发并获取请求参数，避免重复转发
            String afterParam = request.getQueryString().replaceFirst("forward[-_*%#$@+=()^!~`|.,/a-zA-Z0-9]+[&]?", "");

            //服务间参数传递
            String beforeParam = StringUtil.fromMapToUrl(this.getService().getOutParam());

            //转发
            modelAndView.setView(new RedirectView(forward + (StringUtil.isEmpty(afterParam + beforeParam) ? "" : "?" + afterParam + beforeParam)));

            return modelAndView;
        }

        //调用目标方法后处理视图
        modelAndView.addObject("head", new Head(returnState, request));

        //响应数据装填
        modelAndView.addObject("result", this.getService().getOutParam());

        return modelAndView;
    }

    /**
     * 根据服务名在Spring上下文中获取服务bean
     * @param serviceName   服务名
     * @return  服务bean
     */
    private ServiceInterface getService(String serviceName) {
        try {
            Object serviceTry = this.applicationContext.getBean(serviceName);
            service = (ServiceInterface) serviceTry;
            this.setService(service);
            return service;
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 根据servlet请求、认证信息、目标服务名、目标方法名处理入参
     * @param request   servlet请求
     * @param authToken 认证信息
     * @param service   目标服务名
     * @param method    目标方法名
     * @throws IOException 流异常
     */
    private void handleRequestUrl(HttpServletRequest request, String authToken,String service,String method) throws IOException {
        HashMap<String, Object> inParam = new HashMap<>();
        inParam.put("authToken",authToken);
        inParam.put("app",moduleName);
        inParam.put("service",service);
        inParam.put("method",method);
        inParam.put("ip", ServletUtil.getCustomerIPAddr(request));
        inParam.put("url", request.getRequestURL());

        //---------------------------------请求参数解析------------------------------------
        String queryString = request.getQueryString();
        if (!StringUtil.isEmpty(queryString)){
            String[] params = queryString.split("&"),paramContainer;
            for (int i = 0 ; i < params.length ; i++) {
                paramContainer = params[i].split("=");
                if (paramContainer.length == 2){
                    inParam.put(paramContainer[0],paramContainer[1]);
                }
            }
        }

        //将处理过的所有请求参数传入调用服务对象
        this.getService().setInParam(inParam);
    }

    /**
     * 文件下载
     * @param request  请求对象
     * @param path  文件存储路径
     * @throws IOException 流异常
     */
    private void upLoadFile(CommonsMultipartResolver multipartResolver, HttpServletRequest request, String path) throws NoSuchFieldException, IOException {
        List<HashMap<String,Object>> list = new ArrayList<>();

        //转换成多部分request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        //获取所有文件提交的input名
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        while (iterator.hasNext()) {
            List<MultipartFile> files = multipartHttpServletRequest.getFiles(iterator.next());
            for (int i = 0 ; i < files.size();i++){
                MultipartFile file = files.get(i);
                if (!ObjectUtil.isEmpty(file)) {
                    //取得当前文件名
                    String fileName = file.getOriginalFilename();
                    //判断文件是否存在
                    if (!StringUtil.isEmpty(fileName)) {
                        File newFile = new File(path + fileName);
                        file.transferTo(newFile);
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("fileName",file.getOriginalFilename());
                        map.put("fileSize",file.getSize());
                        map.put("contentType",file.getContentType());
                        list.add(map);
                    }
                }
            }
        }
        service.setOutParam("upLoadFile",list);
    }

    private ServiceInterface getService() {
        return service;
    }

    private void setService(ServiceInterface service) {
        this.service = service;
    }

}
