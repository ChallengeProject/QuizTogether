package me.quiz_together.root.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import me.quiz_together.root.model.request.broadcast.BroadcastReq;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateReq;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.CurrentBroadcastView;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.broadcast.BroadcastViewService;
import me.quiz_together.root.support.hashid.HashBroadcastId;

@RestController
public class BroadcastController implements ApiController {
    @Autowired
    private BroadcastViewService broadcastViewService;

    @ApiImplicitParam(name = "next", value = "broadcast hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/broadcast/getPagingBroadcastList")
    public ResultContainer<List<CurrentBroadcastView>> getPagingBroadcastList(@RequestParam(defaultValue = "b0") @HashBroadcastId Long next, @RequestParam(defaultValue = "50") int limit) {

        return new ResultContainer<>(broadcastViewService.getCurrentBroadcastViewList(next, limit));
    }

    @ApiImplicitParam(name = "broadcastId", value = "broadcast hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/broadcast/getBroadcastById")
    public ResultContainer<BroadcastView> getBroadcastById(@RequestParam @HashBroadcastId Long broadcastId) {
        return new ResultContainer<>(broadcastViewService.getBroadcastView(broadcastId));
    }
    @GetMapping("/broadcast/getBroadcastInfo")
    public ResultContainer getBroadcastInfo() {
        return new ResultContainer();
    }

    @ApiImplicitParam(name = "broadcastId", value = "broadcast hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/broadcast/getBroadcastForUpdateById")
    public ResultContainer<BroadcastForUpdateView> getBroadcastForUpdateById(@RequestParam @HashBroadcastId Long broadcastId) {
        return new ResultContainer<>(broadcastViewService.getBroadcastForUpdateById(broadcastId));
    }
    @PostMapping("/broadcast/updateBroadcast")
    public ResultContainer updateBroadcast(@RequestBody BroadcastUpdateReq broadcastUpdateReq) {
        broadcastViewService.updateBroadcast(broadcastUpdateReq);
        return new ResultContainer();
    }

    @PostMapping("/broadcast/sendAnswer")
    public ResultContainer sendAnswer() {
        return new ResultContainer();
    }

    @PostMapping("/broadcast/createBroadcast")
    public ResultContainer createBroadcast(@RequestBody BroadcastReq broadcastReq) {
        broadcastViewService.createBroadcast(broadcastReq);
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
