package me.quiz_together.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.request.firebase.ChatMessageReq;
import me.quiz_together.root.model.request.firebase.OpenAnswerReq;
import me.quiz_together.root.model.request.firebase.OpenQuestionReq;
import me.quiz_together.root.model.request.firebase.OpenWinnersReq;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.FcmService;

@RestController
public class FireBaseController implements ApiController {
    @Autowired
    private FcmService fcmService;

    @PostMapping("/firebase/openAnswer")
    public ResultContainer<FcmResponse> openAnswer(@RequestBody OpenAnswerReq openAnswerReq) {
        return new ResultContainer<>(fcmService.sendAnswer(openAnswerReq));
    }

    @PostMapping("/firebase/openQuestion")
    public ResultContainer<FcmResponse> openQuestion(@RequestBody OpenQuestionReq openQuestionReq) {
        return new ResultContainer<>(fcmService.sendQuestion(openQuestionReq));
    }

    @PostMapping("/firebase/openWinners")
    public ResultContainer<FcmResponse> openWinners(@RequestBody OpenWinnersReq openWinnersReq) {
        return new ResultContainer<>(fcmService.sendWinners(openWinnersReq));
    }

    @PostMapping("/firebase/sendChatMessage")
    public ResultContainer<FcmResponse> sendChatMessage(@RequestBody ChatMessageReq chatMessageReq) {
        return new ResultContainer<>(fcmService.sendChatMessage(chatMessageReq));
    }

}
