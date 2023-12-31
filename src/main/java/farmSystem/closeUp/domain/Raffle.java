package farmSystem.closeUp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Raffle extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raffle_id")
    private Long raffleId;

    @Enumerated(value = EnumType.STRING)
    private WinningInfo winningInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffleProduct_id")
    private RaffleProduct raffleProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Raffle(Long raffleId, WinningInfo winningInfo) {
        this.raffleId = raffleId;
        this.winningInfo = winningInfo;
    }
}
