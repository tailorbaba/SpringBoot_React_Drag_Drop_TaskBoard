package com.tailor.react_dragdrop.bootReact.controller;

import com.tailor.react_dragdrop.bootReact.model.Task;
import com.tailor.react_dragdrop.bootReact.model.TaskRequest;
import com.tailor.react_dragdrop.bootReact.repository.TaskRepository;
import com.tailor.react_dragdrop.bootReact.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("tasks")
public class TaskController {

    private TaskService taskService;

    //check for Autowire
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> list(){
        return taskService.getAll();
    }

    @PutMapping("{id}")
    public Task update(@PathVariable String id, @RequestBody TaskRequest taskRequest){
        return taskService.update(taskRequest);
    }

    @PostMapping
    public Task create(@RequestBody TaskRequest taskRequest){
        return taskService.create(taskRequest);
    }

    @PostMapping("/position")
    public void changePosition(@RequestBody TaskRequest taskRequest){
        taskService.changePosition(taskRequest);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        taskService.delete(id);
    }


}
