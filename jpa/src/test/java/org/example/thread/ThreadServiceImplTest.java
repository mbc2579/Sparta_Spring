package org.example.thread;

import org.example.channel.Channel;
import org.example.channel.ChannelRepository;
import org.example.mention.Mention;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ThreadServiceImplTest {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ThreadService threadService;

    @Test
    void getMentionedThreadList() {
        // giver
        var newUser = User.builder().username("new").password("1").build();
        var savedUser = userRepository.save(newUser);
        var newThread = Thread.builder().message("message").build();
        newThread.addMention(newUser);
        threadService.insert(newThread);

        var newThread2 = Thread.builder().message("message2").build();
        newThread2.addMention(newUser);
        threadService.insert(newThread2);

        // when
        // 모든 채널에서 내가 멘션된 쓰레드 목록 조회 기능
        var mentionedThreads = savedUser.getMentions().stream().map(Mention::getThread).toList();
//        var mentionedThreads = threadService.selectMentionedThreadList(savedUser);

        // then
        assert mentionedThreads.containsAll(List.of(newThread, newThread2));
    }

    @Test
    void getNotEmptyThreadList() {
        // giver
        var newChannel = Channel.builder().name("c1").type(Channel.Type.PUBLIC).build();
        var savedChannel = channelRepository.save(newChannel);
        var newThread = Thread.builder().message("message").build();
        newThread.setChannel(savedChannel);
        threadService.insert(newThread);

        var newThread2 = Thread.builder().message("").build();
        newThread2.setChannel(savedChannel);
        threadService.insert(newThread2);

        // when
        var notEmptyThreads = threadService.selectNotEmptyThreadList(savedChannel);

        // then
        assert !notEmptyThreads.contains(newThread2);
    }
}