package com.example.dashborad_pipe;

import com.example.dashborad_pipe.Model.DataModel;
import com.example.dashborad_pipe.Model.ModelClass;
import com.example.dashborad_pipe.Model.TaskModel;
import com.example.dashborad_pipe.entities.Phase;
import com.example.dashborad_pipe.entities.Taskphase;
import com.example.dashborad_pipe.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private Service service;

    @GetMapping(value = "/")
    public ResponseEntity<?> index(){
        List<Map<String, ?>> result = this.service.getprojectPhases();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = "/alltasks")
    public ResponseEntity<?> alltasks(){
        List<Taskphase> result = this.service.alltasks();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



@PostMapping(value = "/spliteF")
    public ResponseEntity<?> spliteF(@RequestBody ModelClass m){
    System.out.println(m);
        Integer result = this.service.spliteF(m.getId(), m.getPourcent());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(value = "/updatephase")
    public ResponseEntity<?> updatephase(@RequestBody Phase m){
    System.out.println(m);
        service.updatephase(m);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/getGantt")
    public ResponseEntity<?> getGantt(@RequestBody Integer id){

        List<DataModel> dss = service.getGantt(id);
        return new ResponseEntity<>(dss,HttpStatus.OK);
    }

    @PostMapping(value = "/addTask")
    public ResponseEntity<?> addTask(@RequestBody TaskModel taskModel){
        System.out.println(taskModel);
        System.out.println("achraf ouad");
        service.addTask(taskModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }





}
