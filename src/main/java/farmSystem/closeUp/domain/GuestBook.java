package farmSystem.closeUp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestBook extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long guestBookId;

    private Long guestBookUserId;
    private String guestBookUserNickname;
    private String guestBookContent;
    private String guestBookUserProfileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName =  "user_id")
    private User creator; //크리에이터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; //일반 유저

    @Builder
    public GuestBook(Long guestBookId, Long guestBookUserId, String guestBookUserNickname, String guestBookContent, String guestBookUserProfileImageUrl){
        this.guestBookId = guestBookId;
        this.guestBookUserId = guestBookUserId;
        this.guestBookUserNickname = guestBookUserNickname;
        this.guestBookUserProfileImageUrl = guestBookUserProfileImageUrl;
        this.guestBookContent = guestBookContent;

    }
}
