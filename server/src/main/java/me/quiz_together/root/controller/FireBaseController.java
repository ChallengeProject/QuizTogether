package me.quiz_together.root.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.request.firebase.ChatMessageReq;
import me.quiz_together.root.model.request.firebase.OpenAnswerReq;
import me.quiz_together.root.model.request.firebase.OpenQuestionReq;
import me.quiz_together.root.model.request.firebase.OpenWinnersReq;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.FcmService;

@Slf4j
@RestController
public class FireBaseController implements ApiController {
    @Autowired
    private FcmService fcmService;

    @PostMapping("/firebase/openAnswer")
    public ResultContainer<FcmResponse> openAnswer(@RequestBody @Valid OpenAnswerReq openAnswerReq) {
        log.debug("openAnswerReq : {}", openAnswerReq.toString());
        return new ResultContainer<>(fcmService.sendAnswer(openAnswerReq));
    }

    @PostMapping("/firebase/openQuestion")
    public ResultContainer<FcmResponse> openQuestion(@RequestBody @Valid OpenQuestionReq openQuestionReq) {
        log.debug("openQuestionReq : {}", openQuestionReq.toString());
        return new ResultContainer<>(fcmService.sendQuestion(openQuestionReq));
    }

    @PostMapping("/firebase/openWinners")
    public ResultContainer<FcmResponse> openWinners(@RequestBody @Valid OpenWinnersReq openWinnersReq) {
        log.debug("openWinnersReq : {}", openWinnersReq.toString());

        return new ResultContainer<>(fcmService.sendWinners(openWinnersReq));
    }

    @PostMapping("/firebase/sendChatMessage")
    public ResultContainer<FcmResponse> sendChatMessage(@RequestBody @Valid ChatMessageReq chatMessageReq) {
        return new ResultContainer<>(fcmService.sendChatMessage(chatMessageReq, PushType.CHAT_MESSAGE));
    }

    @PostMapping("/firebase/sendAdminChatMessage")
    public ResultContainer<FcmResponse> sendAdminChatMessage(@RequestBody @Valid ChatMessageReq chatMessageReq) {
        return new ResultContainer<>(fcmService.sendChatMessage(chatMessageReq, PushType.ADMIN_MESSAGE));
    }

}
