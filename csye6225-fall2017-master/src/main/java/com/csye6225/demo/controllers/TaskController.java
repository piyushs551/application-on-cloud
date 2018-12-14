package com.csye6225.demo.controllers;

import com.csye6225.demo.bean.Attachment;
import com.csye6225.demo.bean.Task;
import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.UserRepository;
import com.csye6225.demo.services.TaskService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Parser;
import java.util.*;

@Controller
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllTasks(){
        JsonObject jsonObject = new JsonObject();

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            jsonObject.addProperty("message", "you are not logged in!!!");
        } else {
           String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            jsonObject.addProperty("UserName",""+user.getUsername());
            Set<Task> taskList = user.getTaskSet();
            Iterator it = taskList.iterator();
            JsonArray arr = new JsonArray();
            while(it.hasNext())  {
                Task t = (Task)it.next();
                JsonObject temp = new JsonObject();
                String desc = t.getDescription();
                desc = desc.trim();
                if(desc.startsWith("\"")){
                    desc = desc.substring(1,desc.length());
                }
                if(desc.endsWith("\"")){
                    desc = desc.substring(0,desc.length()-1);
                }
                System.out.println(desc);
                temp.addProperty("TaskID",""+ t.getTaskID().toString());
                temp.addProperty("TaskDescription",""+desc);
                arr.add(temp);

            }
            jsonObject.add("Tasks", arr);
        }

        return jsonObject.toString();
    }


    @RequestMapping(value = "/tasks", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String saveTask(HttpEntity<String> entity){
        String json = entity.getBody();
        JsonObject jsonObject = new JsonObject();
        JsonObject inputJsonObject;
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        inputJsonObject = jsonElement.getAsJsonObject();

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            jsonObject.addProperty("message", "you are not logged in!!!");
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            jsonObject.addProperty("UserName",""+user.getUsername());
            Task t = new Task();
            t.setDescription(inputJsonObject.get("description").toString());
            t.setUser(user);
            user.getTaskSet().add(t);
            taskService.saveTask(t);
            jsonObject.addProperty("CreationStatus", "Created");
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateTask(@PathVariable("id") UUID taskID, HttpEntity<String> entity){
        String json = entity.getBody();
        JsonObject jsonObject = new JsonObject();
        JsonObject inputJsonObject;
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        inputJsonObject = jsonElement.getAsJsonObject();
        System.out.println(inputJsonObject.get("description").toString());
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            jsonObject.addProperty("message", "you are not logged in!!!");
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            jsonObject.addProperty("UserName",""+user.getUsername());
            Task t = taskService.getTask(taskID,user);
            if(t != null){
                t.setDescription(inputJsonObject.get("description").toString());
                taskService.saveTask(t);
                jsonObject.addProperty("UpdateStatus", "Updated");
            }else{
                jsonObject.addProperty("Error","403");
                jsonObject.addProperty("Message","Access Forbidden");
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

        }

        return ResponseEntity.ok(jsonObject.toString());
    }

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity saveFile(@PathVariable("id") UUID taskID, @RequestParam("file") MultipartFile file)
    {
        JsonObject jsonObject = new JsonObject();
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            jsonObject.addProperty("message", "you are not logged in!!!");
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            jsonObject.addProperty("UserName",""+user.getUsername());
            Task t = taskService.getTask(taskID, user);
            if(t!=null) {
                //String path = taskService.saveToDisk(file, taskID);
                String taskKey = user.getUsername() + "-" + taskID.toString() + "-" + file.getOriginalFilename();
                taskService.saveTaskToS3(file, taskKey);

                Attachment attachment = new Attachment();

                //attachment.setPath(path);
                attachment.setPath(taskKey);

                t.getAttachmentSet().add(attachment);
                attachment.setTask(t);
                taskService.saveTask(t);
                jsonObject.addProperty("UpdateStatus", "Updated");
            }else{
                jsonObject.addProperty("Error","403");
                jsonObject.addProperty("Message","Access Forbidden");
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(jsonObject.toString());
           }
        }
        return ResponseEntity.ok(jsonObject.toString());
        //return ResponseEntity(jsonObject.toString(), HttpStatus.OK); //jsonObject.toString();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity deleteTask(@PathVariable("id") UUID taskID){
        JsonObject json = new JsonObject();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        json.addProperty("UserName",""+user.getUsername());
        boolean exists = taskService.taskExists(taskID);
        if(exists) {
            if(taskService.deleteTask(taskID, user)){
                // successfully deleted message
                System.out.print("Success");
                json.addProperty("Message","Successfully deleted");
            }else{
                System.out.print("access Forbidden");
                // access forbidden
                json.addProperty("Error","403");
                json.addProperty("Message","Access Forbidden");
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(json.toString());
            }
        }else{
            System.out.print("Bad Request");
            // bad request
            json.addProperty("Error","400");
            json.addProperty("Message","Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json.toString());
        }

        return ResponseEntity.ok(json.toString());
    }

    @RequestMapping(value="/tasks/{id}/attachments/{idAttachments}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteAttachment(@PathVariable("id") UUID taskID, @PathVariable("idAttachments") int attId)
    {
        JsonObject jsonObject = new JsonObject();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        jsonObject.addProperty("UserName",""+user.getUsername());
        Task t = taskService.getTask(taskID, user);
        if(t == null){
            jsonObject.addProperty("Error","403");
            jsonObject.addProperty("Message","Access Forbidden");
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(jsonObject.toString());
        }
        Attachment att = taskService.getAttachment(attId);
        if(att == null){
            jsonObject.addProperty("Error","400");
            jsonObject.addProperty("Message","Bad Request");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        Iterator<Attachment> ite = t.getAttachmentSet().iterator();
        boolean contains = false;
        while (ite.hasNext()){
            Attachment temp = ite.next();
            if(temp.getPathID() == attId){
                contains = true;
            }
        }
        if(!contains){
            jsonObject.addProperty("Error","403");
            jsonObject.addProperty("Message","Access Forbidden");
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(jsonObject.toString());
        }
        boolean deleted = false;
        try{
            deleted = taskService.deleteAttachement(attId,taskID,user);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(deleted)
            jsonObject.addProperty("Message", "Delete Successful");
        else
            jsonObject.addProperty("Message", "Delete Failed");
        return ResponseEntity.ok(jsonObject.toString());
    }

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity listAttachments(@PathVariable("id") UUID taskId){
        JsonObject jsonObject = new JsonObject();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        Task task = taskService.getTask(taskId, user);
        if(task == null){
            jsonObject.addProperty("Error","403");
            jsonObject.addProperty("Message","Access Forbidden");
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(jsonObject.toString());
        }

        Iterator itr = user.getTaskSet().iterator();
        JsonArray arr = new JsonArray();

        while(itr.hasNext()){

            Task t = (Task) itr.next();
            Iterator temp = t.getAttachmentSet().iterator();
            while(temp.hasNext()){
                JsonObject obj = new JsonObject();
                obj.addProperty("Attachment-path",((Attachment) temp.next()).getPath());
                arr.add(obj);
            }

        }
        jsonObject.add("Attachments",arr);
        return ResponseEntity.ok(jsonObject.toString());
    }

}