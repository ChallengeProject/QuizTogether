package me.quiz_together.root.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import me.quiz_together.root.model.supoort.ResultContainer;

@RestController
public class BroadcastController implements ApiController {

    @GetMapping("/broadcast/getBroadcastList")
    public ResultContainer getBroadcastList() {
        return new ResultContainer();
    }

    @GetMapping("/broadcast/getBroadcastById")
    public ResultContainer getBroadcastById() {
        return new ResultContainer();
    }
    @GetMapping("/broadcast/getBroadcastInfo")
    public ResultContainer getBroadcastInfo() {
        return new ResultContainer();
    }
    @GetMapping("/broadcast/getBroadcastForUpdateById")
    public ResultContainer getBroadcastForUpdateById() {
        return new ResultContainer();
    }
    @PostMapping("/broadcast/updateBroadcast")
    public ResultContainer updateBroadcast() {
        return new ResultContainer();
    }

    @PostMapping("/broadcast/sendAnswer")
    public ResultContainer sendAnswer() {
        return new ResultContainer();
    }

    @PostMapping("/broadcast/createBroadcast")
    public ResultContainer createBroadcast() {
        return new ResultContainer();
    }

    @PostMapping("/broadcast/endBroadcast")
    public ResultContainer endBroadcast() {
        return new ResultContainer();
    }

    @PostMapping("/broadcast/startBroadcast")
    public ResultContainer startBroadcast() {
        return new ResultContainer();
    }

}
