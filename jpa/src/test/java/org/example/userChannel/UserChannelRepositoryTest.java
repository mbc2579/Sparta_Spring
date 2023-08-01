package org.example.userChannel;

import org.example.channel.Channel;
import org.example.channel.ChannelRepository;
import org.example.common.PageDTO;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class UserChannelRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    void userJoinChannelTest() {
        // given
        var newChannel = Channel.builder().name("new-channel").build();
        var newUser = User.builder().username("new-user").password("new-pass").build();
        var newUserChannel = newChannel.joinUser(newUser);

        // when
        var savedChannel = channelRepository.save(newChannel);
        var savedUser = userRepository.save(newUser);

        // then
        var foundChannel = channelRepository.findById(savedChannel.getId());
        assert foundChannel.get().getUserChannels().stream().map(UserChannel::getChannel).map(Channel::getName).anyMatch(name -> name.equals(newChannel.getName()));
    }

    @Test
    void userJoinChannelWithCascadeTest() {
        // given
        var newChannel = Channel.builder().name("new-channel").build();
        var newUser = User.builder().username("new_user").password("new-pass").build();
        newChannel.joinUser(newUser);

        // when
        var savedChannel = channelRepository.save(newChannel);
        var savedUser = userRepository.save(newUser);

        // then
        var foundChannel = channelRepository.findById(savedChannel.getId());
        assert foundChannel.get().getUserChannels().stream()
                .map(UserChannel::getChannel)
                .map(Channel::getName)
                .anyMatch(name -> name.equals(newChannel.getName()));
    }

    @Test
    void userCustomFieldSortingTest() {
        // given
        var newUser1 = User.builder().username("new_user").password("new-pass1").build();
        var newUser2 = User.builder().username("new_user").password("new-pass2").build();
        userRepository.save(newUser1);
        userRepository.save(newUser2);

        // when
        var users = userRepository.findByUsernameWithCustomField("new_user", Sort.by("customField"));

        // then
        assert users.get(0).getPassword().equals(newUser1.getPassword());

        // when
        users = userRepository.findByUsernameWithCustomField("new_user", Sort.by("customField").descending()); // 내림차순

        // then
        assert users.get(0).getPassword().equals(newUser2.getPassword());

        var newUser3 = User.builder().username("new_user").password("3").build();
        userRepository.save(newUser3);

        // when
        users = userRepository.findByUsername("new_user", JpaSort.unsafe("LENGTH(password)"));

        // then
        assert users.get(0).getPassword().equals(newUser3.getPassword());
    }

    @Test
    void pageDTOTest() {
        // giver
        var newUser1 = User.builder().username("new_user").password("new-pass1").build();
        var newUser2 = User.builder().username("new_user").password("new-pass2").build();
        var newUser3 = User.builder().username("new_user").password("new-pass3").build();
        userRepository.save(newUser1);
        userRepository.save(newUser2);
        userRepository.save(newUser3);
        var pageDTO = new PageDTO(1, 2, "password");

        // when
        var page = userRepository.findAll(pageDTO.toPageable());

        // then
        assert page.getContent().size() == 2;
    }
}
