package com.czdsb.demo.controller;

import com.czdsb.demo.interceptor.WebAppConfig;
import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.Record;
import com.czdsb.demo.service.CameraService;
import com.czdsb.demo.websocket.MessageSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CameraController {

    @Autowired
    private CameraService cameraService;


    private static final Map<String, String> monthMap = new HashMap<String, String>() {{
        put("January", "01");
        put("February", "02");
        put("March", "03");
        put("April", "04");
        put("May", "05");
        put("June", "06");
        put("July", "07");
        put("August", "08");
        put("September", "09");
        put("October", "10");
        put("November", "11");
        put("December", "12");
    }};

    @PostMapping("/camera/connect")
    @ResponseBody
    String connect(@RequestBody Camera camera) {
        if (!login(camera))
            return "fail";
        cameraService.updateTime(camera);
        return "success";
    }

//    @PostMapping("/login/camera/upload")
//    @ResponseBody
//    String up(HttpServletRequest request) {
//
//        Record record = new Record();
//        record.setCameraId(request.getParameter("cameraId"));
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            record.setInvadeTime(df.parse(request.getParameter("invadeTime")));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println("record");
//        System.out.println(record);
//        record = cameraService.createRecord(record);
//        System.out.println(record);
//        return "fail";
//    }
    @RequestMapping("/camera/getType")
    @ResponseBody
    String getType(HttpServletRequest request){
        String id = request.getParameter("id");
        System.out.println(id);
        return "zxc";
    }

    @PostMapping("/camera/upload")
    @ResponseBody
    String fileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return "empty file";
        String fail = "fail";
        String success = "success";
        Camera camera;
        try {
            Record record = new Record();
            String filename = file.getOriginalFilename();
            System.out.println(filename);
            String[] info = filename.split("-");
            camera = new Camera(info[0],info[1]);
            if(!login(camera))
                return fail;
            String type = info[9];
            if(type.equals(""))
                return fail;
            StringBuilder time = new StringBuilder(info[2]).append("-");
            if (monthMap.containsKey(info[3]))
                time.append(monthMap.get(info[3])).append("-");
            else return fail;
            time.append(info[4]).append(" ");
            Integer hour = Integer.valueOf(info[5]);
            switch (info[8]) {
                case "am":
                    break;
                case "AM":
                    break;
                case "pm":
                    hour += 12;
                    break;
                case "PM":
                    hour += 12;
                    break;
                default:
                    return fail;
            }
            time.append(hour.toString()).append(":");
            time.append(info[6]).append(":");
            time.append(info[7]);
            record.setCameraId(camera.getCameraId());
            record.setInvadeTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time.toString()));
            record.setInvadeType(type);
            record = cameraService.createRecord(record);

            System.out.println(record);

            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(new File(
                            "src\\main\\resources\\invadeReport\\" + record.getRecordId() + ".jpg")));

            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return fail;
        }
        MessageSocket.noticeUser(camera.getCameraId());
        return success;
    }

    private Boolean login(Camera camera) {
        if (camera.getCameraId().equals("") || camera.getPassword().equals(""))
            return false;
        Camera c = cameraService.findById(camera.getCameraId());
        return c != null && c.getPassword().equals(camera.getPassword());
    }
}