package com.csye6225.demo.repository;

import com.csye6225.demo.bean.Task;
import com.csye6225.demo.bean.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface TasksRepository extends CrudRepository<Task, UUID>
{
    public List<Task> findByUser(User user);
    public Task findByTaskID(UUID tid);
    public void deleteTaskByTaskID(UUID tid);
}
