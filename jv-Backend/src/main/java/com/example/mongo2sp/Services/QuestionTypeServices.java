package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.QuestionTypeRepo;
import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeReqDTO;
import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeResDTO;
import com.example.mongo2sp.model.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionTypeServices {
    @Autowired
    QuestionTypeRepo questiontyperepo;
    public QuestionTypeResDTO  insert(QuestionTypeReqDTO question)
    {
        QuestionType questionType = new QuestionType();
        questionType.setQuestion_type(question.getQuestion_type());

        QuestionType q1 = questiontyperepo.save(questionType);


        return QuestionTypeResDTO.builder()
                .id(q1.getId())
                .question_type(q1.getQuestion_type())
                .build();
    }
    public List<QuestionTypeResDTO> getallquestion()
    {
        List<QuestionType> questionTypeList = questiontyperepo.findAll();

        List<QuestionTypeResDTO> dtoList = new ArrayList<>();
        for(QuestionType i:questionTypeList) {
            QuestionTypeResDTO dto = new QuestionTypeResDTO();
            dto.setId(i.getId());
            dto.setQuestion_type(i.getQuestion_type());
            dtoList.add(dto);

        }
        return dtoList;
    }

}
