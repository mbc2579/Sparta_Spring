package org.example.thread;

import org.example.channel.Channel;
import org.example.common.PageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ThreadService {
    List<Thread> selectNotEmptyThreadList(Channel channel);

    Page<Thread> selectMentionedThreadList(Long userId, PageDTO pageDTO);

    Thread insert(Thread thread);
}
