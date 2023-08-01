package org.example.thread;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadSearchCond {
    private Long channelId;
    private Long mentionedUserId; // 멘션된 유저 아이디
}
