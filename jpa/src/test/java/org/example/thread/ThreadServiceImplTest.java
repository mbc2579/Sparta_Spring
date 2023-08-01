package org.example.thread;

import org.example.channel.Channel;
import org.example.channel.ChannelRepository;
import org.example.common.PageDTO;
import org.example.mention.ThreadMention;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
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
        var mentionedThreads = savedUser.getThreadMentions().stream().map(ThreadMention::getThread).toList();
//        var mentionedThreads = threadService.selectMentionedThreadList(savedUser);

        // then
        assert mentionedThreads.containsAll(List.of(newThread, newThread2));
    }

    @Test
    void getNotEmptyThreadList() {
        // giver
        var newChannel = Channel.builder().name("c1").type(Channel.Type.PUBLIC).build();
        var savedChannel = channelRepository.save(newChannel);
        getTestThread("message", savedChannel);

        Thread newThread2 = getTestThread("", savedChannel);

        // when
        var notEmptyThreads = threadService.selectNotEmptyThreadList(savedChannel);

        // then
        assert !notEmptyThreads.contains(newThread2);
    }

    @Test
    @DisplayName("전체 채널에서 내가 멘션된 쓰레드 상세정보 목록 테스트")
    void selectMentionedThreadListTest() {
        // giver
        var user = getTestUser();
        var newChannel = Channel.builder().name("c1").type(Channel.Type.PUBLIC).build();
        var savedChannel = channelRepository.save(newChannel);
        var thread1 = getTestThread("message", savedChannel, user);
        var thread2 = getTestThread("", savedChannel, user);

        // when
        var pageDTO = PageDTO.builder().currentPage(1).size(100).build();
        var mentionedThreadList = threadService.selectMentionedThreadList(user.getId(), pageDTO);

        // then
        assert mentionedThreadList.getTotalElements() == 2;

    }

    private User getTestUser() {
        var newUser = User.builder().username("new").password("1").build();
        return userRepository.save(newUser);
    }

    private Thread getTestThread(String message, Channel savedChannel) {
        var newThread = Thread.builder().message(message).build();
        newThread.setChannel(savedChannel);
        return threadService.insert(newThread);
    }

    private Thread getTestThread(String message, Channel channel, User mentionedUser) {
        var newThread = getTestThread(message, channel);
        newThread.addMention(mentionedUser);
        return threadService.insert(newThread);
    }
}