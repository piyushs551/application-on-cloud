package com.csye6225.demo.services;

import com.csye6225.demo.bean.Attachment;
import com.csye6225.demo.bean.Task;
import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.AttachmentRepository;
import com.csye6225.demo.repository.TasksRepository;
import com.csye6225.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TasksRepository tasksRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    S3Service s3Service;

    public boolean saveTask(Task task) {
        Task t = tasksRepository.save(task);
        if (t == null) {
            return false;
        }
        return true;
    }

    public boolean saveTaskToS3(MultipartFile file, String taskKey) {
        System.out.println("its here");
        s3Service = new S3Service();
        try {
            s3Service.uploadObjectToS3(file, taskKey);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean taskExists(UUID taskId) {
        Task t = tasksRepository.findByTaskID(taskId);
        if (t == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteTask(UUID taskId, User user) {

        Task t = tasksRepository.findByTaskID(taskId);
        Set<Task> tset = user.getTaskSet();
        Iterator it = tset.iterator();
        while (it.hasNext()) {
            Task present = (Task) it.next();
            System.out.println(present + ":" + t);

            if (present.getTaskID() == t.getTaskID()) {
                user.getTaskSet().remove(t);
                userRepository.save(user);
                tasksRepository.deleteTaskByTaskID(taskId);
                return true;
            }
        }
        return false;
    }

    public Task getTask(UUID tid, User user) {
        Task t = tasksRepository.findByTaskID(tid);
        Set<Task> tset = user.getTaskSet();
        Iterator it = tset.iterator();
        while (it.hasNext()) {
            Task present = (Task) it.next();
            System.out.println(present + ":" + t);
            if (present.getTaskID() == t.getTaskID()) {
                return t;
            }
        }
        return null;
    }

    public List<Task> getAllTasks(User user) {
        List<Task> tasks = tasksRepository.findByUser(user);
        return tasks;
    }

    public Attachment getAttachment(int attId) {
        Attachment att = attachmentRepository.findByAttID(attId);
        return att;
    }

    public boolean deleteAttachement(int attId, UUID taskId, User user) throws IOException {
        Task task = getTask(taskId, user);
        Attachment att = getAttachment(attId);
        Path path = Paths.get(att.getPath());
        task.getAttachmentSet().remove(att);
        tasksRepository.save(task);
        attachmentRepository.deleteAllByAttID(attId);
        return Files.deleteIfExists(path);
    }

    public String saveToDisk(MultipartFile file, UUID taskID) {
        String home = System.getProperty("java.io.tmpdir");
        String dirpath = home + File.separator + taskID.toString();

        File dir = new File(dirpath);

        boolean b = false;

        if (dir.isDirectory()) {
            b = true;
        } else {
            b = dir.mkdir();
        }

        String path = dirpath + File.separator + file.getOriginalFilename();

        File f = new File(path);

        try {
            file.transferTo(f);
            return path;
        } catch (IOException e) {
            System.out.println("File Upload failed");
            e.printStackTrace();
        }

        return null;
    }
}