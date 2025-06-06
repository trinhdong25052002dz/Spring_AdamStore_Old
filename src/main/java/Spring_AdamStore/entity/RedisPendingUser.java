package Spring_AdamStore.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisPendingUser")
public class RedisPendingUser {

    @Id
    String email;

    String name;
    String phone;
    String password;

    @TimeToLive
     long ttl;
}
