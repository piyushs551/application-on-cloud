package com.csye6225.demo.repository;

import com.csye6225.demo.bean.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface  AttachmentRepository extends CrudRepository<com.csye6225.demo.bean.Attachment, Integer>
{
    public List<com.csye6225.demo.bean.Attachment> findByTask(Task t);
    public com.csye6225.demo.bean.Attachment findByAttID(int attID);
    public void deleteAllByAttID(int attID);

}
