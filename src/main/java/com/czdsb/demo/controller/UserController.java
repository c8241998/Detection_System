package com.czdsb.demo.controller;

import com.czdsb.demo.interceptor.WebAppConfig;
import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.CameraToUser;
import com.czdsb.demo.model.Record;
import com.czdsb.demo.model.User;
import com.czdsb.demo.service.CameraService;
import com.czdsb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    private static final int PASSWORD_MIN_LENGTH = 6;

    @Autowired
    private UserService userService;

    @Autowired
    private CameraService cameraService;

    @PostMapping("/login")
    @ResponseBody
    String login(@RequestBody User user, HttpSession session) {

        System.out.println("\n" + user.getUsername() + ":" + user.getPassword() + "  login.");

        if (user.getUsername().equals(""))
            return "username null";
        if (user.getPassword().length() < PASSWORD_MIN_LENGTH)
            return "password short";
        User u = userService.findByUsername(user.getUsername());
        if (u == null)
            return "no user";
        if (user.getPassword().equals(u.getPassword())) {
            session.setAttribute(WebAppConfig.SESSION_KEY, u);
            return "success";
        }
        return "fail";
    }

    @PostMapping("/signup")
    @ResponseBody
    String signup(@RequestBody User user, HttpSession session) {
        System.out.println("\n" + user.getUsername() + ":" + user.getPassword() + "  " + user.getName() + " sign up.");

        if (user.getUsername().equals(""))
            return "username null";
        if (user.getPassword().length() < PASSWORD_MIN_LENGTH)
            return "password short";
        User u = userService.findByUsername(user.getUsername());
        if (u != null)
            return "user existed";
        userService.createUser(user);
        session.setAttribute(WebAppConfig.SESSION_KEY, user);
        return "success";
    }

    @PostMapping("/admin/user/update")
    @ResponseBody
    String update(@RequestBody User user, HttpSession session) {
        user.setUserId(((User) session.getAttribute(WebAppConfig.SESSION_KEY)).getUserId());
        if (!user.getName().equals("")) {
            userService.modifyName(user);
        }
        if (!user.getPhone().equals("")) {
            if (!user.getPhone().matches("1[34578][0-9]{9}"))
                return "phone error";
            userService.modifyPhone(user);
        }
        if (!user.getPassword().equals("")) {
            if (user.getPassword().length() < PASSWORD_MIN_LENGTH)
                return "password short";
            userService.modifyPassword(user);
        }
        return "success";
    }

    @PostMapping("/admin/user/bind")
    @ResponseBody
    String bind(@RequestBody Camera camera, HttpSession session) {
        if (camera.getCameraId().equals(""))
            return "id null";
        if (camera.getPassword().equals(""))
            return "password null";
        Camera c = cameraService.findById(camera.getCameraId());
        if (c == null)
            return "camera not exist";
        if (!c.getPassword().equals(camera.getPassword()))
            return "fail";
        User user = (User) session.getAttribute(WebAppConfig.SESSION_KEY);
        if (userService.findByCamera(camera) != null)
            return "bind exist";
        userService.bindWithCamera(user, camera);
        return "success";
    }

    @GetMapping("/admin")
    String admin(HttpSession session) {
        return "admin";
    }

    @GetMapping("/admin/user")
    @ResponseBody
    String getUser(HttpSession session) {
        User user = (User) session.getAttribute(WebAppConfig.SESSION_KEY);
        user = userService.findByUsername(user.getUsername());
        return user.getUsername() + "\n" + user.getName() + "\n" + user.getPhone() + "\n" + user.getUserId();
    }

    @GetMapping("/admin/cameras")
    @ResponseBody
    String getCameras(HttpSession session) {
        User user = (User) session.getAttribute(WebAppConfig.SESSION_KEY);
        List<Camera> cameras = cameraService.findByUser(user);
        StringBuilder message = new StringBuilder();
        for (Camera camera : cameras) {
            message.append(camera.getCameraId()).append("\n").append(
                    (camera.getLastAccessedTime() == null || System.currentTimeMillis() - camera.getLastAccessedTime().getTime() > WebAppConfig.OVERDUE) ? "off" : "on"
            ).append("\n");
        }
        return message.toString();
    }

    @PostMapping("/admin/camera/recordsNum")
    @ResponseBody
    String getRecordsNum(@RequestBody Camera camera) {
        return cameraService.findRecordsNum(camera).toString();
    }

//    @PostMapping("/admin/camera/record")
//    @ResponseBody
//    String getRecordsNum(@RequestBody Record record) {
//        return cameraService.findRecordsNum(camera).toString();
//    }

//    @RequestMapping("/changeInfo")
//    String changeInfo(@RequestBody User user,HttpServletResponse response) {
//        response.reset();
//        String username=user.getusername();
//        String password=user.getpassword();
//        String name=user.getname();
//        JSONObject result = new JSONObject();
//
//        try{
//            User user_mysql = userService.findByUsername(username);
//            result.put("msg", "sameuser");
//        }
//        catch(EmptyResultDataAccessException e1){
//
//        }
//        writeJson(response, result);
//        return "/index";
//    }

//    @RequestMapping("/admin")
//    String admin(HttpServletRequest request) {
//        if (WebSocketTest.getUsername().compareTo("null") == 0) {
//            return "/login";
//        } else {
//            return "/admin";
//        }
//
//    }
//
//    public void writeJson(HttpServletResponse resp, JSONObject json) {
//        PrintWriter out = null;
//        try {
//            //设定类容为json的格式
//            resp.setContentType("application/json;charset=UTF-8");
//            out = resp.getWriter();
//            //写到客户端
//            out.write(json.toJSONString());
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
}