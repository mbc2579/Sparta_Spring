package org.example.mention;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.userChannel.UserChannelId;

import java.io.Serializable;
import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MentionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "thread_id")
    private Long threadId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChannelId userChannelId = (UserChannelId) o;
        return Objects.equals(getUserId(), userChannelId.getUserId()) && Objects.equals(getThreadId(), userChannelId.getChannelId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getThreadId());
    }
}
