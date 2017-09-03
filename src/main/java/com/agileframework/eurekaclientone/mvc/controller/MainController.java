package com.agileframework.eurekaclientone.mvc.controller;

import com.agileframework.eurekaclientone.common.base.Head;
import com.agileframework.eurekaclientone.common.base.RETURN;
import com.agileframework.eurekaclientone.common.base.Constant;
import com.agileframework.eurekaclientone.common.server.ServiceInterface;
import com.agileframework.eurekaclientone.common.util.ObjectUtil;
import com.agileframework.eurekaclientone.common.util.ServletUtil;
import com.agileframework.eurekaclientone.common.util.StringUtil;
import com.google.common.base.Charsets;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class MainController {

    private final ApplicationContext applicationContext;

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
    @RequestMapping(value = {"/","/*","/*/*/*/**"})
    public ModelAndView processor(HttpServletRequest request){
        //初始化参数
        ModelAndView modelAndView = new ModelAndView();

        //判断模块存在
        modelAndView.addObject(Constant.ResponseAbout.HEAD,new Head(RETURN.NO_COMPLETE,request));
        return modelAndView;
    }

    /**
     * agile框架处理器
     * @param request 请求对象
     * @param response 响应对象
     * @param service 服务名
     * @param method 方法名
     * @param forward 转发信息
     * @return 响应试图数据
     * @throws IOException 流异常
     * @throws IllegalAccessException 非法访问异常
     * @throws IllegalArgumentException 非法参数异常
     * @throws InvocationTargetException 调用目标异常
     * @throws NoSuchMethodException 没有这样的方法异常
     * @throws SecurityException 安全异常
     */
    @RequestMapping(value = "/{service}/{method}")
    public ModelAndView processor(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String service,
            @PathVariable String method,
            @RequestParam(value = "forward", required = false) String forward,
            @RequestParam(value = "file-path", required = false) String filePath
    ) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        //初始化参数
        ModelAndView modelAndView = new ModelAndView();//响应视图对象
        service =  StringUtil.toLowerName(service);//设置服务名
        ServiceInterface serviceProxy = this.getService(StringUtil.toLowerName(service));
        method = StringUtil.toLowerName(method);//设置方法名

        //判断服务存在
        if (StringUtil.isEmpty(service) || ObjectUtil.isEmpty(serviceProxy)) {
            modelAndView.addObject(Constant.ResponseAbout.HEAD, new Head(RETURN.NO_SERVICE, request));
            return modelAndView;
        }

        //判断方法存在
        if (StringUtil.isEmpty(method)) {
            modelAndView.addObject(Constant.ResponseAbout.HEAD, new Head(RETURN.NO_METHOD, request));
            return modelAndView;
        }

        //调用目标方法前处理入参
        handleRequestUrl(request,service,serviceProxy, method);

        //调用目标方法
        RETURN returnState = serviceProxy.executeMethod(method,serviceProxy);

        //判断是否存在文件上传
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!StringUtil.isEmpty(filePath) && multipartResolver.isMultipart(request)){
            this.upLoadFile(request, filePath,serviceProxy);
        }

        //判断是否转发
        if (!StringUtil.isEmpty(forward) && RETURN.SUCCESS.equals(returnState)) {
            StringBuilder url = new StringBuilder(forward);
            if(!forward.startsWith(Constant.RegularAbout.SLASH)){
                url.insert(0,Constant.RegularAbout.SLASH);
            }

            //过滤转发并获取请求参数，避免重复转发
            String beforeParam = request.getQueryString().replaceFirst(Constant.RegularAbout.AFTER_PARAM, Constant.RegularAbout.NULL);

            //服务间参数传递
            String afterParam = StringUtil.fromMapToUrl(serviceProxy.getOutParam());


            url = (StringUtil.isEmpty(beforeParam) && StringUtil.isEmpty(afterParam))?url
                    :(StringUtil.compareTo(beforeParam,afterParam)>0)?url.append(Constant.RegularAbout.QUESTION_MARK).append(beforeParam).append(Constant.RegularAbout.AND).append(afterParam)
                    :url.append(Constant.RegularAbout.QUESTION_MARK).append(afterParam).append(Constant.RegularAbout.AND).append(beforeParam);
            url = url.toString().endsWith(Constant.RegularAbout.AND)?url.deleteCharAt(url.lastIndexOf(Constant.RegularAbout.AND)):url;

            //转发
            modelAndView.setView(new RedirectView(url.toString()));

            return modelAndView;
        }

        //调用目标方法后处理视图
        modelAndView.addObject(Constant.ResponseAbout.HEAD, new Head(returnState, request));

        //响应数据装填
        modelAndView.addObject(Constant.ResponseAbout.RESULT, serviceProxy.getOutParam());

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
            return (ServiceInterface) serviceTry;
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 根据servlet请求、认证信息、目标服务名、目标方法名处理入参
     * @param request   servlet请求
     * @param service   目标服务名
     * @param method    目标方法名
     * @throws IOException 流异常
     */
    private void handleRequestUrl(HttpServletRequest request, String service, ServiceInterface serviceProxy, String method) throws IOException {
        HashMap<String, Object> inParam = new HashMap<>();
        inParam.put(Constant.ResponseAbout.APP,moduleName);
        inParam.put(Constant.ResponseAbout.SERVICE,service);
        inParam.put(Constant.ResponseAbout.METHOD,method);
        inParam.put(Constant.ResponseAbout.IP, ServletUtil.getCustomerIPAddr(request));
        inParam.put(Constant.ResponseAbout.URL, request.getRequestURL());

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
        serviceProxy.setInParam(inParam);
    }

    /**
     * 文件下载
     * @param request  请求对象
     * @param path  文件存储路径
     * @param serviceProxy
     */
    private void upLoadFile(HttpServletRequest request, String path, ServiceInterface serviceProxy){
        List<HashMap<String,Object>> list = new ArrayList<>();

        //转换成多部分request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        //获取所有文件提交的input名
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        while (iterator.hasNext()) {
            List<MultipartFile> files = multipartHttpServletRequest.getFiles(iterator.next());
            for (int i = 0 ; i < files.size();i++){
                MultipartFile file = files.get(i);
                String fileName = file.getOriginalFilename();
                HashMap<String,Object> map = new HashMap<>();
                map.put(Constant.FileAbout.FILE_NAME,fileName);
                map.put(Constant.FileAbout.FILE_SIZE,file.getSize());
                map.put(Constant.FileAbout.CONTENT_TYPE,file.getContentType());

                //判断文件名
                if (StringUtil.isEmpty(fileName)) {
                    map.put(Constant.ResponseAbout.STATE,RETURN.EMPTY_FILENAME.getCode());
                    map.put(Constant.ResponseAbout.MSG,RETURN.EMPTY_FILENAME.getMsg());
                    list.add(map);
                    continue;
                }

                //判断文件内容为空
                if (file.isEmpty()) {
                    map.put(Constant.ResponseAbout.STATE,RETURN.EMPTY_FILE.getCode());
                    map.put(Constant.ResponseAbout.MSG,RETURN.EMPTY_FILE.getMsg());
                    list.add(map);
                    continue;
                }
                File newFile = new File(path,fileName);

                //尝试创建文件夹
                if (!newFile.getParentFile().exists() && !newFile.getParentFile().mkdirs()) {
                    map.put(Constant.ResponseAbout.STATE,RETURN.MADE_DIR_FAIL.getCode());
                    map.put(Constant.ResponseAbout.MSG,RETURN.MADE_DIR_FAIL.getMsg());
                    list.add(map);
                    continue;
                }

                //尝试文件复制
                try {
                    file.transferTo(newFile);
                    map.put(Constant.ResponseAbout.STATE,RETURN.UPLOAD_SUCCESS.getCode());
                    map.put(Constant.ResponseAbout.MSG,RETURN.UPLOAD_SUCCESS.getMsg());

                }catch (Exception e){
                    map.put(Constant.ResponseAbout.STATE,RETURN.UPLOAD_ERROR.getCode());
                    map.put(Constant.ResponseAbout.MSG,RETURN.UPLOAD_ERROR.getMsg());
                }
                list.add(map);
            }
        }
        serviceProxy.setOutParam(Constant.FileAbout.UP_LOUD_FILE_INFO,list);
    }

    /**
     * 文件下载
     * @param path 文件路径
     * @param fileName 文件名
     * @return 文件流
     * @throws FileNotFoundException 流异常
     */
    private ResponseEntity<byte[]> downloadFile(String path ,String fileName) throws FileNotFoundException{
        File file = new File(path,fileName);
        byte[] byteFile;
        try {
            byteFile = FileUtils.readFileToByteArray(file);
        }catch (IOException e){
            throw new FileNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData(Constant.HeaderAbout.ATTACHMENT,new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1));
        return new ResponseEntity<>(byteFile, headers, HttpStatus.CREATED);
    }
}
