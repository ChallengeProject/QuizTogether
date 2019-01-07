package me.quiz_together.root.model.question;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionProp implements Serializable {
    private static final long serialVersionUID = -5081594351621659054L;
    private String title;
    private List<String> options;
}
