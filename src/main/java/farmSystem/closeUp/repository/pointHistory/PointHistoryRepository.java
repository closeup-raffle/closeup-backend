package farmSystem.closeUp.repository.pointHistory;

import farmSystem.closeUp.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryRepositoryCustom{
}
