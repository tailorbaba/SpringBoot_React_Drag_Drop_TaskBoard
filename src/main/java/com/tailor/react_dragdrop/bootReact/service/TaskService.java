package com.tailor.react_dragdrop.bootReact.service;

import com.tailor.react_dragdrop.bootReact.exception.TaskNotFoundException;
import com.tailor.react_dragdrop.bootReact.model.Status;
import com.tailor.react_dragdrop.bootReact.model.Task;
import com.tailor.react_dragdrop.bootReact.model.TaskRequest;
import com.tailor.react_dragdrop.bootReact.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task update(TaskRequest taskRequest) {
         Task task = taskRepository.findById(taskRequest.getId())
                            .orElseThrow(() -> new TaskNotFoundException());
         task.setTitle(taskRequest.getTitle());
         task.setDescription(taskRequest.getDescription());
         return taskRepository.save(task);
    }

    public void delete(String id) {
        taskRepository.deleteById(id);
    }

    public Task create(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(Status.TODO);
        task.setPosition(taskRepository.countTasksByStatus(Status.TODO));

        return taskRepository.save(task);
    }

    @Transactional
    public void changePosition(TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskRequest.getId())
                                .orElseThrow(TaskNotFoundException::new);
        Long oldPosition = task.getPosition();
        Long newPosition = taskRequest.getPosition();
        Status oldStatus = task.getStatus();
        if(oldStatus.equals(taskRequest.getStatus())){
            if(newPosition > oldPosition){
                taskRepository.decrementAboveToPosition(newPosition, oldPosition, oldStatus, task.getId());
            } else {
                taskRepository.incrementBelowToPosition(newPosition,oldPosition,oldStatus, task.getId());
            }
            task.setPosition(taskRequest.getPosition());
            taskRepository.save(task);
        } else {
            Status newStatus = taskRequest.getStatus();
            taskRepository.decrementBelow(task.getPosition(), oldStatus, task.getId());
            taskRepository.incrementBelow(taskRequest.getPosition(), newStatus, task.getId());

            task.setPosition(taskRequest.getPosition());
            task.setStatus(taskRequest.getStatus());
            taskRepository.save(task);
        }
    }

}
