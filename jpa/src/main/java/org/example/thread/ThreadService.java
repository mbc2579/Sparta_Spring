package org.example.thread;

import org.example.channel.Channel;

import java.util.List;

public interface ThreadService {
    List<Thread> selectNotEmptyThreadList(Channel channel);

    Thread insert(Thread thread);
}
