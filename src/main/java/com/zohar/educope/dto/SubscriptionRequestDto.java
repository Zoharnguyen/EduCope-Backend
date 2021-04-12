package com.zohar.educope.dto;

import java.util.List;
import lombok.Data;

@Data
public class SubscriptionRequestDto {

    String topicName;
    List<String> tokens;
}
